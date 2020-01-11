import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试文章树结构
 */
public class TestArticleTree {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/bbs";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;

    // map id -> article
    private static Map<Integer, Article> articleMap = new HashMap<>();

    // map pid -> articles
    private static Map<Integer, List<Article>> map = new HashMap<>();

    public static void main(String[] args) {
        try {
            // 初始化连接
            init();

            // 构建目录树

            // 寻找pid=0的集合（根集合）
            List<Article> rootList = map.get(0);

            System.out.println("----文章树结构----");
            for (Article article : rootList) {
                int articleId = article.getId();
                // 递归打印树
                tree(articleId, 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放连接
            close();
        }

    }

    private static void init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM article");

            // 结果集装入两个Map
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int pid = resultSet.getInt("pid");
                int rootId = resultSet.getInt("rootid");
                String title = resultSet.getString("title");
                int isLeaf = resultSet.getInt("isleaf");

                Article article = new Article(id, pid, rootId, title, isLeaf);

                // 装入articleMap
                if (!articleMap.containsKey(id)) {
                    articleMap.put(id, article);
                }

                // 装入父目录映射map
                List<Article> articles;
                if (map.containsKey(pid)) {
                    articles = map.get(pid);
                } else {
                    articles = new ArrayList<>();
                }
                articles.add(article);
                map.put(pid, articles);
            }

            // 打印MAP
            System.out.println("----MAP结构----");
            for (Map.Entry<Integer, Article> entry : articleMap.entrySet()) {
                System.out.println(entry.getKey() + " => " + entry.getValue());
            }

            System.out.println("----PID MAP结构----");
            for (Map.Entry<Integer, List<Article>> entry : map.entrySet()) {
                System.out.println("PID " + entry.getKey() + " => " + entry.getValue());
            }

        } catch (ClassNotFoundException e) {
            System.out.println("没有找到驱动类");
        } catch (SQLException e) {
            System.out.println("SQL异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }

            if (statement != null) {
                statement.close();
                statement = null;
            }

            if (connection != null) {
                connection.close();
                connection = null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建某个指定文章的子树
     *
     * @param articleId int 文章id
     * @param level int 层级（根为0，以此类推）
     */
    private static void tree(int articleId, int level) {
        // 返回该articleId的子文章
        Article article = articleMap.get(articleId);

        StringBuilder preStr = new StringBuilder("");
        for (int i = 0; i < level; i++) {
            preStr.append("    ");
        }

        String echoStr = preStr + article.getTitle();
        System.out.println(echoStr);

        if (article.getIsLeaf() != 0) {
            // 获取子文章递归处理
            List<Article> articles = map.get(articleId);
            for (Article child : articles) {
                tree(child.getId(), level+1);
            }
        }
    }
}

class Article {
    private int id;
    private int pid;
    private int rootId;
    private String title;
    // 0-Leaf，1-非Leaf
    private int isLeaf;

    Article(int id, int pid, int rootId, String title, int isLeaf) {
        this.id = id;
        this.pid = pid;
        this.rootId = rootId;
        this.title = title;
        this.isLeaf = isLeaf;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getIsLeaf() {
        return isLeaf;
    }

    @Override
    public String toString() {
        return "id:" + this.getId() + ", title:" + this.getTitle();
    }
}
