package com.game.b1ingservice.utils;


import org.apache.commons.lang3.time.FastDateFormat;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final FastDateFormat FORMATTER_YYYYMMDD = FastDateFormat.getInstance("yyyyMMdd");

    public static final FastDateFormat DDMMYYYY = FastDateFormat.getInstance("dd/MM/yyyy");
    public static final FastDateFormat DDMMYYYHHmm = FastDateFormat.getInstance("dd/MM/yyyy HH:mm");
    public static String nowDateFormat(String format) {
        return new SimpleDateFormat(format).format(nowDateUtil());
    }

    public static boolean canCastDate(String dateStr) {
        boolean result = true;
        try {
            DDMMYYYY.parse(dateStr);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public static boolean canCastDateTime(String dateStr) {
        boolean result = true;
        try {
            DDMMYYYHHmm.parse(dateStr);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public static Date convertStartDate(String strDate) {
        try {
            return DateUtils.atStartOfDay(DDMMYYYY.parse(strDate));
        } catch (Exception e) {
            //  log.error("Parse Date Error : {}", e.getMessage());
            return null;
        }
    }

    public static Date convertEndDate(String strDate) {
        try {
            return DateUtils.atEndOfDay(DDMMYYYY.parse(strDate));
        } catch (Exception e) {
            //  log.error("Parse Date Error : {}", e.getMessage());
            return null;
        }
    }

    public static Date convertStartDateTime(String strDate) {
        try {
            return DateUtils.atStartOfDateTime(DDMMYYYHHmm.parse(strDate));
        } catch (Exception e) {
            //  log.error("Parse Date Error : {}", e.getMessage());
            return null;
        }
    }

    public static Date convertEndDateTime(String strDate) {
        try {
            return DateUtils.atEndOfDateTime(DDMMYYYHHmm.parse(strDate));
        } catch (Exception e) {
            //  log.error("Parse Date Error : {}", e.getMessage());
            return null;
        }
    }

    public static Date atStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date atEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return calendar.getTime();
    }

    public static Date atStartOfDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTime();
    }

    public static Date atEndOfDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTime();
    }


    public static Date nowDateUtil() {
        return new Date();
    }


}
