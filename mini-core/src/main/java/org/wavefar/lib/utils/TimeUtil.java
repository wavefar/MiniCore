package org.wavefar.lib.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式工具类
 *
 * @author summer
 */
public class TimeUtil {

    /**
     * 暂时格式化时间不调这个方法
     * 以秒的单位计算 个性时间显示 服务器端由于没有存储毫秒数
     *
     * @param timestamp 以秒为单位
     * @return
     */
    public static String converTime(long timestamp) {
        long currentSeconds = System.currentTimeMillis() / 1000L;
        // 与现在时间相差秒数
        long timeGap = currentSeconds - timestamp;
        String timeStr;
        // 1年以上
        if (timeGap > 24 * 60 * 60 * 30 * 365) {
            timeStr = timeGap / (24 * 60 * 60 * 30 * 365) + "年前";
            // 30天以上
        } else if (timeGap > 24 * 60 * 60 * 30) {
            timeStr = timeGap / (24 * 60 * 60 * 30) + "月前";
            // 1-30天
        } else if (timeGap > 24 * 60 * 60) {
            timeStr = timeGap / (24 * 60 * 60) + "天前";
            // 1小时-24小时
        } else if (timeGap > 60 * 60) {
            timeStr = timeGap / (60 * 60) + "小时前";
            // 1分钟-59分钟
        } else if (timeGap > 60) {
            timeStr = timeGap / 60 + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "1分钟前";
        }
        return timeStr;
    }

    /**
     * 格式化 当前时间
     *
     * @param form 格式字符串 可为空串
     * @return
     */
    public static String getCurrentDateTime(String form) {
        String localform = form;
        if ("".equals(localform)) {
            localform = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat time = new SimpleDateFormat(localform);
        return time.format(new Date());
    }

    /**
     * 格式化 当前时间
     *
     * @param form 格式字符串 可为空串
     * @return
     */
    public static long getCurrentDateTimeToLong(String form) {
        long time = 0;
        try {
            String localform = form;
            if ("".equals(localform)) {
                localform = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(localform);
            String timeStr = dateFormat.format(System.currentTimeMillis());
            Date date = dateFormat.parse(timeStr);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * @param dt   构造Date
     * @param form 格式化字符串 可为空串
     * @return
     */
    public static String dateTime2String(Date dt, String form) {
        return dateTime2String(dt.getTime(), form);
    }

    public static String dateTime2String(long dt, String form) {
        String localform = form;
        if ("".equals(localform)) {
            localform = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat time = new SimpleDateFormat(localform);
        return time.format(dt);
    }

    /**
     * 时间戳转换格式化的时间
     *
     * @param ldt  传入值要是毫秒精度
     *             时间戳
     * @param form yyyy-MM-dd HH:mm:ss 格式化字符串 可为空
     * @return
     */
    public static String getTime2String(long ldt, String form) {
        String localform = form;
        if ("".equals(localform)) {
            localform = "yyyy-MM-dd HH:mm:ss";
        }
        Date dt = new Date(ldt);
        SimpleDateFormat time = new SimpleDateFormat(localform);
        return time.format(dt);
    }

    /**
     * 以字串形式的时间进行计算，增加或减少iHouer小时，返回日期时间字串
     *
     * @param dateString 必须为这样的格式yyyy-MM-dd HH:mm:ss
     * @param hour
     * @return
     */
    public static String stringDateTimePlus(String dateString, int hour) {
        DateFormat dateFormat;
        // 设定格式
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            // util类型
            java.util.Date timeDate = dateFormat.parse(dateString);
            // Timestamp类型,timeDate.getTime()返回一个long型
            java.sql.Timestamp dateTime = new java.sql.Timestamp(
                    timeDate.getTime() + (hour * 60 * 60 * 1000L));
            return dateTime.toString().substring(0, 19);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }

    /**
     * 当前时间戳加减
     *
     * @param pattern
     * @param hour    以小时为单位
     * @return
     */
    public static String timestampPlus(String pattern, int hour) {
        return getTime2String(System.currentTimeMillis() + hour * 60 * 60 * 1000L, pattern);
    }

    /**
     * 字符串解析成Date对象
     *
     * @param dataString 要解析字符串
     * @param pattern    要解析的格式 如 yyyy年MM月dd
     * @return Date
     */
    public static Date parseStringToDate(String dataString, String pattern) {
        // 设定格式
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(dataString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数字补零操作如:01
     *
     * @param obj
     * @return
     */
    public static String timeZeroFill(Object obj) {
        String parten = "00";
        DecimalFormat decimal = new DecimalFormat(parten);
        return decimal.format(obj);
    }

    /**
     * 该方法时间戳精度到s
     * 5分钟之内，显示“刚刚”；
     * 今天之内，显示“今天 XX：XX”；
     * 昨天之内，显示“昨天 XX：XX”；
     * 昨天之前的时间段，显示具体日期“xx月XX日”；
     *
     * @param timestamp 以秒为单位
     * @return
     */
    public static String converFromSecondTime(long timestamp) {
        long cuurentMillis = System.currentTimeMillis();
        long currentSeconds = cuurentMillis / 1000;
        // 与现在时间相差秒数
        long timeGap = currentSeconds - timestamp;
        Calendar targetCalendar = Calendar.getInstance();
        //当前时间
        targetCalendar.setTime(new Date(cuurentMillis));
        int currentDay = targetCalendar.get(Calendar.DAY_OF_YEAR);
        //传递进来的时间
        targetCalendar.setTime(new Date(timestamp * 1000));
        int targetDay = targetCalendar.get(Calendar.DAY_OF_YEAR);
        String timeStr;
        // 处理跨年显示
        if (Math.abs(currentDay - targetDay) > 1) {
            timeStr = getTime2String(timestamp * 1000, "MM月dd日");
        } else if (currentDay - targetDay == 1) {
            timeStr = "昨天" + getTime2String(timestamp * 1000, "HH:mm");
            // 5分钟之内以后
        } else if (timeGap > 60 * 5) {
            timeStr = "今天" + getTime2String(timestamp * 1000, "HH:mm");
            // 5分钟以内
        } else {
            timeStr = "刚刚";
        }
        return timeStr;
    }
}
