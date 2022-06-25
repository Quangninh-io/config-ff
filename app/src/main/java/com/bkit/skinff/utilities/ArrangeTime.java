package com.bkit.skinff.utilities;

import android.annotation.SuppressLint;

import com.bkit.skinff.model.FileData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class ArrangeTime {
    static ArrangeTime instance;
    public static ArrangeTime getInstance(){
        if (instance==null){
            return new ArrangeTime();
        }
        return instance;
    }
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public Comparator<FileData> comparator = (c2, c1) ->{
        try {
            return Math.toIntExact(Long.valueOf(simpleDateFormat.parse(c1.getTime()).compareTo(simpleDateFormat.parse(c2.getTime()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    };
}
