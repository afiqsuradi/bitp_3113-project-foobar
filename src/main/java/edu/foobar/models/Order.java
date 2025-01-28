package edu.foobar.models;

public class Order {
    private int id;
    private int membershipId;
    private Enums.OrderStatus status;

    public Order(int id, int membershipId, Enums.OrderStatus status) {
        this.id = id;
        this.membershipId = membershipId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public Enums.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(Enums.OrderStatus status) {
        this.status = status;
    }
}
