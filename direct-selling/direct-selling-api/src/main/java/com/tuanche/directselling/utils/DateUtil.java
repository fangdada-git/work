package com.tuanche.directselling.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
 
    private DateUtil() {
    }
 
    public static final String FORMART_YMD = "yyyy-MM-dd";
 
    public static final String FORMART_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
 
    public static final String FORMART_YMD_HMS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
 
    public static final String FORMART_YEAR = "yyyy";
 
    public static final String TIME_BEGIN = " 00:00:00";
 
    public static final String TIME_END = " 23:59:59";
 
    public static final long SECOND_OF_DAY = 60 * 60 * 24;
     
    public static final long MILLISECOND_OF_DAY = 60 * 60 * 24 * 1000;
    /**
     * 默认格式的SimpleDateFormat
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormartDefault() {
        return getSimpleDateFormart(FORMART_YMD_HMS);
    }
    /**
     * 指定格式的SimpleDateFormat
     * @param pattern
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormart(String pattern) {
        return new SimpleDateFormat(pattern);
    }
    /**
     * 格式化日期 默认方式
     * @param date
     * @return
     */
    public static String formartDefault(Date date) {
        return formart(date, FORMART_YMD_HMS);
    }
    /**
     * 指定格式格式化日期
     * @param date
     * @param pattern
     * @return
     */
    public static String formart(Date date, String pattern) {
        return getSimpleDateFormart(pattern).format(date);
    }
    /**
     * 解析指定格式的日期
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date, String pattern){
        try {
			return getSimpleDateFormart(pattern).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
    }
    /**
     * 解析指定日期
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDateDefault(String date){
        try {
			return getSimpleDateFormartDefault().parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
    }
    /**
     * 获取Calendar
     * @return
     */
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }
    /**
     * 获取日期指定日期  所在具体年月日时分秒 时间
     * @param dt 日期
     * @param year 年
     * @param month 月
     * @param date 日
     * @param hourOfDay 时
     * @param minute 分
     * @param second 秒
     * @return
     */
    public static Calendar getCalendar(Date dt, int year,int month, int date, int hour, int minute, int second) {
        Calendar calendar = getCalendar();
        calendar.setTime(dt);
        calendar.add(calendar.YEAR, year);
        calendar.add(calendar.MONTH, month);
        calendar.add(calendar.DATE, date);
        calendar.set(calendar.HOUR_OF_DAY, hour);
        calendar.set(calendar.MINUTE, minute);
        calendar.set(calendar.SECOND, second);
        return calendar;
    }
    /**
     * 获取指定日期 所在年月日的具体时间
     * @param date
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getNextDate(Date date, int year, int month, int day) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.add(calendar.YEAR, year);
        calendar.add(Calendar.MONTH, month);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }
    /**
     * 获取当前日期
     * @return
     */
    public static Date getCurrentDate() {
        return new Date();
    }
     
    /**
     *  获取指定日期所在年份 相差years的年份
     * @param date
     * @param years
     * @return
     */
    public static int getCurrentYear() {
        return getYear(getCurrentDate(), 0);
    }
    /**
     *  获取指定日期所在年份 相差years的年份
     * @param date
     * @param years
     * @return
     */
    public static int getYear(Date date, int years) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.add(calendar.YEAR, years);
        return calendar.get(calendar.YEAR);
    }
     
    /**
     *  获取指定日期相差 年份 所在日期
     * @param date
     * @param years
     * @return
     */
    public static Date getYearDate(Date date, int years) {
        return getNextDate(date, years, 0, 0);
    }
     
    /**
     *  获取指定日期的 相差 days 天数的日期
     * @param date
     * @param days
     * @return
     */
    public static Date getNextDate(Date date, int days) {
        return getNextDate(date, 0, 0, days);
    }
     
    /**
     * 获取指定日期 相差月份所在日期
     * 31号和30号超出上月最大日期会同时对应上个月最大日期
     * @param date
     * @param months
     * @return
     */
    public static Date getNextMonthDate(Date date, int months) {
        return getNextDate(date, 0, months, 0);
    }
     
    /**
     * 获取当前日期下一天
     * @return
     */
    public static Date getNextDate() {
        return getNextDate(getCurrentDate(), 1);
    }
     
    /**
     * 获取当前日期上一天
     * @return
     */
    public static Date getBeforeDate() {
        return getNextDate(getCurrentDate(), -1);
    }
     
    /**
     * 获取当前日期开始时间
     * @param date
     * @return
     */
    public static Date getDateBegin(Date date) {
        return getDate(date, 0, 0, 0);
    }
     
    /**
     * 获取当前日期结束时间
     * @param date
     * @return
     */
    public static Date getDateEnd(Date date) {
        return getDate(date, 23, 59, 59);
    }
     
    /**
     * 获取当前日期 指定时分秒的时间
     * @param date
     * @return
     */
    public static Date getDate(Date date, int hour, int minte, int sencond) {
        Calendar calendar = getCalendar(date, 0, 0, 0, hour, minte, sencond);
        return calendar.getTime();
    }
     
    /**
     * 判断两个日期是否相等
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isSameDay(Date d1, Date d2) {
        long t1 = d1.getTime();
        long t2 = d2.getTime();
        return t1 == t2;
    }
    /**
     * 获取当周 周一
     * @param date
     * @return
     */
    public static Date getCurrentWeekMondy(Date date) {
       return getNextWeekMondy(date, 0);
    }
    /**
     * 根据日期获取当周或下周等
     * 相差周数的周一
     * Calendar默认国外日期   一周：周日-周六
     * @param date
     * @param weeks
     * @return
     */
    public static Date getNextWeekMondy(Date date, int weeks) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        int diff = (-day_of_week + 1) + (weeks * 7);
        calendar.add(Calendar.DATE, diff);
        return calendar.getTime();
    }
    /**
     * 获取日期之间的天数,忽略时分秒的差异
     * @param d1
     * @param d2
     * @return
     */
    public static long getDiffDays(Date d1, Date d2) {
        long t1 = getDateBegin(d1).getTime();
        long t2 = getDateBegin(d2).getTime();
        long diff = t2 -t1;
        long days = diff / MILLISECOND_OF_DAY;
        return days;
    }
    /**
     * 获取日期之间相差的秒数
     * @param d1
     * @param d2
     * @return
     */
    public static long getDiffSecond(Date d1, Date d2) {
        long t1 = d1.getTime();
        long t2 = d2.getTime();
        return (t2 - t1) / 1000;
    }
    /**
     * 获取某年 某月的最后一天
     * @CreatTime 2019-06-12 17:31
     * @param 
     * @return String
     */
    public static String getLastDayOfMonth(int year,int month) {
        Calendar cal = getCalendar();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH, month-1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        String lastDayOfMonth = getSimpleDateFormart(FORMART_YMD).format(cal.getTime());
        return lastDayOfMonth;
    }

    /**
     * 获取上月第一天
     * @author liuwenlei
     * @CreatTime 2019-07-02 11:45
     * @param 
     * @return String
     */
    public static String getFirstDayOfBeforeMonth(String formart) {
    	Calendar cal = getCalendar();
    	cal.add(Calendar.MONTH, -1);
    	cal.set(Calendar.DAY_OF_MONTH,1);
    	String firstDay = getSimpleDateFormart(formart).format(cal.getTime());
    	return firstDay;
    }
    
    /**
     * 获取上月最后一天
     * @author liuwenlei
     * @CreatTime 2019-07-02 11:47
     * @param 
     * @return String
     */
    public static String getLastDayOfBeforeMonth(String formart) {
    	Calendar cal = getCalendar();
    	cal.set(Calendar.DAY_OF_MONTH,0);
    	String endDay = getSimpleDateFormart(formart).format(cal.getTime());
    	return endDay;
    }
    /**
     * 获取指定日期所在月的第一天
     * @author liuwenlei
     * @CreatTime 2019-09-25 10:53
     * @param 
     * @return Date
     */
    public static Date getFirstDayDateOfMonth(final Date date) {
    	Calendar cal = getCalendar();
        cal.setTime(date);
        int last = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        return cal.getTime();
    }
    /**
     * 获取指定日期所在月的最后一天
     * @author liuwenlei
     * @CreatTime 2019-09-25 10:53
     * @param 
     * @return Date
     */
    public static Date getLastDayOfMonth(final Date date) {
        Calendar cal = getCalendar();
        cal.setTime(date);
        int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        return cal.getTime();
    }
    
    public static void main(String[] args) {
		String ds = "2019-02-01";
    	Date d = parseDate(ds, FORMART_YMD);
    	Date f = getFirstDayDateOfMonth(d);
    	Date e = getLastDayOfMonth(d);
    	
    	System.out.println(formartDefault(f));
    	
    	System.out.println(formartDefault(e));
	}
}
