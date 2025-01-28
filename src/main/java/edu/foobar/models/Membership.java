package edu.foobar.models;

public class Membership {
    private int id;
    private String customerEmail;
    private double points;

    /**
     * This constructor is meant to be used when getting data from database, NOT when creating new data
     */
    public Membership(int id, String customerEmail, double points) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.points = points;
    }

    public Membership(String customerEmail, double points) {
        this.customerEmail = customerEmail;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
