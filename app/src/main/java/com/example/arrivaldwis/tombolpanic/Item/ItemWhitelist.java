package com.example.arrivaldwis.tombolpanic.Item;


public class ItemWhitelist {
    private String nama;
    private String notelp;

    public ItemWhitelist(String n, String i) {
        setNama(n);
        setNotelp(i);
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }
}
