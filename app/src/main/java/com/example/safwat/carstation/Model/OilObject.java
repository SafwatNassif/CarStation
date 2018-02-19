package com.example.safwat.carstation.Model;

/**
 * Created by safwat on 02/10/17.
 */

public class OilObject {
    private String carName,date;

    public OilObject(String carName, String date) {
        this.carName = carName;
        this.date = date;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

