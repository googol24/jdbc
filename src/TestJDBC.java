
import java.sql.*;

/**
 * JDBC：Java Database Connectivity 用于Java编程语言和数据库之间的数据库无关连接的标准Java API
 *       是一个规范，它提供了一整套接口，允许以一种可移植的访问底层数据库API
 *
 * 架构：
 *      -> JDBC API: 提供了应用程序对JDBC的管理连接。
 *      -> JDBC Driver API: 支持JDBC管理到驱动器连接。
 *      -> JDBC API的使用驱动程序管理器和数据库特定的驱动程序提供透明的连接到异构数据库。
 *      -> JDBC 驱动程序管理器可确保正确的驱动程序来访问每个数据源。该驱动程序管理器能够支持连接到多个异构数据库的多个并发的驱动程序。
 *         JDBC的驱动程序实现了JDBC API中定义的接口，直接与DBMS交互

 *
 * JDBC API提供了以下接口和类：
     DriverManager: 这个类管理数据库驱动程序的列表。确定内容是否符合从Java应用程序使用的通信子协议正确的数据库驱动程序的连接请求。
                    识别JDBC在一定子协议的第一个驱动器将被用来建立数据库连接。
     Driver: 此接口处理与数据库服务器通信。很少直接直接使用驱动程序（Driver）对象，一般使用DriverManager中的对象，它用于管理此类型的对象。
             它也抽象与驱动程序对象工作相关的详细信息
     Connection : 此接口与接触数据库的所有方法。连接对象表示通信上下文，即，与数据库中的所有的通信是通过此唯一的连接对象。
     Statement : 可以使用这个接口创建的对象的SQL语句提交到数据库。一些派生的接口接受除执行存储过程的参数。
     ResultSet: 这些对象保存从数据库后，执行使用Statement对象的SQL查询中检索数据。它作为一个迭代器，可以通过移动它来检索下一个数据。
     SQLException: 这个类用于处理发生在数据库应用程序中的任何错误


 */
public class TestJDBC {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

        try {
            // 根据驱动类名称加载mysql的JDBC驱动，并注册到驱动管理器
            // 也即注册JDBC驱动程序，使JVM将所需要的驱动程序加载到内存
            Class.forName("com.mysql.jdbc.Driver");

            // 也可以这样注册驱动程序
//            DriverManager.registerDriver(new com.mysql.jdbc.Driver());

            // 通过JDBC的驱动程序管理器打开连接，获取连接对象
            // Mysql的数据库URL格式：jdbc:mysql://hostname/databaseName
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/test", "root", "root");

            // 通过连接创建statement（执行SQL语句的载体）
            statement = connection.createStatement();

            // 执行查询并返回结果集
            ResultSet resultSet = statement.executeQuery("SELECT name,tel FROM t3");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String tel = resultSet.getString("tel");

                System.out.println("Name:" + name + ", tel:" + tel);
            }


        } catch (ClassNotFoundException e) {
            System.out.println("未找到驱动类！");
        } catch (SQLException e) {
            System.out.println("SQL异常：" + e.getMessage());
        } finally {

            try {
                // 关闭statement对象
                if (statement != null) {
                    statement.close();
                }

                // 关闭连接
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
