package com.musala.drone.manager.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.musala.drone.manager.enums.Model;
import com.musala.drone.manager.enums.State;
import lombok.Data;
import lombok.Generated;

import javax.persistence.*;

@Data
@Entity
@Table(name = "drone")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    xserial number (100 characters max);
    @Column(name = "serial_number", length = 100, unique = true)
    private String serialNumber;

    //    model (LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT, HEAVYWEIGHT);
    @Column(name = "model")
    private Model model;

    //    weight limit (500gr max);
    @Column(name = "weight")
    private int weight;

    //    battery capacity (percentage);
    @Column(name = "battery_capacity")
    private int batteryCapacity;

    //    state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).
    @Column(name = "state")
    private State state;

    //Join id
    @OneToOne(targetEntity = Medication.class, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "medication_id", referencedColumnName = "id")
    Medication medication;

    @JsonManagedReference
    public Medication getMedication() {
        return medication;
    }
}
