
package com.example.myfirstapp;

public class LaundryUser{

    String name;
    String dorm;
    String year;

    public LaundryUser(){
        name = "";
        dorm = "";
        year = "";
    }

    public void setName(String name){this.name = name;}
    public void setDorm(String dorm){this.dorm = dorm;}
    public void setYear(String year){this.year = year;}

    public String getName(){ return name; }
    public String getDorm(){ return dorm; }
    public String getYear(){ return year; }

}