package com.example.pizzamaniaadmin;

public class OrderItemModel {
    private String itemName;
    private String size;
    private int quantity;
    private double price;

    public OrderItemModel() {}

    public OrderItemModel(String itemName, String size, int quantity, double price) {
        this.itemName = itemName;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
