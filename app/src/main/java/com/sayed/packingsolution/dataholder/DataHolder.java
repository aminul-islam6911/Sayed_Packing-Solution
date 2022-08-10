package com.sayed.packingsolution.dataholder;

import android.net.Uri;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class DataHolder {

    String EMAIL,DESCRIPTION,DATE,STATUS;
    HashMap<String,String> IMAGES;

    public DataHolder(){

    }

    public DataHolder(String EMAIL,String DESCRIPTION,String DATE,String STATUS){
        this.IMAGES = new HashMap<String,String>();

        this.DATE = DATE;
        this.EMAIL = EMAIL;
        this.DESCRIPTION = DESCRIPTION;
        this.STATUS = STATUS;

    }

    public DataHolder(String DESCRIPTION,String EMAIL,HashMap<String,String> images){
        this.DESCRIPTION = DESCRIPTION;
        this.EMAIL = EMAIL;
        this.IMAGES = new HashMap<String,String>();
        this.IMAGES.putAll(images);
    }


    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public HashMap<String, String> getIMAGES() {
        return IMAGES;
    }

    public void setIMAGES(HashMap<String, String> IMAGES) {
        this.IMAGES = IMAGES;
    }
}
