package br.ufrn.imd;

public class ItemDataStore {
    private float[] totals;
    private byte[] groups;

    public ItemDataStore(int size) {
        this.totals = new float[size];
        this.groups = new byte[size];
    }

    public void setItem(int index, float total, byte group) {
        totals[index] = total;
        groups[index] = group;
    }

    public int size() {
        return groups.length;
    }

    public float getTotal(int index) {
        return totals[index];
    }

    public byte getGroup(int index) {
        return groups[index];
    }

    // Outros métodos conforme necessário, como métodos de busca ou atualização
}