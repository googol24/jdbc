import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 日期类型处理
 *
 * Java MySql
 *
 * 历史：java.util.Date -> java.util.Calendar ->
 *
 * UT（Universal Time 世界时）是基于天体观察计算出来的时间。
 *      UT本身是一个广泛的概念，其下包括UT0，UT1,UT2等。其中UT0是完全按照天体运行计算出来的时间，UT1是在UT0的基础上做了一些调整，UT2是在UT0和UT1的基础上又进行了一些调整。由于天体运行的一些不确定性(比如地球的自转并非匀速的，而是以复杂的方式进行着加速和减速)，所以UT时间并不是均匀流过的。
 * UTC（Universal Time Coordinate 协调世界时）是基于原子时钟的时间。
 *      什么是原子时钟？个人认为就是一个很小的，长度固定的，不可再分的时间段。所以UTC的时间是均匀的。为了能够尽量减小和UT时间的误差，UTC引入了闰秒（在某些年份的最后一分钟是61秒），以确保UTC是UT1之间的误差在0.9秒之内。
 * GMT（Greenwish Mean Time 格林威治平时），这是UTC的民间名称。GMT=UTC
 *
 * 常见的时间处理类：
 *      java.lang.System（里面有currentTimeMillis()等方法）
 *      java.util.Data
 *      java.util.Calendar
 *      java.util.GregorianCalendar 方便时区的处理
 *      java.util.Timezone
 *      java.text.DateFormat
 *      java.text.SimpleDateFormat
 *      java.sql.Data
 *      java.sql.Time
 *      java.sql.Timestamp
 *
 */
public class TestDate {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/bbs", "root", "root");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT pdate FROM article");
            while (resultSet.next()) {
                // resultSet.getDate() 截取掉了时间里面的时分秒，只有日期类型，没有时间
                Date d = resultSet.getDate("pdate");

                // 获取年月日
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                System.out.println("日期: " + sdf.format(d));

                // 获取时分秒 该函数返回java.sql.Time，该类继承了java.util.Date
                Time t = resultSet.getTime("pdate");
                sdf = new SimpleDateFormat("HH时mm分ss秒");
                System.out.println("时间： " + sdf.format(t));

                // 获取年月日时分秒
                Timestamp ts = resultSet.getTimestamp("pdate");
                sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
                System.out.println("年月日时分秒： " + sdf.format(ts));

                // 仅仅获取日期里面的月份
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);
                System.out.println("月份：" + calendar.get(Calendar.MONTH));
            }
        } catch (ClassNotFoundException e) {
            System.out.println("异常：未找到驱动类");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL 异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
