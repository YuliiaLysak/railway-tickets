package edu.lysak.railwaytickets.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "stations")
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String name;
}
