package com.example.firestoreapp.models;

public class Cart {
    private String IDCart;
   private   String productRef;
   private String name;
   private int quantity;
   private float unit_price;


    public Cart() {
    }

    public Cart(String IDCart, String productRef, String name, int quantity, float unit_price) {
        this.IDCart = IDCart;
        this.productRef = productRef;
        this.name = name;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }

    public Cart(String productRef, String name, int quantity, float unit_price) {
        this.productRef = productRef;
        this.name = name;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }


    public String getIDCart() {
        return IDCart;
    }

    public void setIDCart(String IDCart) {
        this.IDCart = IDCart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductRef() {
        return productRef;
    }

    public void setProductRef(String productRef) {
        this.productRef = productRef;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(float unit_price) {
        this.unit_price = unit_price;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "productRef='" + productRef + '\'' +
                ", quantity=" + quantity +
                ", unit_price=" + unit_price +
                '}';
    }
}
