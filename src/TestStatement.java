import java.sql.*;

/**
 * JDBC中常见的Statement
 *
 *  Statement：用于对数据库进行通用访问，在运行时使用静态SQL语句时很有用。Statement接口不能接受参数。stmt = conn.createStatement();
 *  PreparedStatement：当计划要多次使用SQL语句时使用。PreparedStatement接口在运行时接受输入参数。
 *                     String SQL = "Update Employees SET age = ? WHERE id = ?";
 *                     PreparedStatement pstmt = conn.prepareStatement(SQL);
 *
 *  CallableStatement：当想要访问数据库存储过程时使用。CallableStatement接口也可以接受运行时输入参数
 */
public class TestStatement {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "UPDATE t3 SET tel=? where name=?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, "1590000127");
            preparedStatement.setString(2, "name");

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("更新成功！");
            } else {
                System.out.println("未更新！");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("驱动加载失败：" + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
