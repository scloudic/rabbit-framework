package com.scloudic.rabbitframework.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

public class DateUtils {
    private static final long dayTime = 24 * 60 * 60 * 1000;
    //java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ssss");

    /**
     * 将输入格式为2004-8-13类型的字符串转换为标准的Date类型
     *
     * @param dateStr dateStr
     * @return date
     */
    public static Date toSimpleDate(String dateStr) {
        String[] list = dateStr.split("-");
        int year = new Integer(list[0]).intValue();
        int month = new Integer(list[1]).intValue();
        int day = new Integer(list[2]).intValue();
        Calendar cale = Calendar.getInstance();
        cale.set(year, month - 1, day, 0, 0, 0);
        return cale.getTime();
    }

    /**
     * 字符转日期类型，转换后格式为yyyy-MM-dd
     *
     * @param str str
     * @return string
     */
    public static String strToDate(String str) {
        Date d = toSimpleDate(str);
        DateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd");

        return format.format(d);
    }

    /**
     * 获取系统当前日期与时间，默认格式为：yyyy-MM-dd HH:mm:ss
     *
     * @return string
     */
    public static String getCurrDataTime() {
        Date now = new Date();
        String s = formatDate(now, "yyyy-MM-dd HH:mm:ss");
        return s;
    }

    public static String getCurrDataTime(String format) {
        Date now = new Date();
        String s = formatDate(now, format);
        return s;
    }


    /**
     * 得到系统当前的时间 格式为hh:mm:ss
     *
     * @return string
     */
    public static final String getSystemCurrentTime() {
        return getCurrDataTime().substring(11, 19);
    }

    /**
     * 根据参数将日期类型转换为字符型。
     *
     * @param date   日期
     * @param format 转换格式，如：yyyy-MM-dd
     * @return string
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat outFormat = new SimpleDateFormat(format);
        String s = outFormat.format(date);
        return s;
    }

    /**
     * 日期转字符型，转换格式为：yyyy-MM-dd
     *
     * @param date date
     * @return string
     */
    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 日期转字符型，转换格式为：yyyy-MM-dd HH:mm:ss
     *
     * @param date data
     * @return string
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期转字符型，转换格式为：yyyy/MM/dd
     *
     * @param myDate myDate
     * @return string
     */
    public static String formatDate2(Date myDate) {
        return formatDate(myDate, "yyyy/MM/dd");
    }

    /**
     * 日期转字符型，转换格式为：yyyyMMdd
     *
     * @param myDate myDate
     * @return string
     */
    public static String formatDate4(Date myDate) {
        return formatDate(myDate, "yyyyMMdd");
    }

    public static int getYear() {
        Calendar cld = Calendar.getInstance();
        cld.setTime(new Date());
        return cld.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar cld = Calendar.getInstance();
        cld.setTime(new Date());
        return cld.get(Calendar.MONTH) + 1;
    }

    public static int getDay() {
        Calendar cld = Calendar.getInstance();
        cld.setTime(new Date());
        return cld.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 字符串转日期，指定格式为：yyyy-MM-dd
     *
     * @param dateStr dateString
     * @return date
     */
    public static Date getDate(String dateStr) {
        return getDate(dateStr, "yyyy-MM-dd");
    }

    /**
     * 根据字符串及日期格式转日期类型
     *
     * @param dateStr    dateStr
     * @param formatText formatText
     * @return date
     */
    public static Date getDate(String dateStr, String formatText) {
        Date d = null;
        if (StringUtils.isNotEmpty(dateStr)) {
            SimpleDateFormat outFormat = new SimpleDateFormat(formatText);
            try {
                d = outFormat.parse(dateStr);
            } catch (ParseException e) {
                return null;
            }
        }
        return d;
    }

    /**
     * 根据时间差多少秒一天结束
     *
     * @param currentDate currentDate
     * @return long
     */
    public static long getRemainSecondsOneDay(Date currentDate) {
        Calendar midnight = Calendar.getInstance();
        midnight.setTime(currentDate);
        midnight.add(midnight.DAY_OF_MONTH, 1);
        midnight.set(midnight.HOUR_OF_DAY, 0);
        midnight.set(midnight.MINUTE, 0);
        midnight.set(midnight.SECOND, 0);
        midnight.set(midnight.MILLISECOND, 0);
        long seconds = (midnight.getTime().getTime() - currentDate.getTime()) / 1000;
        return seconds;
    }

    /**
     * 根据字符串获得下一天日期
     *
     * @param dateStr dateStr
     * @return date
     */
    public static Date getNextDate(String dateStr) {
        return new Date(getDate(dateStr, "yyyy-MM-dd").getTime() + dayTime);
    }

    /**
     * 获取当前时间前后beforeOrAfterDay天时间
     *
     * @param beforeOrAfterDay beforeOrAfterDay
     * @return Date date
     */
    public static Date getDate(int beforeOrAfterDay) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.DATE, beforeOrAfterDay);
        return calendar.getTime();
    }

    /**
     * 获取Date前后afterOrAgo天时间
     *
     * @param date             date
     * @param beforeOrAfterDay beforeOrAfterDay
     * @return Date
     */
    public static Date getDate(Date date, int beforeOrAfterDay) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(GregorianCalendar.DATE, beforeOrAfterDay);
        return calendar.getTime();
    }

    /**
     * 根据二个以yyyyMM格式，获取二者间隔的所有日期字符
     *
     * @param startMonth startMonth
     * @param endMonth   endMonth
     * @return vector
     */
    public static Vector<String> doFormatDate(String startMonth, String endMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Vector<String> vector = new Vector<String>();
        vector.addElement(startMonth);
        if (startMonth.equals(endMonth)) {
            return vector;
        }
        try {
            Date a = sdf.parse(startMonth);
            while (true) {
                a.setMonth(a.getMonth() + 1);
                String fDate = sdf.format(a);
                vector.addElement(fDate);
                if (fDate.equals(endMonth))
                    break;
            }
        } catch (ParseException e) {
            return vector;
        }
        return vector;
    }

    /**
     * 指定日期加n天
     *
     * @param date date
     * @param n    n
     * @return date
     */
    public static Date getNextNDay(Date date, int n) {
        /**
         * 详细设计： 1.指定日期加n天
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.DATE, n);
        return gc.getTime();
    }

    /**
     * 得到二个日期间的间隔天数
     *
     * @param start start
     * @param end   end
     * @return long
     */
    public static long countDay(Date start, Date end) {
        long day = 0;
        try {
            day = (end.getTime() - start.getTime()) / dayTime;
        } catch (Exception e) {
            return 0l;
        }
        return day;
    }

    /**
     * 获得昨天的日期
     *
     * @return 指定日期的上一天 格式:yyyy-MM-dd HH:mm:ss
     */
    public static String getDayBeforeToday() {
        Date date = new Date(System.currentTimeMillis());
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.DATE, -1);
        return formatDateTime(gc.getTime());
    }

    /**
     * 获取下一个月的日期
     *
     * @param date date
     * @return yyyy-MM-dd
     */
    public static String getNextMonthFirstDay(String date) {
        Date date1 = toSimpleDate(date);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date1);
        gc.add(Calendar.MONTH, 1);
        gc.set(Calendar.DATE, 1);
        return formatDate(gc.getTime());
    }

    /**
     * 得到系统时间的下一月的第一天
     *
     * @return 格式:yyyy-MM-dd
     */

    public static synchronized String getNextMonthFirstDay() {
        Date date1 = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date1);
        gc.add(Calendar.MONTH, 1);
        gc.set(Calendar.DATE, 1);
        return formatDate(gc.getTime());
    }

    public static Date getNextMonthFirstDayDate() {
        Date date1 = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date1);
        gc.add(Calendar.MONTH, 1);
        gc.set(Calendar.DATE, 1);
        String[] list0 = gc.getTime().toLocaleString().split(" ");
        String date = list0[0];
        String time = list0[1];
        String[] list1 = date.split("-");
        int year = new Integer(list1[0]).intValue();
        int month = new Integer(list1[1]).intValue();
        int day = new Integer(list1[2]).intValue();
        String[] list2 = time.split(":");
        int hour = new Integer(list2[0]).intValue();
        int min = new Integer(list2[1]).intValue();
        int second = new Integer(list2[2]).intValue();
        Calendar cale = Calendar.getInstance();
        cale.set(year, month - 1, day, 0, 0, 0);
        cale.getTime();

        return cale.getTime();
    }

    /**
     * 获得以后几个月的日期
     *
     * @param months months
     * @return 指定日期的后面几个月 格式:yyyy-MM-dd
     */
    public static synchronized Date getDayAfterMonth(int months) {
        Date date = new Date(System.currentTimeMillis());
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.MONTH, months);
        return gc.getTime();
    }

    /**
     * 将一个日期对象转换成为指定日期、时间格式的字符串。 如果日期对象为空，返回一个空字符串对象.
     *
     * @param theDate       要转换的日期对象
     * @param theDateFormat 返回的日期字符串的格式
     * @return 转换结果
     */
    public static synchronized String toString(Date theDate,
                                               DateFormat theDateFormat) {
        if (theDate == null) {
            return "";
        } else {
            return theDateFormat.format(theDate);
        }
    }

    /**
     * 将输入格式为2004-8-13 12:31:22类型的字符串转换为标准的Date类型
     *
     * @param dateStr dateStr
     * @return date
     */
    public static Date toDate(String dateStr) {
        String[] list0 = dateStr.split(" ");
        String date = list0[0];
        String time = list0[1];
        String[] list1 = date.split("-");
        int year = new Integer(list1[0]).intValue();
        int month = new Integer(list1[1]).intValue();
        int day = new Integer(list1[2]).intValue();
        String[] list2 = time.split(":");
        int hour = new Integer(list2[0]).intValue();
        int min = new Integer(list2[1]).intValue();
        int second = new Integer(list2[2]).intValue();
        Calendar cale = Calendar.getInstance();
        cale.set(year, month - 1, day, hour, min, second);
        return cale.getTime();
    }


    /**
     * 将输入格式为2004-8-13,2004-10-8类型的字符串转换为标准的Date类型,这种Date类型 对应的日期格式为yyyy-MM-dd
     * 00:00:00,代表一天的开始时刻
     *
     * @param dateStr dateStr
     * @return date
     */
    public static Date toDayStartDate(String dateStr) {
        String[] list = dateStr.split("-");
        int year = new Integer(list[0]).intValue();
        int month = new Integer(list[1]).intValue();
        int day = new Integer(list[2]).intValue();
        Calendar cale = Calendar.getInstance();
        cale.set(year, month - 1, day, 0, 0, 0);
        return cale.getTime();

    }

    /**
     * 将输入格式为2004-8-13,2004-10-8类型的字符串转换为标准的Date类型,这种Date类型 对应的日期格式为yyyy-MM-dd
     * 23:59:59,代表一天的结束时刻
     *
     * @param dateStr 输入格式:2004-8-13,2004-10-8
     * @return date
     */
    public static Date toDayEndDate(String dateStr) {
        String[] list = dateStr.split("-");
        int year = new Integer(list[0]).intValue();
        int month = new Integer(list[1]).intValue();
        int day = new Integer(list[2]).intValue();
        Calendar cale = Calendar.getInstance();
        cale.set(year, month - 1, day, 23, 59, 59);
        return cale.getTime();

    }

    public static String compareTwoDate(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date2);
        double timeLong = calendar1.getTimeInMillis() - calendar.getTimeInMillis();
        int hourNum = (int) (timeLong / 1000 / 3600);
        int minuteNum = (int) ((timeLong % (1000 * 3600)) / 1000 / 60);
        int secondNum = (int) (((timeLong % (1000 * 3600)) % (1000 * 60)) / 1000);
        String hourStr = "";
        if (hourNum < 10) {
            hourStr = "0" + new Integer(hourNum).toString();
        } else {
            hourStr = new Integer(hourNum).toString();
        }
        String minuteStr = "";
        if (minuteNum < 10) {
            minuteStr = "0" + new Integer(minuteNum).toString();
        } else {
            minuteStr = new Integer(minuteNum).toString();
        }
        String secondStr = "";
        if (secondNum < 10) {
            secondStr = "0" + new Integer(secondNum).toString();
        } else {
            secondStr = new Integer(secondNum).toString();
        }
        String time = hourStr + ":" + minuteStr + ":" + secondStr;
        return time;
    }

    public static double compareDate(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date2);
        double timeLong = calendar1.getTimeInMillis()
                - calendar.getTimeInMillis();
        return timeLong;
    }

    /**
     * 将两个scorm时间相加
     *
     * @param scormTime1 scorm时间,格式为00:00:00(1..2).0(1..3)
     * @param scormTime2 scorm时间,格式为00:00:00(1..2).0(1..3)
     * @return string
     */
    public static synchronized String addTwoScormTime(String scormTime1,
                                                      String scormTime2) {
        int dotIndex1 = scormTime1.indexOf(".");
        int hh1 = Integer.parseInt(scormTime1.substring(0, 2));
        int mm1 = Integer.parseInt(scormTime1.substring(3, 5));
        int ss1 = Integer.parseInt(scormTime1.substring(6, dotIndex1));
        int ms1 = Integer.parseInt(scormTime1.substring(dotIndex1 + 1,
                scormTime1.length()));

        int dotIndex2 = scormTime2.indexOf(".");
        int hh2 = Integer.parseInt(scormTime2.substring(0, 2));
        int mm2 = Integer.parseInt(scormTime2.substring(3, 5));
        int ss2 = Integer.parseInt(scormTime2.substring(6, dotIndex2));
        int ms2 = Integer.parseInt(scormTime2.substring(dotIndex1 + 1,
                scormTime2.length()));

        int hh = 0;
        int mm = 0;
        int ss = 0;
        int ms = 0;

        if (ms1 + ms2 >= 1000) {
            ss = 1;
            ms = ms1 + ms2 - 1000;
        } else {
            ms = ms1 + ms2;
        }
        if (ss1 + ss2 + ss >= 60) {
            mm = 1;
            ss = ss1 + ss2 + ss - 60;
        } else {
            ss = ss1 + ss2 + ss;
        }
        if (mm1 + mm2 + mm >= 60) {
            hh = 1;
            hh = mm1 + mm2 + mm - 60;
        }
        hh = hh + hh1 + hh2;

        StringBuffer sb = new StringBuffer();
        if (hh < 10) {
            sb.append("0").append(hh);
        } else {
            sb.append(hh);
        }
        sb.append(":");
        if (mm < 10) {
            sb.append("0").append(mm);
        } else {
            sb.append(mm);
        }
        sb.append(":");
        if (ss < 10) {
            sb.append("0").append(ss);
        } else {
            sb.append(ss);
        }
        sb.append(".");
        if (ms < 10) {
            sb.append(ms).append("00");
        } else if (ms < 100) {
            sb.append(ms).append("0");
        } else {
            sb.append(ms);
        }
        return sb.toString();
    }

    /**
     * 根据timeType返回当前日期与传入日期的差值（当前日期减传入日期） 当要求返回月份的时候，date的日期必须和当前的日期相等，
     * 否则返回0（例如：2003-2-23 和 2004-6-12由于23号和12号不是同一天，固返回0， 2003-2-23 和 2005-6-23
     * 则需计算相差的月份，包括年，此例应返回28（个月）。 2003-2-23 和 2001-6-23
     * 也需计算相差的月份，包括年，此例应返回-20（个月））
     *
     * @param date     要与当前日期比较的日期
     * @param timeType 0代表返回两个日期相差月数，1代表返回两个日期相差天数
     * @return 根据timeType返回当前日期与传入日期的差值
     */
    public static int compareDateWithNow(Date date, int timeType) {
        Date now = Calendar.getInstance().getTime();
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTime(now);
        calendarNow.set(Calendar.HOUR, 0);
        calendarNow.set(Calendar.MINUTE, 0);
        calendarNow.set(Calendar.SECOND, 0);
        // System.out.println("calendarNow "+calendarNow.getTime());

        Calendar calendarPara = Calendar.getInstance();
        calendarPara.setTime(date);
        calendarPara.set(Calendar.HOUR, 0);
        calendarPara.set(Calendar.MINUTE, 0);
        calendarPara.set(Calendar.SECOND, 0);
        // System.out.println("calendarPara "+calendarPara.getTime());
        float nowTime = now.getTime();
        float dateTime = date.getTime();
        if (timeType == 0) {
            if (calendarNow.get(Calendar.DAY_OF_YEAR) == calendarPara
                    .get(Calendar.DAY_OF_YEAR))
                return 0;
            return (calendarNow.get(Calendar.YEAR) - calendarPara
                    .get(Calendar.YEAR))
                    * 12
                    + calendarNow.get(Calendar.MONTH)
                    - calendarPara.get(Calendar.MONTH);
        } else {
            float result = nowTime - dateTime;
            float day = 24 * 60 * 60 * 1000;
            // System.out.println("day "+day);
            result = (result > 0) ? result : -result;
            // System.out.println(result);
            result = result / day;
            Float resultFloat = new Float(result);
            float fraction = result - resultFloat.intValue();
            if (fraction > 0.5) {
                return resultFloat.intValue() + 1;
            } else {
                return resultFloat.intValue();
            }
        }
    }

    public static int compareTowDate(String dateStr, String dateStr1) {
        Date date = toSimpleDate(dateStr);
        Date date1 = toSimpleDate(dateStr1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.set(Calendar.HOUR, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        return (calendar.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR))
                * 12 + calendar.get(Calendar.MONTH)
                - calendar1.get(Calendar.MONTH);
    }

    /**
     * 去掉毫秒
     * <p>
     * 输入格式： 2003-07-18 00:00:00.0 返回格式： 2003-07-18 00:00:00 如果输入时间没有到毫秒级别
     * 则返回原时间
     *
     * @param date date
     * @return string
     */
    public static String dateRemovedMillisecond(String date) {
        if (date == null) {
            return "";
        }
        if (date.toString().length() > 18) {
            return date.toString().substring(0, 19);
        } else {
            return date.toString();
        }
    }

    /**
     * 比较2个时间(String)的大小 输入格式 2006-01-1 ,2006-3-3
     *
     * @param s1 s1
     * @param s2 s1
     * @return boolean
     */
    public static boolean contrastDate(String s1, String s2) {
        long s = toSimpleDate(s1).getTime() - toSimpleDate(s2).getTime();
        if (s >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 比较s1是否大于等于当前时间 输入格式 2006-01-1
     *
     * @param s1 s1
     * @return boolean
     */
    public static boolean contrastDate(String s1) {
        long s = toSimpleDate(s1).getTime()
                - toSimpleDate(formatDate(new Date())).getTime();
        if (s >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 得到当前日期前的日期
     *
     * @param day_i day_i
     * @return string
     */
    public static final String getBefDateStringFormer(int day_i) {
        Date date = new Date(System.currentTimeMillis() - day_i * 24 * 60 * 60
                * 1000);
        SimpleDateFormat formattxt = new SimpleDateFormat("yyyy-MM-dd");
        return formattxt.format(date);
    }

    /**
     * 得到当前日期后的日期
     *
     * @param day_i day_i
     * @return string
     */
    public static final String getBefDateStringAfter(int day_i) {
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE, day_i);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(day.getTime());
    }

    /**
     * 计算周
     *
     * @return string
     */
    public static String weekDate() {
        Date d = new Date();
        String endDate = "";
        String startDate = "";
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        String mydate = formatter.format(d);
        if (mydate.equals("星期一")) {
            endDate = getBefDateStringAfter(6); // 加
            startDate = getBefDateStringFormer(0); // 减
        } else if (mydate.equals("星期二")) {
            endDate = getBefDateStringAfter(5); // 加
            startDate = getBefDateStringFormer(1); // 减
        } else if (mydate.equals("星期三")) {
            endDate = getBefDateStringAfter(4); // 加
            startDate = getBefDateStringFormer(2); // 减
        } else if (mydate.equals("星期四")) {
            endDate = getBefDateStringAfter(3); // 加
            startDate = getBefDateStringFormer(3); // 减
        } else if (mydate.equals("星期五")) {
            endDate = getBefDateStringAfter(2); // 加
            startDate = getBefDateStringFormer(4); // 减
        } else if (mydate.equals("星期六")) {
            endDate = getBefDateStringAfter(1); // 加
            startDate = getBefDateStringFormer(5); // 减
        } else {
            endDate = getBefDateStringAfter(0); // 加
            startDate = getBefDateStringFormer(6); // 减
        }
        return startDate + "," + endDate;
    }

    /**
     * 计算季
     *
     * @return string
     */
    public static String season() {
        String endDates = "";
        String startDates = "";
        SimpleDateFormat fayear = new SimpleDateFormat("yyyy");
        Date date = new Date();
        SimpleDateFormat faM = new SimpleDateFormat("M");
        Integer month = Integer.parseInt(faM.format(new Date()));
        if (month.intValue() >= 1 && month.intValue() <= 3) {
            endDates = fayear.format(date) + "-03-31";
            startDates = fayear.format(date) + "-01-01";
        } else if (month.intValue() >= 4 && month.intValue() <= 6) {
            endDates = fayear.format(date) + "-06-30";
            startDates = fayear.format(date) + "-04-01";
        } else if (month.intValue() >= 7 && month.intValue() <= 9) {
            endDates = fayear.format(date) + "-09-30";
            startDates = fayear.format(date) + "-07-01";
        } else if (month.intValue() >= 10 && month.intValue() <= 12) {
            endDates = fayear.format(date) + "-12-31";
            startDates = fayear.format(date) + "-10-01";
        }
        return startDates + "," + endDates;
    }

    /**
     * 计算年
     *
     * @return string
     */
    public static String year() {
        SimpleDateFormat fayear = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return fayear.format(date);
    }

    /**
     * 计算月
     *
     * @return string
     */
    public static String month() {
        SimpleDateFormat fa = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return fa.format(date);
    }


    /**
     * 根据时间断获取相差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 返回格式：天,时,分,秒
     */
    public static String getHourMinSedMs(Date startDate, Date endDate) {
        long between = 0;
        try {
            between = (endDate.getTime() - startDate.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {

        }
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                - min * 60 * 1000 - s * 1000);
        return day + "," + hour + "," + min + "," + s + "," + ms;
    }
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
