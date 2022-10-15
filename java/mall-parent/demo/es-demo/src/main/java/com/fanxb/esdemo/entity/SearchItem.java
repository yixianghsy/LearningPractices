package com.fanxb.esdemo.entity;

import java.io.Serializable;

public class SearchItem implements Serializable{
    private String id;
    private String title;
    private String sell_point;
    private long price;
    private String image;
    private String category_name;
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SearchItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", sell_point='" + sell_point + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", category_name='" + category_name + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSell_point() {
        return sell_point;
    }
    public void setSell_point(String sell_point) {
        this.sell_point = sell_point;
    }
    public long getPrice() {
        return price;
    }
    public void setPrice(long price) {
        this.price = price;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getCategory_name() {
        return category_name;
    }
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String[] getImages() {
        if (image != null && !"".equals(image)) {
            String[] strings = image.split(",");
            return strings;
        }
        return null;
    }
    public SearchItem(String id , String title, String sell_point, long price, String image, String category_name){
        this.id=id;
        this.title=title;
        this.sell_point=sell_point;
        this.price=price;
        this.image=image;
        this.category_name=category_name;
    }

}
