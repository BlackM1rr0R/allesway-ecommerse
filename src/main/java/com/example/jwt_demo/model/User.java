package com.example.jwt_demo.model;

import com.example.jwt_demo.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String phonenumber;
    private String adress;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
    
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorites> favorites;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id",referencedColumnName = "id")
    @JsonIgnore
    private Image image;
    public User(Long id, String username, String email, String phonenumber, String adress, String password, Role role, Image image) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.adress = adress;
        this.password = password;
        this.role = role;
        this.image = image;
    }
}
