package com.findenhub_project.app.data.model;

import com.google.firebase.Timestamp;

public class Request {

    private String id;
    private String eventId;
    private String clientId;
    private String supplierId;
    private String serviceId;
    private String message;
    private String status;     // PENDING, ACCEPTED, REJECTED
    private Timestamp createdAt;

    // Campos não persistidos — preenchidos na UI via joins manuais
    private String clientName;
    private String eventTitle;

    public Request() {}

    public Request(String eventId, String clientId, String supplierId,
                   String serviceId, String message) {
        this.eventId    = eventId;
        this.clientId   = clientId;
        this.supplierId = supplierId;
        this.serviceId  = serviceId;
        this.message    = message;
        this.status     = "PENDING";
        this.createdAt  = Timestamp.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
}
