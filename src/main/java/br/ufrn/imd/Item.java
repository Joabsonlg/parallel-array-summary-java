package br.ufrn.imd;

/**
 * Class that represents an item.
 */
public class Item {
    private int id;
    private float total;
    private byte group;

    /**
     * Constructors
     */
    public Item(int id, float total, byte group) {
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

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public byte getGroup() {
        return group;
    }

    public void setGroup(byte group) {
        this.group = group;
    }
}
