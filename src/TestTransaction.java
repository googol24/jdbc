import java.sql.*;

/**
 * 测试JDBC事务
 */
public class TestTransaction {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        // 模拟一个转账事务
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 关闭自动提交
            connection.setAutoCommit(false);
            String sql1 = "UPDATE user_account SET balance=balance-? WHERE user_id=?";
            String sql2 = "UPDATE user_account SET balance=balance+? WHERE user_id=?";

            preparedStatement = connection.prepareStatement(sql1);
            preparedStatement.setDouble(1, 200d);
            preparedStatement.setInt(2, 1);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(sql2);
            preparedStatement.setDouble(1, 200d);
            preparedStatement.setInt(2, 2);
            preparedStatement.executeUpdate();

            // 事务提交
            connection.commit();

            // 开启自动提交
            connection.setAutoCommit(true);

        } catch (ClassNotFoundException e) {
            System.out.println("驱动加载失败：" + e.getMessage());
        } catch (SQLException e) {
            // 恢复现场
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException se) {
                System.out.println("SQL异常1：" + se.getMessage());
            }
            System.out.println("SQL异常2：" + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
