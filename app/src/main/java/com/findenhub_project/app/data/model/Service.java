package com.findenhub_project.app.data.model;

import com.google.firebase.Timestamp;

public class Service {

    private String id;
    private String supplierId;
    private String title;
    private String category;
    private String description;
    private String priceBase;
    private String city;
    private boolean active;
    private Timestamp createdAt;

    public Service() {}

    public Service(String supplierId, String title, String category,
                   String description, String priceBase, String city) {
        this.supplierId  = supplierId;
        this.title       = title;
        this.category    = category;
        this.description = description;
        this.priceBase   = priceBase;
        this.city        = city;
        this.active      = true;
        this.createdAt   = Timestamp.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriceBase() { return priceBase; }
    public void setPriceBase(String priceBase) { this.priceBase = priceBase; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
