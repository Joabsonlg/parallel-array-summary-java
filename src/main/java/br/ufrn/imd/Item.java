package br.ufrn.imd;

/**
 * Class that represents an item.
 */
public class Item {
    private int id;
    private double total;
    private int group;

    /**
     * Constructors
     */
    public Item(int id, double total, int group) {
        this.id = id;
        this.total = total;
        this.group = group;
    }

    /**
     * Getters and Setters
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
