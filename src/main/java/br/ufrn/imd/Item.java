package br.ufrn.imd;

/**
 * Classe que representa um item.
 */
public class Item {
    private int id;
    private double total;
    private int grupo;

    /**
     * Constructors
     */
    public Item(int id, double total, int grupo) {
        this.id = id;
        this.total = total;
        this.grupo = grupo;
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

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }
}
