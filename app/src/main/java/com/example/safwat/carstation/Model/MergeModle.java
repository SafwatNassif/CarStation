package com.example.safwat.carstation.Model;

/**
 * Created by safwat on 29/08/17.
 */

public class MergeModle {

    private String  car_name ,declearation;
    private Double total,expense,daily_morning,daily_night;


    public MergeModle(String car_name, Double total ,
                      Double daily_moring, Double daily_night, Double expense,String declearation) {
        this.car_name = car_name;
        this.declearation =declearation;
        this.total = total;
        this.expense=expense;
        this.daily_morning=daily_moring;
        this.daily_night=daily_night;
    }

    public String getDeclearation() {
        return declearation;
    }

    public void setDeclearation(String declearation) {
        this.declearation = declearation;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public Double getDaily_morning() {
        return daily_morning;
    }

    public void setDaily_morning(Double daily_morning) {
        this.daily_morning = daily_morning;
    }

    public Double getDaily_night() {
        return daily_night;
    }

    public void setDaily_night(Double daily_night) {
        this.daily_night = daily_night;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }




}
