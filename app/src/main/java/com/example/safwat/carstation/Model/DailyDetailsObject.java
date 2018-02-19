package com.example.safwat.carstation.Model;


import java.util.Date;

/**
 * Created by safwat on 12/07/17.
 */

public class DailyDetailsObject {
    private String carName,driverName,description;
    private String  date  ;
    private Integer id_car,id_driver,daily_id,change_oil;
    private double morning_cost,night_cost,total_cost,expense_cost,kilos;

    public DailyDetailsObject(String carName, String driverName, String description, String date,
                              Integer id_car, Integer id_driver, Integer daily_id,
                              Integer change_oil, double morning_cost, double night_cost,
                              double total_cost, double expense_cost, double kilos) {
        this.carName = carName;
        this.driverName = driverName;
        this.description = description;
        this.date = date;
        this.id_car = id_car;
        this.id_driver = id_driver;
        this.daily_id = daily_id;
        this.change_oil = change_oil;
        this.morning_cost = morning_cost;
        this.night_cost = night_cost;
        this.total_cost = total_cost;
        this.expense_cost = expense_cost;
        this.kilos = kilos;

    }

//
//    public DailyDetailsObject(String driverName, double morning_cost, double night_cost,
//                              double total_cost, double expense_cost , String  description
//                            , Integer daily_id ,Integer id_driver) {
//        this.driverName = driverName;
//        this.morning_cost = morning_cost;
//        this.night_cost = night_cost;
//        this.total_cost = total_cost;
//        this.expense_cost = expense_cost;
//        this.description = description;
//        this.daily_id=daily_id;
//        this.id_driver=id_driver;
//
//    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId_car() {
        return id_car;
    }

    public void setId_car(Integer id_car) {
        this.id_car = id_car;
    }

    public Integer getId_driver() {
        return id_driver;
    }

    public void setId_driver(Integer id_driver) {
        this.id_driver = id_driver;
    }

    public Integer getDaily_id() {
        return daily_id;
    }

    public void setDaily_id(Integer daily_id) {
        this.daily_id = daily_id;
    }

    public Integer getChange_oil() {
        return change_oil;
    }

    public void setChange_oil(Integer change_oil) {
        this.change_oil = change_oil;
    }

    public double getMorning_cost() {
        return morning_cost;
    }

    public void setMorning_cost(double morning_cost) {
        this.morning_cost = morning_cost;
    }

    public double getNight_cost() {
        return night_cost;
    }

    public void setNight_cost(double night_cost) {
        this.night_cost = night_cost;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public double getExpense_cost() {
        return expense_cost;
    }

    public void setExpense_cost(double expense_cost) {
        this.expense_cost = expense_cost;
    }

    public double getKilos() {
        return kilos;
    }

    public void setKilos(double kilos) {
        this.kilos = kilos;
    }
}
