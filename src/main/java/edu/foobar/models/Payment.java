package edu.foobar.models;

public class Payment {
    private int id;
    private int orderId;

    /**
     * total is in long for precision, assume total in cent
     */
    private long total;
    private Enums.PaymentStatus status;
    private double point;

    /**
     * This constructor is meant to be used when getting data from database, NOT when creating new data
     */
    public Payment(int id, int orderId, long total, Enums.PaymentStatus status, double point) {
        this.id = id;
        this.orderId = orderId;
        this.total = total;
        this.status = status;
        this.point = point;
    }

    public Payment(int orderId, long total, Enums.PaymentStatus status, double point) {
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
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
