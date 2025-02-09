package edu.foobar.models;

public class OrderItem {
    private int id;
    private int orderId;
    private Menu menu;
    private int quantity;

    /**
     * This constructor is meant to be used when getting data from the database, NOT when creating.
     */
    public OrderItem(int id, int orderId, Menu menu, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderItem(int orderId, Menu menu, int quantity) {
        this.orderId = orderId;
        this.menu = menu;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}