package com.musala.drone.manager.model;

import com.musala.drone.manager.enums.Model;
import com.musala.drone.manager.enums.State;
import com.musala.drone.manager.models.Drone;
import com.musala.drone.manager.models.Medication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MedicationTest {

    @Test()
    void MedicationCreationTest(){

        Drone drone = new Drone();
        drone.setId(1L);
        drone.setBatteryCapacity(100);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setSerialNumber("Nimbus_2000");
        drone.setWeight(500);
        drone.setState(State.IDLE);

        Medication medication = new Medication();
        medication.setId(2L);
        medication.setWeight(100);
        medication.setCode("MSX-YZ250");
        medication.setDrone(drone);
        medication.setName("Drug Medicals");
        medication.setImage("https://www.images.com/drone.png");

        assertAll(
                () -> { assertEquals(2L, medication.getId()); },
                () -> { assertEquals(100, medication.getWeight()); },
                () -> { assertEquals("Drug Medicals", medication.getName()); },
                () -> { assertEquals("MSX-YZ250", medication.getCode()); },
                () -> { assertEquals("https://www.images.com/drone.png", medication.getImage());},

                () -> { assertEquals(1L, medication.getDrone().getId()); },
                () -> { assertEquals(100, medication.getDrone().getBatteryCapacity()); },
                () -> { assertEquals(Model.HEAVYWEIGHT, medication.getDrone().getModel()); },
                () -> { assertEquals("Nimbus_2000", medication.getDrone().getSerialNumber()); },
                () -> { assertEquals(500, medication.getDrone().getWeight()); },
                () -> { assertEquals(State.IDLE, medication.getDrone().getState());},
                () -> { assertEquals(null, medication.getDrone().getMedication());}
        );
    }
}
