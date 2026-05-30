package com.findenhub_project.app.data.model;

import com.google.firebase.Timestamp;

public class User {

    private String id;
    private String name;
    private String email;
    private String cpf;
    private String userType;   // CLIENT ou SUPPLIER
    private String photoUrl;
    private String phone;
    private String category;   // usado pelo SUPPLIER
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Construtor vazio obrigatório para o Firestore desserializar
    public User() {}

    public User(String id, String name, String email, String cpf, String userType) {
        this.id       = id;
        this.name     = name;
        this.email    = email;
        this.cpf      = cpf;
        this.userType = userType;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
