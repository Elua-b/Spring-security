package com.eloi.example.v1.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "car")
//@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long carId;
    @NotNull
    public String carName;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")

    public User user;
    @NotNull
    public Double price ;
    @NotNull
    private int year;

    public Car(String carName, Double price, int year) {
        this.carName = carName;
        this.price = price;
        this.year = year;
    }
}
