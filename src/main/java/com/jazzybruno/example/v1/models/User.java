package com.jazzybruno.example.v1.models;


import com.jazzybruno.example.v1.enums.Gender;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;
    @NotNull
    private String email;

    @NotNull
    private String username;

    @NotNull
    private String national_id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    @Column
    private Date lastLogin;

    @Column
    @NotNull
    private Gender gender;

    @Column
    @NotNull
    private String profilePicture;
    @NotNull
    private String password;

    public User(Long user_id, String email, String username, String national_id, Role role, Date lastLogin, String profilePicture, String password) {
        this.user_id = user_id;
        this.email = email;
        this.username = username;
        this.national_id = national_id;
        this.role = role;
        this.lastLogin = lastLogin;

        this.profilePicture = profilePicture;
        this.password = password;
    }

    public User(String email, String username, String national_id, Gender gender,  String password) {
        this.email = email;
        this.username = username;
        this.national_id = national_id;
        this.gender = gender;
        this.password = password;
    }
}

  