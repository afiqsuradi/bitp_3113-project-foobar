package edu.foobar.models;

public class OrderItem {
    private int id;
    private int orderId;
    private int menuId;

    public OrderItem(int orderId, int menuId) {
        this.orderId = orderId;
        this.menuId = menuId;
    }

    public int getId() {
        return id;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
