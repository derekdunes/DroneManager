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
public class DroneTest {

    @Test()
    void DroneCreationTest(){
        Medication medication = null;
        Drone drone = new Drone();
        drone.setId(1L);
        drone.setBatteryCapacity(100);
        drone.setModel(Model.HEAVYWEIGHT);
        drone.setSerialNumber("Nimbus_2000");
        drone.setWeight(500);
        drone.setState(State.IDLE);
        drone.setMedication(medication);

        assertAll(
                () -> { assertEquals(1L, drone.getId()); },
                () -> { assertEquals(100, drone.getBatteryCapacity()); },
                () -> { assertEquals(Model.HEAVYWEIGHT, drone.getModel()); },
                () -> { assertEquals("Nimbus_2000", drone.getSerialNumber()); },
                () -> { assertEquals(500, drone.getWeight()); },
                () -> { assertEquals(State.IDLE, drone.getState());},
                () -> { assertEquals(null, drone.getMedication());}
        );
    }

}
