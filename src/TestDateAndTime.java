import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestDateAndTime {
    public static void main(String[] args) {
        // 1 获取当前的系统时间

        // 1.1 当前的毫秒级时间戳（一般用于计算一段程序的执行时间ms）
        System.out.println(System.currentTimeMillis());

        // 1.2 使用Date类
        Date d = new Date();
        System.out.println(d);

        // 1.3 使用Calendar类
        Calendar c = Calendar.getInstance();
        System.out.println(c.getTime());

        // 2 格式化时间：使用DateFormat或者SimpleDateFormat

        // 3 String类型的时间字符串转换为时间
        String s = "2018-10-24 08:15:27";
        Timestamp ts = Timestamp.valueOf(s);
        System.out.println(ts);

        // 4 时区处理
        Calendar cJapan = new GregorianCalendar(TimeZone.getTimeZone("Japan"));
        System.out.println(cJapan.get(Calendar.HOUR));

        // 获取所有时区
        for (String str : TimeZone.getAvailableIDs()) {
            System.out.println(str);
        }

        // 5 时间排序
        String s1 = "2019-08-10 22:18:22";
        String s2 = "2018-08-10 22:18:22";
        String s3 = "2018-08-10 22:19:22";
        String s4 = "2019-08-11 22:19:22";

        List<Date> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateList.add(sdf.parse(s1));
            dateList.add(sdf.parse(s2));
            dateList.add(sdf.parse(s3));
            dateList.add(sdf.parse(s4));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 排序前：
        for (Date date : dateList) {
            System.out.println(sdf.format(date));
        }

        System.out.println("排序后：");
        Collections.sort(dateList);

        // 排序后：
        for (Date date : dateList) {
            System.out.println(sdf.format(date));
        }
    }
}
