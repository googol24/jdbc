import java.sql.*;

/**
 * JDBC 批量处理
 */
public class TestBatch {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            statement = connection.createStatement();

            connection.setAutoCommit(false);
            String sql1 = "INSERT INTO user_account(`user_name`, `balance`) VALUES ('wangwu', 2000)";
            statement.addBatch(sql1);
            String sql2 = "INSERT INTO user_account(`user_name`, `balance`) VALUES ('liuliu', 3000)";
            statement.addBatch(sql2);

            int[] count = statement.executeBatch();

            for (int i : count) {
                System.out.println(i);
            }

            connection.commit();
            connection.setAutoCommit(true);

        } catch (ClassNotFoundException e) {
            System.out.println("注册JDBC驱动失败：" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL异常：" + e.getMessage());
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.rollback();
                }
            } catch (SQLException se) {
                System.out.println("SQL回滚异常：" + e.getMessage());
            }
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("关闭资源异常：" + e.getMessage());
            }
        }
    }
}
