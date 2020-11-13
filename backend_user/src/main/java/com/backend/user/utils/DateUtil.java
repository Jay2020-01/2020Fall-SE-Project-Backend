package com.backend.user.utils;


import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * date工具
 */
@Component
public class DateUtil {


    /**
     * 获取当前date格式时间
     *
     * @return
     */
    public Date getCurrentDate() {

        return new Date();
    }


    //打印当前日期
    public String printDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 输入年-月-日  转化为date类型
     **/
    public Date calendarToData(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();//日历类的实例化
        calendar.set(year, month - 1, day);//设置日历时间，月份必须减一
        Date date = calendar.getTime(); // 从一个 Calendar 对象中获取 Date 对象
        return date;
    }

    /**
     * 获得一个 Date 对象实例
     **/
    public Calendar dataToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 设置时间
     *
     * @param year
     * @param month
     * @param date
     * @return
     */
    public Calendar setCalendar(int year, int month, int date) {
        Calendar cl = Calendar.getInstance();
        cl.set(year, month - 1, date);
        return cl;
    }

    /**
     * 获取当前时间的前一天时间
     *
     * @param cl
     * @return
     */
    public Calendar getBeforeDay(Calendar cl) {
        //使用roll方法进行向前回滚
        //cl.roll(Calendar.DATE, -1);
        //使用set方法直接进行设置
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day - 1);
        return cl;
    }

    /**
     * 获取当前时间的后一天时间
     *
     * @param cl
     * @return
     */
    public Calendar getAfterDay(Calendar cl) {
        //使用roll方法进行回滚到后一天的时间
        //cl.roll(Calendar.DATE, 1);
        //使用set方法直接设置时间值
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day + 1);
        return cl;
    }

    /**
     * 打印时间
     *
     * @param cl
     */
    public void printCalendar(Calendar cl) {
        int year = cl.get(Calendar.YEAR);
        int month = cl.get(Calendar.MONTH) + 1;
        int day = cl.get(Calendar.DATE);
        System.out.println(year + "-" + month + "-" + day);
    }

    /**
     * 判断是否是今天
     **/
    public boolean isToday(Date date) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH) + 1;
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        if (year1 == year2 && month1 == month2 && day1 == day2) {
            return true;
        }
        return false;
    }

    /**
     * 系统当前时间转化为秒
     *
     * @return
     */
    public long getUnixStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 字符串转换为对应日期
     *
     * @param source
     * @param pattern "yyyy-MM-dd"
     * @return
     */
    public Date stringToDate(String source, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(source);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * Date类型转为指定格式的String类型
     *
     * @param source
     * @param pattern
     * @return
     */
    public String DateToString(Date source, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(source);
    }

    /**
     * 获取前一天的时间的 年-月-日
     *
     * @return
     */
    public String getYestoryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    /**
     * 获取当前时间的 年-月-日
     *
     * @return
     */
    public String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    /**
     * 将当前时间秒的形式（1538129545）转换为年月日-时分秒（2018-09-28 18:12:25）
     *
     * @param timeStamp
     * @return
     */
    public String timeStampToStr(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 将当前时间秒的形式（1538129545）转换为年月日（2018-09-28）
     *
     * @param timeStamp 时间戳
     * @return
     */
    public String formatDate(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 当前时间秒的形式转化为 HH:mm:ss
     *
     * @param timeStamp 时间戳
     * @return
     */
    public String getTime(long timeStamp) {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        String[] split = date.split("\\s");
        if (split.length > 1) {
            time = split[1];
        }
        return time;
    }

    /**
     * 判断目标日期距离当前日期多长时间
     *
     * @param timeStamp
     * @return
     */
    public String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "月";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年";
        } else {
            return "刚刚";
        }
    }

    /**
     * 计算传入时间和系统时间相差几分钟
     *
     * @param date
     * @return
     */
    public long dateDiffMinute(Date date) {
        long curTime = System.currentTimeMillis();//当前时间秒数
        long time = curTime - date.getTime();//时间差
        return time / 1000 / 60;   //  除1000 当前秒数  除60 分钟数
    }

    /**
     * 获取当天中午12点的时间（毫秒）
     **/
    public Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    /**
     * 获取下一天中午12点的时间（毫秒）
     **/
    public Long getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }


    /**
     * 把毫秒转化成日期
     *
     * @param dateFormat (日期格式，例如：MM/ dd/yyyy HH:mm:ss)
     * @param millSec    (毫秒数)
     * @return
     */
    private String transferLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

}
