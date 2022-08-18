package com.musala.drone.manager.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musala.drone.manager.dto.DroneDto;
import com.musala.drone.manager.dto.MedicationDto;
import com.musala.drone.manager.dto.Response;
import com.musala.drone.manager.enums.Model;
import com.musala.drone.manager.enums.State;
import com.musala.drone.manager.models.Drone;
import com.musala.drone.manager.models.Medication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {Utils.class})
@ExtendWith(SpringExtension.class)
class UtilsTest {
    @Autowired
    private Utils utils;

    /**
     * Method under test: {@link Utils#getModelFromString(String)}
     */
    @Test
    void testGetModelFromString() {
        assertEquals(Model.INVALID, this.utils.getModelFromString("Model"));
        assertEquals(Model.LIGHTWEIGHT, this.utils.getModelFromString("LIGHTWEIGHT"));
        assertEquals(Model.MIDDLEWEIGHT, this.utils.getModelFromString("MIDDLEWEIGHT"));
        assertEquals(Model.CRUISERWEIGHT, this.utils.getModelFromString("CRUISERWEIGHT"));
        assertEquals(Model.HEAVYWEIGHT, this.utils.getModelFromString("HEAVYWEIGHT"));
    }

    /**
     * Method under test: {@link Utils#getStringFromModel(Model)}
     */
    @Test
    void testGetStringFromModel() {
        assertEquals("LIGHTWEIGHT", this.utils.getStringFromModel(Model.LIGHTWEIGHT));
        assertEquals("MIDDLEWEIGHT", this.utils.getStringFromModel(Model.MIDDLEWEIGHT));
        assertEquals("CRUISERWEIGHT", this.utils.getStringFromModel(Model.CRUISERWEIGHT));
        assertEquals("HEAVYWEIGHT", this.utils.getStringFromModel(Model.HEAVYWEIGHT));
        assertEquals("Invalid", this.utils.getStringFromModel(Model.INVALID));
    }

    /**
     * Method under test: {@link Utils#getStateAsString(State)}
     */
    @Test
    void testGetStateAsString() {
        assertEquals("IDLE", this.utils.getStateAsString(State.IDLE));
        assertEquals("LOADING", this.utils.getStateAsString(State.LOADING));
        assertEquals("LOADED", this.utils.getStateAsString(State.LOADED));
        assertEquals("DELIVERING", this.utils.getStateAsString(State.DELIVERING));
        assertEquals("DELIVERED", this.utils.getStateAsString(State.DELIVERED));
        assertEquals("RETURNING", this.utils.getStateAsString(State.RETURNING));
        assertEquals("INVALID", this.utils.getStateAsString(State.INVALID));
    }

    /**
     * Method under test: {@link Utils#getStateFromString(String)}
     */
    @Test
    void testGetStateFromString() {
        assertEquals(State.INVALID, this.utils.getStateFromString("MD"));
        assertEquals(State.IDLE, this.utils.getStateFromString("IDLE"));
        assertEquals(State.LOADING, this.utils.getStateFromString("LOADING"));
        assertEquals(State.LOADED, this.utils.getStateFromString("LOADED"));
        assertEquals(State.DELIVERING, this.utils.getStateFromString("DELIVERING"));
        assertEquals(State.DELIVERED, this.utils.getStateFromString("DELIVERED"));
        assertEquals(State.RETURNING, this.utils.getStateFromString("RETURNING"));
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);

        DroneDto droneDto = new DroneDto();
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals(
                "The model is invalid, please provide the supported model types (LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT,"
                        + " HEAVYWEIGHT)",
                actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone2() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("42");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("Model");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals(
                "The model is invalid, please provide the supported model types (LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT,"
                        + " HEAVYWEIGHT)",
                actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone3() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(500);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("42");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("Model");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The Drone battery capacity is greater than Max(100) supported value, please provide a valid value",
                actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone4() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(0);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("42");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("Model");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The Drone battery capacity is less than or equal to Zero(0), please provide a valid value",
                actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone5() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(0);
        when(droneDto.getSerialNumber()).thenReturn("42");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("Model");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The Drone Weight is less than or equal to Zero(0), please provide a valid Weight",
                actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone6() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("9");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("Model");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals(
                "The model is invalid, please provide the supported model types (LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT,"
                        + " HEAVYWEIGHT)",
                actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone7() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("^[0-9]+");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("Model");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The Serial Number is invalid, please provide a number only serial number",
                actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone8() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("1");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("Model");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals(
                "The model is invalid, please provide the supported model types (LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT,"
                        + " HEAVYWEIGHT)",
                actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone9() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn(
                "The model is invalid, please provide the supported model types (LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT,"
                        + " HEAVYWEIGHT)");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("Model");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The maximum length of a serial number is 100 characters. Please provide a valid numbers",
                actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone10() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("Model");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The Drone Serial Number  is empty, please provide a valid serial number",
                actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone11() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("42");
        when(droneDto.getState()).thenReturn("");
        when(droneDto.getModel()).thenReturn("Model");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The Drone Code is empty, please provide a valid Code", actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone12() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("42");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("LIGHTWEIGHT");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The state is invalid, please provide the supported state types (IDLE, LOADING, LOADED, DELIVERING,"
                + " DELIVERED, RETURNING)", actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone13() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("42");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("MIDDLEWEIGHT");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The state is invalid, please provide the supported state types (IDLE, LOADING, LOADED, DELIVERING,"
                + " DELIVERED, RETURNING)", actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone14() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("42");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("CRUISERWEIGHT");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The state is invalid, please provide the supported state types (IDLE, LOADING, LOADED, DELIVERING,"
                + " DELIVERED, RETURNING)", actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone15() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("42");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("HEAVYWEIGHT");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The state is invalid, please provide the supported state types (IDLE, LOADING, LOADED, DELIVERING,"
                + " DELIVERED, RETURNING)", actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateDrone(DroneDto)}
     */
    @Test
    void testValidateDrone16() {
        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(new Drone());
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(medication);
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication1 = new Medication();
        medication1.setCode("Code");
        medication1.setDrone(drone);
        medication1.setId(123L);
        medication1.setImage("Image");
        medication1.setName("Name");
        medication1.setWeight(3);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getBatteryCapacity()).thenReturn(1);
        when(droneDto.getWeight()).thenReturn(3);
        when(droneDto.getSerialNumber()).thenReturn("42");
        when(droneDto.getState()).thenReturn("MD");
        when(droneDto.getModel()).thenReturn("");
        doNothing().when(droneDto).setBatteryCapacity(anyInt());
        doNothing().when(droneDto).setId((Long) any());
        doNothing().when(droneDto).setMedication((Medication) any());
        doNothing().when(droneDto).setModel((String) any());
        doNothing().when(droneDto).setSerialNumber((String) any());
        doNothing().when(droneDto).setState((String) any());
        doNothing().when(droneDto).setWeight(anyInt());
        droneDto.setBatteryCapacity(1);
        droneDto.setId(123L);
        droneDto.setMedication(medication1);
        droneDto.setModel("Model");
        droneDto.setSerialNumber("42");
        droneDto.setState("MD");
        droneDto.setWeight(3);
        Response actualValidateDroneResult = this.utils.validateDrone(droneDto);
        assertEquals("The Drone Model Name is empty, please provide a valid name",
                actualValidateDroneResult.getStatusMessage());
        assertEquals("1", actualValidateDroneResult.getStatusCode());
        verify(droneDto).getBatteryCapacity();
        verify(droneDto).getWeight();
        verify(droneDto).getModel();
        verify(droneDto).getSerialNumber();
        verify(droneDto).getState();
        verify(droneDto).setBatteryCapacity(anyInt());
        verify(droneDto).setId((Long) any());
        verify(droneDto).setMedication((Medication) any());
        verify(droneDto).setModel((String) any());
        verify(droneDto).setSerialNumber((String) any());
        verify(droneDto).setState((String) any());
        verify(droneDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateMedication(MedicationDto)}
     */
    @Test
    void testValidateMedication() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(new Medication());
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(drone);
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setId(123L);
        drone1.setMedication(medication);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setSerialNumber("42");
        drone1.setState(State.IDLE);
        drone1.setWeight(3);

        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setCode("Code");
        medicationDto.setDrone(drone1);
        medicationDto.setId(123L);
        medicationDto.setImage("Image");
        medicationDto.setName("Name");
        medicationDto.setWeight(3);
        Response actualValidateMedicationResult = this.utils.validateMedication(medicationDto);
        assertEquals("The Code of Medication should only contain Uppercase letters, numbers and hyphen(-), Kindly provide a"
                + " valid code", actualValidateMedicationResult.getStatusMessage());
        assertEquals("1", actualValidateMedicationResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateMedication(MedicationDto)}
     */
    @Test
    void testValidateMedication2() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(new Medication());
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(drone);
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setId(123L);
        drone1.setMedication(medication);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setSerialNumber("42");
        drone1.setState(State.IDLE);
        drone1.setWeight(3);
        MedicationDto medicationDto = mock(MedicationDto.class);
        when(medicationDto.getCode()).thenReturn("Code");
        when(medicationDto.getWeight()).thenReturn(3);
        when(medicationDto.getImage()).thenReturn("Image");
        when(medicationDto.getName()).thenReturn("Name");
        doNothing().when(medicationDto).setCode((String) any());
        doNothing().when(medicationDto).setDrone((Drone) any());
        doNothing().when(medicationDto).setId((Long) any());
        doNothing().when(medicationDto).setImage((String) any());
        doNothing().when(medicationDto).setName((String) any());
        doNothing().when(medicationDto).setWeight(anyInt());
        medicationDto.setCode("Code");
        medicationDto.setDrone(drone1);
        medicationDto.setId(123L);
        medicationDto.setImage("Image");
        medicationDto.setName("Name");
        medicationDto.setWeight(3);
        Response actualValidateMedicationResult = this.utils.validateMedication(medicationDto);
        assertEquals("The Code of Medication should only contain Uppercase letters, numbers and hyphen(-), Kindly provide a"
                + " valid code", actualValidateMedicationResult.getStatusMessage());
        assertEquals("1", actualValidateMedicationResult.getStatusCode());
        verify(medicationDto).getWeight();
        verify(medicationDto).getCode();
        verify(medicationDto).getImage();
        verify(medicationDto).getName();
        verify(medicationDto).setCode((String) any());
        verify(medicationDto).setDrone((Drone) any());
        verify(medicationDto).setId((Long) any());
        verify(medicationDto).setImage((String) any());
        verify(medicationDto).setName((String) any());
        verify(medicationDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateMedication(MedicationDto)}
     */
    @Test
    void testValidateMedication3() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(new Medication());
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(drone);
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setId(123L);
        drone1.setMedication(medication);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setSerialNumber("42");
        drone1.setState(State.IDLE);
        drone1.setWeight(3);
        MedicationDto medicationDto = mock(MedicationDto.class);
        when(medicationDto.getCode()).thenReturn("U");
        when(medicationDto.getWeight()).thenReturn(3);
        when(medicationDto.getImage()).thenReturn("Image");
        when(medicationDto.getName()).thenReturn("Name");
        doNothing().when(medicationDto).setCode((String) any());
        doNothing().when(medicationDto).setDrone((Drone) any());
        doNothing().when(medicationDto).setId((Long) any());
        doNothing().when(medicationDto).setImage((String) any());
        doNothing().when(medicationDto).setName((String) any());
        doNothing().when(medicationDto).setWeight(anyInt());
        medicationDto.setCode("Code");
        medicationDto.setDrone(drone1);
        medicationDto.setId(123L);
        medicationDto.setImage("Image");
        medicationDto.setName("Name");
        medicationDto.setWeight(3);
        Response actualValidateMedicationResult = this.utils.validateMedication(medicationDto);
        assertEquals("The image link should start with (http://) or (https://), Kindly provide a valid image link",
                actualValidateMedicationResult.getStatusMessage());
        assertEquals("1", actualValidateMedicationResult.getStatusCode());
        verify(medicationDto).getWeight();
        verify(medicationDto).getCode();
        verify(medicationDto).getImage();
        verify(medicationDto).getName();
        verify(medicationDto).setCode((String) any());
        verify(medicationDto).setDrone((Drone) any());
        verify(medicationDto).setId((Long) any());
        verify(medicationDto).setImage((String) any());
        verify(medicationDto).setName((String) any());
        verify(medicationDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateMedication(MedicationDto)}
     */
    @Test
    void testValidateMedication4() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(new Medication());
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(drone);
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setId(123L);
        drone1.setMedication(medication);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setSerialNumber("42");
        drone1.setState(State.IDLE);
        drone1.setWeight(3);
        MedicationDto medicationDto = mock(MedicationDto.class);
        when(medicationDto.getCode()).thenReturn("1");
        when(medicationDto.getWeight()).thenReturn(3);
        when(medicationDto.getImage()).thenReturn("Image");
        when(medicationDto.getName()).thenReturn("Name");
        doNothing().when(medicationDto).setCode((String) any());
        doNothing().when(medicationDto).setDrone((Drone) any());
        doNothing().when(medicationDto).setId((Long) any());
        doNothing().when(medicationDto).setImage((String) any());
        doNothing().when(medicationDto).setName((String) any());
        doNothing().when(medicationDto).setWeight(anyInt());
        medicationDto.setCode("Code");
        medicationDto.setDrone(drone1);
        medicationDto.setId(123L);
        medicationDto.setImage("Image");
        medicationDto.setName("Name");
        medicationDto.setWeight(3);
        Response actualValidateMedicationResult = this.utils.validateMedication(medicationDto);
        assertEquals("The image link should start with (http://) or (https://), Kindly provide a valid image link",
                actualValidateMedicationResult.getStatusMessage());
        assertEquals("1", actualValidateMedicationResult.getStatusCode());
        verify(medicationDto).getWeight();
        verify(medicationDto).getCode();
        verify(medicationDto).getImage();
        verify(medicationDto).getName();
        verify(medicationDto).setCode((String) any());
        verify(medicationDto).setDrone((Drone) any());
        verify(medicationDto).setId((Long) any());
        verify(medicationDto).setImage((String) any());
        verify(medicationDto).setName((String) any());
        verify(medicationDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateMedication(MedicationDto)}
     */
    @Test
    void testValidateMedication5() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(new Medication());
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(drone);
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setId(123L);
        drone1.setMedication(medication);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setSerialNumber("42");
        drone1.setState(State.IDLE);
        drone1.setWeight(3);
        MedicationDto medicationDto = mock(MedicationDto.class);
        when(medicationDto.getCode()).thenReturn("42");
        when(medicationDto.getWeight()).thenReturn(3);
        when(medicationDto.getImage()).thenReturn("Image");
        when(medicationDto.getName()).thenReturn("Name");
        doNothing().when(medicationDto).setCode((String) any());
        doNothing().when(medicationDto).setDrone((Drone) any());
        doNothing().when(medicationDto).setId((Long) any());
        doNothing().when(medicationDto).setImage((String) any());
        doNothing().when(medicationDto).setName((String) any());
        doNothing().when(medicationDto).setWeight(anyInt());
        medicationDto.setCode("Code");
        medicationDto.setDrone(drone1);
        medicationDto.setId(123L);
        medicationDto.setImage("Image");
        medicationDto.setName("Name");
        medicationDto.setWeight(3);
        Response actualValidateMedicationResult = this.utils.validateMedication(medicationDto);
        assertEquals("The image link should start with (http://) or (https://), Kindly provide a valid image link",
                actualValidateMedicationResult.getStatusMessage());
        assertEquals("1", actualValidateMedicationResult.getStatusCode());
        verify(medicationDto).getWeight();
        verify(medicationDto).getCode();
        verify(medicationDto).getImage();
        verify(medicationDto).getName();
        verify(medicationDto).setCode((String) any());
        verify(medicationDto).setDrone((Drone) any());
        verify(medicationDto).setId((Long) any());
        verify(medicationDto).setImage((String) any());
        verify(medicationDto).setName((String) any());
        verify(medicationDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateMedication(MedicationDto)}
     */
    @Test
    void testValidateMedication6() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(new Medication());
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(drone);
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setId(123L);
        drone1.setMedication(medication);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setSerialNumber("42");
        drone1.setState(State.IDLE);
        drone1.setWeight(3);
        MedicationDto medicationDto = mock(MedicationDto.class);
        when(medicationDto.getCode()).thenReturn("");
        when(medicationDto.getWeight()).thenReturn(3);
        when(medicationDto.getImage()).thenReturn("Image");
        when(medicationDto.getName()).thenReturn("Name");
        doNothing().when(medicationDto).setCode((String) any());
        doNothing().when(medicationDto).setDrone((Drone) any());
        doNothing().when(medicationDto).setId((Long) any());
        doNothing().when(medicationDto).setImage((String) any());
        doNothing().when(medicationDto).setName((String) any());
        doNothing().when(medicationDto).setWeight(anyInt());
        medicationDto.setCode("Code");
        medicationDto.setDrone(drone1);
        medicationDto.setId(123L);
        medicationDto.setImage("Image");
        medicationDto.setName("Name");
        medicationDto.setWeight(3);
        Response actualValidateMedicationResult = this.utils.validateMedication(medicationDto);
        assertEquals("The Medication Code provided is empty, please input a valid Code",
                actualValidateMedicationResult.getStatusMessage());
        assertEquals("1", actualValidateMedicationResult.getStatusCode());
        verify(medicationDto).getWeight();
        verify(medicationDto).getCode();
        verify(medicationDto).getImage();
        verify(medicationDto).getName();
        verify(medicationDto).setCode((String) any());
        verify(medicationDto).setDrone((Drone) any());
        verify(medicationDto).setId((Long) any());
        verify(medicationDto).setImage((String) any());
        verify(medicationDto).setName((String) any());
        verify(medicationDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateMedication(MedicationDto)}
     */
    @Test
    void testValidateMedication7() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(new Medication());
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(drone);
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setId(123L);
        drone1.setMedication(medication);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setSerialNumber("42");
        drone1.setState(State.IDLE);
        drone1.setWeight(3);
        MedicationDto medicationDto = mock(MedicationDto.class);
        when(medicationDto.getCode()).thenReturn("Code");
        when(medicationDto.getWeight()).thenReturn(0);
        when(medicationDto.getImage()).thenReturn("Image");
        when(medicationDto.getName()).thenReturn("Name");
        doNothing().when(medicationDto).setCode((String) any());
        doNothing().when(medicationDto).setDrone((Drone) any());
        doNothing().when(medicationDto).setId((Long) any());
        doNothing().when(medicationDto).setImage((String) any());
        doNothing().when(medicationDto).setName((String) any());
        doNothing().when(medicationDto).setWeight(anyInt());
        medicationDto.setCode("Code");
        medicationDto.setDrone(drone1);
        medicationDto.setId(123L);
        medicationDto.setImage("Image");
        medicationDto.setName("Name");
        medicationDto.setWeight(3);
        Response actualValidateMedicationResult = this.utils.validateMedication(medicationDto);
        assertEquals("The Medication Weight provided is less than or equal to Zero(0), please input a valid Weight",
                actualValidateMedicationResult.getStatusMessage());
        assertEquals("1", actualValidateMedicationResult.getStatusCode());
        verify(medicationDto).getWeight();
        verify(medicationDto).getCode();
        verify(medicationDto).getImage();
        verify(medicationDto).getName();
        verify(medicationDto).setCode((String) any());
        verify(medicationDto).setDrone((Drone) any());
        verify(medicationDto).setId((Long) any());
        verify(medicationDto).setImage((String) any());
        verify(medicationDto).setName((String) any());
        verify(medicationDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateMedication(MedicationDto)}
     */
    @Test
    void testValidateMedication8() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(new Medication());
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(drone);
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setId(123L);
        drone1.setMedication(medication);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setSerialNumber("42");
        drone1.setState(State.IDLE);
        drone1.setWeight(3);
        MedicationDto medicationDto = mock(MedicationDto.class);
        when(medicationDto.getCode()).thenReturn("Code");
        when(medicationDto.getWeight()).thenReturn(3);
        when(medicationDto.getImage()).thenReturn("");
        when(medicationDto.getName()).thenReturn("Name");
        doNothing().when(medicationDto).setCode((String) any());
        doNothing().when(medicationDto).setDrone((Drone) any());
        doNothing().when(medicationDto).setId((Long) any());
        doNothing().when(medicationDto).setImage((String) any());
        doNothing().when(medicationDto).setName((String) any());
        doNothing().when(medicationDto).setWeight(anyInt());
        medicationDto.setCode("Code");
        medicationDto.setDrone(drone1);
        medicationDto.setId(123L);
        medicationDto.setImage("Image");
        medicationDto.setName("Name");
        medicationDto.setWeight(3);
        Response actualValidateMedicationResult = this.utils.validateMedication(medicationDto);
        assertEquals("The Image link provided is empty, please input a valid Image Link",
                actualValidateMedicationResult.getStatusMessage());
        assertEquals("1", actualValidateMedicationResult.getStatusCode());
        verify(medicationDto).getWeight();
        verify(medicationDto).getCode();
        verify(medicationDto).getImage();
        verify(medicationDto).getName();
        verify(medicationDto).setCode((String) any());
        verify(medicationDto).setDrone((Drone) any());
        verify(medicationDto).setId((Long) any());
        verify(medicationDto).setImage((String) any());
        verify(medicationDto).setName((String) any());
        verify(medicationDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateMedication(MedicationDto)}
     */
    @Test
    void testValidateMedication9() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(new Medication());
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(drone);
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setId(123L);
        drone1.setMedication(medication);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setSerialNumber("42");
        drone1.setState(State.IDLE);
        drone1.setWeight(3);
        MedicationDto medicationDto = mock(MedicationDto.class);
        when(medicationDto.getCode()).thenReturn("Code");
        when(medicationDto.getWeight()).thenReturn(3);
        when(medicationDto.getImage()).thenReturn("Image");
        when(medicationDto.getName()).thenReturn("^[a-zA-Z0-9_-]*$");
        doNothing().when(medicationDto).setCode((String) any());
        doNothing().when(medicationDto).setDrone((Drone) any());
        doNothing().when(medicationDto).setId((Long) any());
        doNothing().when(medicationDto).setImage((String) any());
        doNothing().when(medicationDto).setName((String) any());
        doNothing().when(medicationDto).setWeight(anyInt());
        medicationDto.setCode("Code");
        medicationDto.setDrone(drone1);
        medicationDto.setId(123L);
        medicationDto.setImage("Image");
        medicationDto.setName("Name");
        medicationDto.setWeight(3);
        Response actualValidateMedicationResult = this.utils.validateMedication(medicationDto);
        assertEquals("The Name of Medication should only contain letters, numbers, hyphen(-) and  underscore(_), Kindly"
                + " Provide a valid name", actualValidateMedicationResult.getStatusMessage());
        assertEquals("1", actualValidateMedicationResult.getStatusCode());
        verify(medicationDto).getWeight();
        verify(medicationDto).getCode();
        verify(medicationDto).getImage();
        verify(medicationDto).getName();
        verify(medicationDto).setCode((String) any());
        verify(medicationDto).setDrone((Drone) any());
        verify(medicationDto).setId((Long) any());
        verify(medicationDto).setImage((String) any());
        verify(medicationDto).setName((String) any());
        verify(medicationDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateMedication(MedicationDto)}
     */
    @Test
    void testValidateMedication10() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(new Medication());
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(drone);
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setId(123L);
        drone1.setMedication(medication);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setSerialNumber("42");
        drone1.setState(State.IDLE);
        drone1.setWeight(3);
        MedicationDto medicationDto = mock(MedicationDto.class);
        when(medicationDto.getCode()).thenReturn("Code");
        when(medicationDto.getWeight()).thenReturn(3);
        when(medicationDto.getImage()).thenReturn("Image");
        when(medicationDto.getName()).thenReturn("foo");
        doNothing().when(medicationDto).setCode((String) any());
        doNothing().when(medicationDto).setDrone((Drone) any());
        doNothing().when(medicationDto).setId((Long) any());
        doNothing().when(medicationDto).setImage((String) any());
        doNothing().when(medicationDto).setName((String) any());
        doNothing().when(medicationDto).setWeight(anyInt());
        medicationDto.setCode("Code");
        medicationDto.setDrone(drone1);
        medicationDto.setId(123L);
        medicationDto.setImage("Image");
        medicationDto.setName("Name");
        medicationDto.setWeight(3);
        Response actualValidateMedicationResult = this.utils.validateMedication(medicationDto);
        assertEquals("The Code of Medication should only contain Uppercase letters, numbers and hyphen(-), Kindly provide a"
                + " valid code", actualValidateMedicationResult.getStatusMessage());
        assertEquals("1", actualValidateMedicationResult.getStatusCode());
        verify(medicationDto).getWeight();
        verify(medicationDto).getCode();
        verify(medicationDto).getImage();
        verify(medicationDto).getName();
        verify(medicationDto).setCode((String) any());
        verify(medicationDto).setDrone((Drone) any());
        verify(medicationDto).setId((Long) any());
        verify(medicationDto).setImage((String) any());
        verify(medicationDto).setName((String) any());
        verify(medicationDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateMedication(MedicationDto)}
     */
    @Test
    void testValidateMedication11() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(1);
        drone.setId(123L);
        drone.setMedication(new Medication());
        drone.setModel(Model.LIGHTWEIGHT);
        drone.setSerialNumber("42");
        drone.setState(State.IDLE);
        drone.setWeight(3);

        Medication medication = new Medication();
        medication.setCode("Code");
        medication.setDrone(drone);
        medication.setId(123L);
        medication.setImage("Image");
        medication.setName("Name");
        medication.setWeight(3);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setId(123L);
        drone1.setMedication(medication);
        drone1.setModel(Model.LIGHTWEIGHT);
        drone1.setSerialNumber("42");
        drone1.setState(State.IDLE);
        drone1.setWeight(3);
        MedicationDto medicationDto = mock(MedicationDto.class);
        when(medicationDto.getCode()).thenReturn("Code");
        when(medicationDto.getWeight()).thenReturn(3);
        when(medicationDto.getImage()).thenReturn("Image");
        when(medicationDto.getName()).thenReturn("");
        doNothing().when(medicationDto).setCode((String) any());
        doNothing().when(medicationDto).setDrone((Drone) any());
        doNothing().when(medicationDto).setId((Long) any());
        doNothing().when(medicationDto).setImage((String) any());
        doNothing().when(medicationDto).setName((String) any());
        doNothing().when(medicationDto).setWeight(anyInt());
        medicationDto.setCode("Code");
        medicationDto.setDrone(drone1);
        medicationDto.setId(123L);
        medicationDto.setImage("Image");
        medicationDto.setName("Name");
        medicationDto.setWeight(3);
        Response actualValidateMedicationResult = this.utils.validateMedication(medicationDto);
        assertEquals("The Medication Name provided is empty, please input a valid name",
                actualValidateMedicationResult.getStatusMessage());
        assertEquals("1", actualValidateMedicationResult.getStatusCode());
        verify(medicationDto).getWeight();
        verify(medicationDto).getCode();
        verify(medicationDto).getImage();
        verify(medicationDto).getName();
        verify(medicationDto).setCode((String) any());
        verify(medicationDto).setDrone((Drone) any());
        verify(medicationDto).setId((Long) any());
        verify(medicationDto).setImage((String) any());
        verify(medicationDto).setName((String) any());
        verify(medicationDto).setWeight(anyInt());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds() {
        Response actualValidateIdsResult = this.utils.validateIds("42");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds2() {
        Response actualValidateIdsResult = this.utils.validateIds("9");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds3() {
        Response actualValidateIdsResult = this.utils.validateIds("^[0-9]+");
        assertEquals("The Id is not a valid number, please provide a valid Id", actualValidateIdsResult.getStatusMessage());
        assertEquals("1", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds4() {
        Response actualValidateIdsResult = this.utils.validateIds("0");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds5() {
        Response actualValidateIdsResult = this.utils.validateIds("");
        assertEquals("The Id is empty, please provide a valid Id", actualValidateIdsResult.getStatusMessage());
        assertEquals("1", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds6() {
        Response actualValidateIdsResult = this.utils.validateIds("1");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds7() {
        Response actualValidateIdsResult = this.utils.validateIds("4242");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds8() {
        Response actualValidateIdsResult = this.utils.validateIds("429");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds9() {
        Response actualValidateIdsResult = this.utils.validateIds("420");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds10() {
        Response actualValidateIdsResult = this.utils.validateIds("421");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds11() {
        Response actualValidateIdsResult = this.utils.validateIds("942");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds12() {
        Response actualValidateIdsResult = this.utils.validateIds("99");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds13() {
        Response actualValidateIdsResult = this.utils.validateIds("90");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds14() {
        Response actualValidateIdsResult = this.utils.validateIds("91");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds15() {
        Response actualValidateIdsResult = this.utils.validateIds("042");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds16() {
        Response actualValidateIdsResult = this.utils.validateIds("09");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds17() {
        Response actualValidateIdsResult = this.utils.validateIds("00");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#validateIds(String)}
     */
    @Test
    void testValidateIds18() {
        Response actualValidateIdsResult = this.utils.validateIds("01");
        assertEquals("Success", actualValidateIdsResult.getStatusMessage());
        assertEquals("0", actualValidateIdsResult.getStatusCode());
    }

    /**
     * Method under test: {@link Utils#isNumberValid(String)}
     */
    @Test
    void testIsNumberValid() {
        assertTrue(this.utils.isNumberValid("42"));
        assertTrue(this.utils.isNumberValid("9"));
        assertFalse(this.utils.isNumberValid("^[0-9]+"));
        assertTrue(this.utils.isNumberValid("0"));
        assertTrue(this.utils.isNumberValid("1"));
        assertTrue(this.utils.isNumberValid("4242"));
        assertTrue(this.utils.isNumberValid("429"));
        assertTrue(this.utils.isNumberValid("420"));
        assertTrue(this.utils.isNumberValid("421"));
        assertTrue(this.utils.isNumberValid("942"));
        assertTrue(this.utils.isNumberValid("99"));
        assertTrue(this.utils.isNumberValid("90"));
        assertTrue(this.utils.isNumberValid("91"));
        assertTrue(this.utils.isNumberValid("042"));
        assertTrue(this.utils.isNumberValid("09"));
        assertTrue(this.utils.isNumberValid("00"));
        assertTrue(this.utils.isNumberValid("01"));
    }
}

