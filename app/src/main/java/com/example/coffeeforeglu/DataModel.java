package com.example.coffeeforeglu;

public class DataModel {

    private long date;
    private String name;
    private String type;
    private String size;
    private String price;

    DataModel(){

    }
    DataModel(long date, String name, String type, String size, String price){
        this.date = date;
        this.name = name;
        this.type = type;
        this.size = size;
        this.price = price;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
