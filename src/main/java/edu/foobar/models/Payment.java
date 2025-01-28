package edu.foobar.models;

public class Payment {
    private int id;
    private int orderId;
    private double total;
    private Enums.PaymentStatus status;
    private double point;

    public Payment(int orderId, double total, Enums.PaymentStatus status, double point) {
        this.orderId = orderId;
        this.total = total;
        this.status = status;
        this.point = point;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Enums.PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(Enums.PaymentStatus status) {
        this.status = status;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }
}
