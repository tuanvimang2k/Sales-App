package com.example.firestoreapp.models;

public class Order {

    private String name;
    private int quantity;
    private double price;
    private String imageResource;
    private String idCustomer;
    private String productRef;
    public Order() {
    }

    public Order(String name, int quantity, double price, String imageResource) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.imageResource = imageResource;
    }

    public Order(String name, int quantity, double price, String imageResource, String idCustomer) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.imageResource = imageResource;
        this.idCustomer = idCustomer;
    }

    public Order(String name, int quantity, double price, String imageResource, String idCustomer, String productRef) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.imageResource = imageResource;
        this.idCustomer = idCustomer;
        this.productRef = productRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getProductRef() {
        return productRef;
    }

    public void setProductRef(String productRef) {
        this.productRef = productRef;
    }

    @Override
    public String toString() {
        return "Order{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", imageResource='" + imageResource + '\'' +
                '}';
    }
}
