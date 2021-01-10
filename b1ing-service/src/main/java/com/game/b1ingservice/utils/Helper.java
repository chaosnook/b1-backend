package com.game.b1ingservice.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

public class Helper {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATETIME_FORMAT = "dd/MM/yyyy";
    public static final FastDateFormat DDMMYYYY = FastDateFormat.getInstance(DATE_FORMAT);
    public static final FastDateFormat DDMMYYYY_HHMMSS = FastDateFormat.getInstance("dd/MM/yyyy_HH-mm-");
    public static final FastDateFormat DATE_FORMAT_ss = FastDateFormat.getInstance("ss");

    public static String roundHalfSec(int sec) {
        int mod = sec % 30;
        int res = 0;
        if ((mod) > 0) {
            res = 30;
        }
        return StringUtils.leftPad(String.valueOf(res), 2, "0");
    }

    public static String getHalfSec(long toEpochMilli) {
        return DDMMYYYY_HHMMSS.format(toEpochMilli) + roundHalfSec(Integer.parseInt(DATE_FORMAT_ss.format(toEpochMilli)));
    }
}
