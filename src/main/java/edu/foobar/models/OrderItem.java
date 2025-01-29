package edu.foobar.models;

public class OrderItem {
    private int id;
    private int orderId;
    private Menu menu;

    /**
     * This constructor is meant to be used when getting data from database, NOT when creating new data
     */
    public OrderItem(int id, int orderId, Menu menu) {
        this.id = id;
        this.orderId = orderId;
        this.menu = menu;
    }

    public OrderItem(int orderId, Menu menu) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
