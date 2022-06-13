package com.example.cscmp.entity;

public class Nodes extends BaseBean {

    private String id;
    private String category;
    private String name;
    private Float symbolSize;
    private Integer value;
    private Float x;
    private Float y;

    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSymbolSize() {
        return symbolSize;
    }

    public void setSymbolSize(Float symbolSize) {
        this.symbolSize = symbolSize;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }
}
