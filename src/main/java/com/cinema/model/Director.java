package com.cinema.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "directors")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = false)
public class Director {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
}
