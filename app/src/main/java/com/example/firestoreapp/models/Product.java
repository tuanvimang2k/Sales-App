package com.example.firestoreapp.models;

public class Product {
    private String id;
   private String description;
   private String product_category_name;
   private String product_image;
    private String product_name;
    private String provider_name;
    private int stock_quantity;
    private float unit_price;
    private  String warrantly_duration_months;

    public Product(String description, String product_category_name, String product_image,
                   String product_name, String provider_name, int stock_quantity, float unit_price,
                   String warrantly_duration_months) {
        this.description = description;
        this.product_category_name = product_category_name;
        this.product_image = product_image;
        this.product_name = product_name;
        this.provider_name = provider_name;
        this.stock_quantity = stock_quantity;
        this.unit_price = unit_price;
        this.warrantly_duration_months = warrantly_duration_months;
    }

    public Product() {
    }

    public Product(String id, String description, String product_category_name, String product_image
            , String product_name, String provider_name, int stock_quantity, float unit_price
            , String warrantly_duration_months) {
        this.id = id;
        this.description = description;
        this.product_category_name = product_category_name;
        this.product_image = product_image;
        this.product_name = product_name;
        this.provider_name = provider_name;
        this.stock_quantity = stock_quantity;
        this.unit_price = unit_price;
        this.warrantly_duration_months = warrantly_duration_months;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_category_name() {
        return product_category_name;
    }

    public void setProduct_category_name(String product_category_name) {
        this.product_category_name = product_category_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(float unit_price) {
        this.unit_price = unit_price;
    }

    public String getWarrantly_duration_months() {
        return warrantly_duration_months;
    }

    public void setWarrantly_duration_months(String warrantly_duration_months) {
        this.warrantly_duration_months = warrantly_duration_months;
    }

    @Override
    public String toString() {
        return "Product{" +
                "description='" + description + '\'' +
                ", product_category_name='" + product_category_name + '\'' +
                ", product_image='" + product_image + '\'' +
                ", product_name='" + product_name + '\'' +
                ", provider_name='" + provider_name + '\'' +
                ", stock_quantity=" + stock_quantity +
                ", unit_price=" + unit_price +
                ", warrantly_duration_months='" + warrantly_duration_months + '\'' +
                '}';
    }
}
