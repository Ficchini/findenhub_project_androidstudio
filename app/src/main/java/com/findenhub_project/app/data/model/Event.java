package com.findenhub_project.app.data.model;

import com.google.firebase.Timestamp;

public class Event {

    private String id;
    private String clientId;
    private String title;
    private String category;
    private String date;
    private String location;
    private String budget;
    private String description;
    private String status;     // ex: OPEN, CLOSED
    private Timestamp createdAt;

    public Event() {}

    public Event(String clientId, String title, String category,
                 String date, String location, String budget, String description) {
        this.clientId    = clientId;
        this.title       = title;
        this.category    = category;
        this.date        = date;
        this.location    = location;
        this.budget      = budget;
        this.description = description;
        this.status      = "OPEN";
        this.createdAt   = Timestamp.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
