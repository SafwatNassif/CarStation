package com.example.safwat.carstation.Model;

/**
 * Created by safwat on 04/10/17.
 */

public class CollectedMonyModel {
    String  date;
    String name,driver;
    String decleration;
    double moring,night,totalExpense,finalTotal,daily;

    public CollectedMonyModel(String date, String name, String driver, double totalExpense, double finalTotal, double daily,String decleration) {
        this.date = date;
        this.name = name;
        this.driver = driver;
        this.totalExpense = totalExpense;
        this.finalTotal = finalTotal;
        this.daily = daily;
        this.decleration =decleration;
    }

    public CollectedMonyModel(String date, double moring, double night, double totalExpense, double finalTotal,String  decleration) {
        this.date = date;
        this.moring = moring;
        this.night = night;
        this.decleration =decleration;
        this.totalExpense = totalExpense;
        this.finalTotal = finalTotal;
    }
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
    public String getDecleration() {
        return decleration;
    }

    public void setDecleration(String decleration) {
        this.decleration = decleration;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDaily() {
        return daily;
    }

    public void setDaily(double daily) {
        this.daily = daily;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMoring() {
        return moring;
    }

    public void setMoring(double moring) {
        this.moring = moring;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(double finalTotal) {
        this.finalTotal = finalTotal;
    }
}
