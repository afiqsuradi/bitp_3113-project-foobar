package edu.foobar.models;

import java.util.List;

public class Order {
    private int id;
    private Enums.OrderStatus status;
    private Membership membership;

    /**
     * Lists of OrderItem correspond to the current order
     * */
    private List<OrderItem> orderItem;

    /**
     * This constructor is meant to be used when getting data from database, NOT when creating new data
     */
    public Order(int id, Enums.OrderStatus status, Membership membership, List<OrderItem> orderItem) {
        this.id = id;
        this.status = status;
        this.membership = membership;
        this.orderItem = orderItem;
    }

    public Order(Membership membership, Enums.OrderStatus status) {
        this.status = status;
        this.membership = membership;
    }

    public int getId() {
        return id;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public Enums.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(Enums.OrderStatus status) {
        this.status = status;
    }
}
