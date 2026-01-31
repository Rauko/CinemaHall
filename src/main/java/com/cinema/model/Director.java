package com.cinema.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "directors")
@Data
public class Director {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
}
