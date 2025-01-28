package edu.foobar.models;

public class Menu {
    private int id;
    private String name;

    /**
     * price is in long for precision, assume price in cent
     */
    private long price;
    private Enums.FoodCategory category;

    /**
     * This constructor is meant to be used when getting data from database, NOT when creating new data
     */
    public Menu(int id, String name, long price, Enums.FoodCategory category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    /**
     * This constructor is meant to be used when create data to store into database
     */
    public Menu(String name, long price, Enums.FoodCategory category) {
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Enums.FoodCategory getCategory() {
        return category;
    }

    public void setCategory(Enums.FoodCategory category) {
        this.category = category;
    }
}