package com.musala.drone.manager.Service;

import com.musala.drone.manager.ContainerInitializer;
import com.musala.drone.manager.dto.DroneDto;
import com.musala.drone.manager.dto.Response;
import com.musala.drone.manager.dto.MedicationDto;
import com.musala.drone.manager.misc.Utils;
import com.musala.drone.manager.services.interfaces.DroneServiceInterface;
import com.musala.drone.manager.services.interfaces.MedicationServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MedicationServiceTest extends ContainerInitializer {

    @Autowired
    MedicationServiceInterface medicationService;

    @Autowired
    DroneServiceInterface droneService;

    @Autowired
    Utils utils;

    @ParameterizedTest
    @Transactional
    @DisplayName("Create Medications tests")
    @CsvSource({"500, MUSK-419, twitter, dummyLink", "250, AMG-419, Bird, nbaLink", "250, AMG-419, Bird, nbaLink", "100, MSX-YZ250, Drug Medicals, https://www.images.com/drone.png"})
    public void CreateMedicationTests(String weight, String code, String name, String image){

        //create medic
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setWeight(Integer.parseInt(weight));
        medicationDto.setCode(code);
        medicationDto.setName(name);
        medicationDto.setImage(image);

        //save
        Response response = medicationService.createMedication(medicationDto);

        assertEquals("0", response.getStatusCode());
        assertEquals("Success", response.getStatusMessage());
        assertNotNull(response.getMedication().getId());

        //get saved data
        Response fetchResponse = medicationService.getMedication(medicationDto.getId());

        //validate data
        assertEquals("0", fetchResponse.getStatusCode());
        assertEquals("Success", fetchResponse.getStatusMessage());
        assertEquals(weight,  Integer.toString(fetchResponse.getMedication().getWeight()));
        assertEquals(code, fetchResponse.getMedication().getCode());
        assertEquals(name, fetchResponse.getMedication().getName());
        assertEquals(image, fetchResponse.getMedication().getImage());
    }


    @Nested
    @DisplayName("All Get Medication Tests")
    class GetMedicationsTests {

        //get one medication
        //public DroneResponse getMedication(Long medicationId);
        @Test
        @Transactional
        public void getMedicationTest() {

            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(100);
            medicationDto.setCode("MSX-YZ250");
            medicationDto.setName("Drug Medicals");
            medicationDto.setImage("https://www.images.com/drone.png");

            MedicationDto medicationDto2 = new MedicationDto();
            medicationDto2.setWeight(150);
            medicationDto2.setCode("MUS-YZ250");
            medicationDto2.setName("Medicals Document");
            medicationDto2.setImage("https://www.images.com/drone.png");

            Response response = medicationService.createMedication(medicationDto);
            Response response2 = medicationService.createMedication(medicationDto2);

            Long firstId = response.getMedication().getId();
            Long secondId = response2.getMedication().getId();

            Response secondMedicationInDb = medicationService.getMedication(secondId);

            Response firstMedicationInDb = medicationService.getMedication(firstId);

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());

            assertEquals("0", response2.getStatusCode());
            assertEquals("Success", response2.getStatusMessage());

            assertEquals("0", secondMedicationInDb.getStatusCode());
            assertEquals("Success", secondMedicationInDb.getStatusMessage());

            assertEquals(secondId, secondMedicationInDb.getMedication().getId());
            assertEquals(medicationDto2.getCode(), secondMedicationInDb.getMedication().getCode());
            assertEquals(medicationDto2.getName(), secondMedicationInDb.getMedication().getName());
            assertEquals(medicationDto2.getWeight(), secondMedicationInDb.getMedication().getWeight());
            assertEquals(medicationDto2.getImage(), secondMedicationInDb.getMedication().getImage());

            assertEquals("0", firstMedicationInDb.getStatusCode());
            assertEquals("Success", firstMedicationInDb.getStatusMessage());

            assertEquals(firstId, firstMedicationInDb.getMedication().getId());
            assertEquals(medicationDto.getCode(), firstMedicationInDb.getMedication().getCode());
            assertEquals(medicationDto.getName(), firstMedicationInDb.getMedication().getName());
            assertEquals(medicationDto.getWeight(), firstMedicationInDb.getMedication().getWeight());
            assertEquals(medicationDto.getImage(), firstMedicationInDb.getMedication().getImage());

        }


        //get all medications
        //public DroneResponse getAllMedications();
        @Test
        @Transactional
        public void getAllMedications() {
            //create drones and save
            MedicationDto medication = new MedicationDto();
            medication.setWeight(1000);
            medication.setCode("MEDEQ-YZ250");
            medication.setName("Medical Equipment");
            medication.setImage("https://www.images.com/drone.png");

            MedicationDto medication2 = new MedicationDto();
            medication.setWeight(500);
            medication.setCode("DRUG-YZ250");
            medication.setName("Drug Medicals");
            medication.setImage("https://www.images.com/drone.png");

            MedicationDto medication3 = new MedicationDto();
            medication.setWeight(600);
            medication.setCode("COV-YZ250");
            medication.setName("Covid Equipments");
            medication.setImage("https://www.images.com/drone.png");

            medicationService.createMedication(medication);
            medicationService.createMedication(medication2);
            medicationService.createMedication(medication3);

            Response response = medicationService.getAllMedications();

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertEquals(3, response.getMedicationList().size());

        }

    }

    //update medication
    //public DroneResponse updateMedication(MedicationDto medication, Long medicationId);
    @ParameterizedTest
    @Transactional
    @DisplayName("Update Medication tests")
    @CsvSource({"500, MUSK-419, twitter, dummyLink", "250, AMG-419, Bird, nbaLink", "250, AMG-419, Bird, nbaLink"})
    public void updateMedicationTest(String newWeight, String newCode, String newName, String newImage){

        //create medic
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setWeight(100);
        medicationDto.setCode("MSX-YZ250");
        medicationDto.setName("Drug Medicals");
        medicationDto.setImage("https://www.images.com/drone.png");

        //save
        Response response = medicationService.createMedication(medicationDto);

        assertEquals("0", response.getStatusCode());
        assertEquals("Success", response.getStatusMessage());
        assertNotNull(response.getMedication().getId());

        //update medic with params
        medicationDto.setWeight(Integer.parseInt(newWeight));
        medicationDto.setCode(newCode);
        medicationDto.setName(newName);
        medicationDto.setImage(newImage);

        medicationService.updateMedication(medicationDto, medicationDto.getId());

        //get new params
        Response updatedResponse = medicationService.getMedication(medicationDto.getId());

        //validate update
        assertEquals("0", updatedResponse.getStatusCode());
        assertEquals("Success", updatedResponse.getStatusMessage());
        assertEquals(newWeight,  Integer.toString(updatedResponse.getMedication().getWeight()));
        assertEquals(newCode, updatedResponse.getMedication().getCode());
        assertEquals(newName, updatedResponse.getMedication().getName());
        assertEquals(newImage, updatedResponse.getMedication().getImage());
    }


    //public DroneResponse checkDroneCarryingMedication(Long medicationId);
    //create Drone, //create medic tie medic to drone and vice versa
    //perform query
//    @Test
//    @Transactional
//    @DisplayName("Check Drone Tied to Medication test")
//    public void CheckDroneTiedToMedicationTest(){
//
//        DroneDto droneDto = new DroneDto();
//        droneDto.setBatteryCapacity(100);
//        droneDto.setModel("HEAVYWEIGHT");
//        droneDto.setSerialNumber("Nimbus_2000");
//        droneDto.setWeight(500);
//
//        Response droneResponse = droneService.createDrone(droneDto);
//
//        assertEquals("0", droneResponse.getStatusCode());
//        assertEquals("Success", droneResponse.getStatusMessage());
//        assertNotNull(droneResponse.getDrone().getId());
//
//        //create medic and tie it to drone
//        MedicationDto medicationDto = new MedicationDto();
//        medicationDto.setWeight(100);
//        medicationDto.setCode("MSX-YZ250");
//        medicationDto.setName("Drug Medicals");
//        medicationDto.setImage("https://www.images.com/drone.png");

//        //Tie medication to drone(Switched to Cascade.persist to reduce multiple queries causing the medication id to stop returning during tests)
//        Response medicationResponse = droneService.loadDroneWithNewMedication(medicationDto, droneResponse.getDrone().getId());
//
//        assertEquals("0", medicationResponse.getStatusCode());
//        assertEquals("Success", medicationResponse.getStatusMessage());
//        //assertNotNull(medicationResponse.getMedication().getId());
//
//        //perform Main Query
//        Response checkDroneCarryingMedicationResponse = medicationService.checkDroneCarryingMedication(medicationResponse.getMedication().getId());
//
//        //validate update
//        assertEquals("0", checkDroneCarryingMedicationResponse.getStatusCode());
//        assertEquals("Success", checkDroneCarryingMedicationResponse.getStatusMessage());
//        assertEquals(droneResponse.getDrone().getId(),  checkDroneCarryingMedicationResponse.getMedication().getDrone().getId());
//        assertEquals(droneDto.getSerialNumber(), checkDroneCarryingMedicationResponse.getMedication().getDrone().getSerialNumber());
//        assertEquals(utils.getModelFromString(droneDto.getModel()), checkDroneCarryingMedicationResponse.getMedication().getDrone().getModel());
//        assertEquals(droneDto.getBatteryCapacity(), checkDroneCarryingMedicationResponse.getMedication().getDrone().getBatteryCapacity());
//        assertEquals(droneDto.getWeight(), checkDroneCarryingMedicationResponse.getMedication().getDrone().getWeight());
//
//    }

    //delete medication
    //public DroneResponse deleteMedication(Long medicationId);
    //only delete a medication if Drone is not carrying it(meaning its id is not in the Drone join column anymore)
    @Nested
    @Transactional
    @DisplayName("Delete all medication tests")
    class deleteMedicationTests {
        @ParameterizedTest
        @DisplayName("Delete Untied Medications tests")
        @CsvSource({"500, MUSK-419, twitter, dummyLink", "250, AMG-419, Bird, nbaLink", "250, AMG-419, Bird, nbaLink", "100, MSX-YZ250, Drug Medicals, https://www.images.com/drone.png"})
        public void deleteUntiedMedicationTests(String weight, String code, String name, String image){

            //create medic
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(Integer.parseInt(weight));
            medicationDto.setCode(code);
            medicationDto.setName(name);
            medicationDto.setImage(image);

            //save
            Response response = medicationService.createMedication(medicationDto);

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getMedication().getId());

            Response deleteResponse = medicationService.deleteMedication(medicationDto.getId());

            assertEquals("0", deleteResponse.getStatusCode());
            assertEquals("Success", deleteResponse.getStatusMessage());

            //get saved data
            Response fetchResponse = medicationService.getMedication(medicationDto.getId());

            String errorMessage = "No Medication found with this ID " + medicationDto.getId();

            //validate data
            assertEquals("1", fetchResponse.getStatusCode());
            assertEquals(errorMessage, fetchResponse.getStatusMessage());

        }

        //public DroneResponse checkDroneCarryingMedication(Long medicationId);
        //create Drone, //create medic tie medic to drone and vice versa
        //perform query

//        @Test
//        @Transactional
//        @DisplayName("Delete Medication Tied to a Drone test")
//        public void deleteTiedMedicationTests(){
//
//            DroneDto droneDto = new DroneDto();
//            droneDto.setBatteryCapacity(100);
//            droneDto.setModel("HEAVYWEIGHT");
//            droneDto.setSerialNumber("Tesla_3");
//            droneDto.setWeight(500);
//
//            Response droneResponse = droneService.createDrone(droneDto);
//
//            assertEquals("0", droneResponse.getStatusCode());
//            assertEquals("Success", droneResponse.getStatusMessage());
//            assertNotNull(droneResponse.getDrone().getId());
//
//            //create medic and tie it to drone
//            MedicationDto medicationDto = new MedicationDto();
//            medicationDto.setWeight(100);
//            medicationDto.setCode("MSX-YZ250");
//            medicationDto.setName("Drug Medicals");
//            medicationDto.setImage("https://www.images.com/drone.png");
//
//            //Tie medication to drone(Switched to Cascade.persist to reduce multiple queries causing the medication id to stop returning during tests)
//            Response medicationResponse = droneService.loadDroneWithNewMedication(medicationDto, droneResponse.getDrone().getId());
//
//            assertEquals("0", medicationResponse.getStatusCode());
//            assertEquals("Success", medicationResponse.getStatusMessage());
//            //assertNotNull(medicationResponse.getMedication().getId());
//
//            //try to delete tied medication to drone
//            Response deleteResponse = medicationService.deleteMedication(medicationResponse.getMedication().getId());
//
//            String errorMessage = "Cannot delete Id, still tied" + medicationResponse.getMedication().getId();
//
//            //validate delete
//            assertEquals("1", deleteResponse.getStatusCode());
//            assertEquals(errorMessage, deleteResponse.getStatusMessage());
//
//        }

    }
}
