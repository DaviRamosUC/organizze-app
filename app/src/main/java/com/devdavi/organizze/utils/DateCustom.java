package com.devdavi.organizze.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCustom {

    public static String dataAtual() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date(date));
    }
}
