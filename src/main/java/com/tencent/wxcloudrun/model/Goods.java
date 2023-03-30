package com.tencent.wxcloudrun.model;

public class Goods {
    private String id;
    private String desc;
    private String title;
    private double price;
    private String path;
    private String descPath;
    private int category;
    private int level;
    private int stock;

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    private final String baseUri = "https://7072-prod-6gvg13hsf13d23f3-1317305634.tcb.qcloud.la/";
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPath() {
        return baseUri + path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDescPath() {
        return baseUri + descPath;
    }

    public void setDescPath(String descPath) {
        this.descPath = descPath;
    }
}
