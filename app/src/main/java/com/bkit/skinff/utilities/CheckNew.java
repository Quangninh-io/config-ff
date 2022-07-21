package com.bkit.skinff.utilities;

import static com.bkit.skinff.utilities.Constants.LIMITED_DATE_SET_NEW;

import android.util.Log;
import android.view.View;

import com.bkit.skinff.model.FileData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class CheckNew {
    private static CheckNew instance;
    public static CheckNew getInstance(){
        if(instance ==null){
            return new CheckNew();
        }
        return instance;
    }
    boolean c = false;
    public boolean check(FileData fileData) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Instant now = Instant.now(); //current date
        Instant before = now.minus(Duration.ofDays(LIMITED_DATE_SET_NEW));
        Date dateBefore = Date.from(before);
        String thirtyDayAgo = simpleDateFormat.format(dateBefore);
        Log.d("time", thirtyDayAgo);
        try {
            Date dateInFirebase = simpleDateFormat.parse(fileData.getTime());
            Date dateInReality = simpleDateFormat.parse(thirtyDayAgo);
            if(dateInFirebase.compareTo(dateInReality)>0){
               c = true;
            }else{
                c= false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c;
    }
}
