package com.example.th03final;

import java.io.Serializable;
import java.util.UUID;

public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private long amount;
    private String category;
    private String date;
    private String note;

    public Expense(String name, long amount, String category, String date, String note) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    public Expense(String id, String name, long amount, String category, String date, String note) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
