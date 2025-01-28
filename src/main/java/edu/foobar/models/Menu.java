package edu.foobar.models;

public class Menu {
    private int id;
    private String name;
    private double price;
    private Enums.FoodCategory category;

    public Menu(int id, String name, double price, Enums.FoodCategory category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Enums.FoodCategory getCategory() {
        return category;
    }

    public void setCategory(Enums.FoodCategory category) {
        this.category = category;
    }
}