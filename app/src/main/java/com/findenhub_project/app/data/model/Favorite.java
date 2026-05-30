package com.findenhub_project.app.data.model;

import com.google.firebase.Timestamp;

public class Favorite {

    private String id;
    private String clientId;
    private String supplierId;
    private Timestamp createdAt;

    public Favorite() {}

    public Favorite(String clientId, String supplierId) {
        this.clientId   = clientId;
        this.supplierId = supplierId;
        this.createdAt  = Timestamp.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
