package com.example.firestoreapp.models;

public class Cart {
   private   String productRef;
   private int quantity;
   private float unit_price;

    public Cart() {
    }

    public Cart(String productRef, int quantity, float unit_price) {
        this.productRef = productRef;
        this.quantity = quantity;
        this.unit_price = unit_price;
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
