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
    private String specList;
    private String brand;
    private String place;
    private String factory;
    private String method;
    private String life;
    private String type;
    private String remarks;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLife() {
        return life;
    }

    public void setLife(String life) {
        this.life = life;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSpecList() {
        return specList;
    }

    public void setSpecList(String specList) {
        this.specList = specList;
    }
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
