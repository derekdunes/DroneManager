package com.musala.drone.manager.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "medication")
@Data
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    name (allowed only letters, numbers, ‘-‘, ‘_’);
    @Column(name = "name")
    private String name;

    //    weight;
    @Column(name = "weight")
    private int weight;

    //    code (allowed only upper case letters, underscore and numbers);
    @Column(name = "code")
    private String code;

    //    image (picture of the medication case).
    @Column(name = "image")
    private String image;

    //join id
    @OneToOne(mappedBy = "medication", targetEntity = Drone.class, fetch = FetchType.EAGER)
    private Drone drone;

    @JsonBackReference
    public Drone getDrone() {
        return drone;
    }

}
