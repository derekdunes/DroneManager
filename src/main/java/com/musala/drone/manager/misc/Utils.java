package com.musala.drone.manager.misc;

import com.musala.drone.manager.dto.DroneDto;
import com.musala.drone.manager.dto.MedicationDto;
import com.musala.drone.manager.dto.Response;
import com.musala.drone.manager.enums.Model;
import com.musala.drone.manager.enums.State;
import org.springframework.stereotype.Component;

@Component
public class Utils {
    public Model getModelFromString(String model) {

        if (model.equalsIgnoreCase("LIGHTWEIGHT")){
            return Model.LIGHTWEIGHT;
        } else if (model.equalsIgnoreCase("MIDDLEWEIGHT")){
            return Model.MIDDLEWEIGHT;
        } else if (model.equalsIgnoreCase("CRUISERWEIGHT")){
            return Model.CRUISERWEIGHT;
        } else if (model.equalsIgnoreCase("HEAVYWEIGHT")){
            return Model.HEAVYWEIGHT;
        }  else {
            return Model.INVALID;
        }
    }

    public String getStringFromModel(Model model) {

        if (model == Model.LIGHTWEIGHT){
            return "LIGHTWEIGHT";
        }

        if (model == Model.MIDDLEWEIGHT){
            return "MIDDLEWEIGHT";
        }

        if (model == Model.CRUISERWEIGHT){
            return "CRUISERWEIGHT";
        }

        if (model == Model.HEAVYWEIGHT){
            return "HEAVYWEIGHT";
        }

        return "Invalid"; // this guy might cause issue always check the endpoint for null response
    }

    public String getStateAsString(State state) {

        if (state == State.IDLE){
            return "IDLE";
        } else if (state == State.LOADING) {
            return "LOADING";
        } else if (state == State.LOADED) {
            return "LOADED";
        } else if (state == State.DELIVERING) {
            return "DELIVERING";
        } else if (state == State.DELIVERED) {
            return "DELIVERED";
        } else if (state == State.RETURNING){
            return "RETURNING";
        } else {
            return "INVALID";
        }

    }

    public State getStateFromString(String state) {

        if (state.equalsIgnoreCase("IDLE")){
            return State.IDLE;
        } else if (state.equalsIgnoreCase("LOADING")) {
            return State.LOADING;
        } else if (state.equalsIgnoreCase("LOADED")) {
            return State.LOADED;
        } else if (state.equalsIgnoreCase("DELIVERING")) {
            return State.DELIVERING;
        } else if (state.equalsIgnoreCase("DELIVERED")) {
            return State.DELIVERED;
        } else if (state.equalsIgnoreCase("RETURNING")){
            return State.RETURNING;
        } else {
            return State.INVALID;
        }

    }

    public Response validateDrone(DroneDto request) {

        String model = request.getModel().trim();
        int weight = request.getWeight();
        int batteryCapacity = request.getBatteryCapacity();
        String state = request.getState();
        String serialNumber = request.getSerialNumber();

        if (model.isEmpty()){
            return new Response("1", "The Drone Model Name is empty, please provide a valid name");
        }

        if (state.isEmpty()){
            return new Response("1", "The Drone Code is empty, please provide a valid Code");
        }

        if (serialNumber.isEmpty()){
            return new Response("1", "The Drone Serial Number  is empty, please provide a valid serial number");
        }

        //        weight limit (500gr max);
        if (weight <= 0){
            return new Response("1", "The Drone Weight is less than or equal to Zero(0), please provide a valid Weight");
        }

        if (weight > 500){
            return new Response("1", "The Drone Weight is greater than the Max(500) supported value, please provide a valid Weight");
        }

        //battery capacity (percentage);
        if (batteryCapacity <= 0){
            return new Response("1", "The Drone battery capacity is less than or equal to Zero(0), please provide a valid value");
        }

        if (batteryCapacity > 100){
            return new Response("1", "The Drone battery capacity is greater than Max(100) supported value, please provide a valid value");
        }

        //serial number (100 characters max);
        if (serialNumber.length() > 100) {
            return new Response("1", "The maximum length of a serial number is 100 characters. Please provide a valid numbers");
        }

        //serial number (100 characters max);
        if (!isNumberValid(serialNumber)) {
            return new Response("1", "The Serial Number is invalid, please provide a number only serial number");
        }

        //validate the model
        //model (LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT, HEAVYWEIGHT);
        if(Model.INVALID == getModelFromString(model)){
            return new Response("1", "The model is invalid, please provide the supported model types (LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT, HEAVYWEIGHT)");
        }

        //state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).
        if(State.INVALID == getStateFromString(state)){
            return new Response("1", "The state is invalid, please provide the supported state types (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING)");
        }

        return new Response("0", "Success");
    }

    public Response validateMedication(MedicationDto request) {

        String name = request.getName().trim();
        int weight = request.getWeight();
        String image = request.getImage().trim();
        String code = request.getCode().trim();

//        name (allowed only letters, numbers, ‘-‘, ‘_’);
//        weight;
//        code (allowed only upper case letters, underscore and numbers);
//        image (picture of the medication case).

        if (name.isEmpty()){
            return new Response("1", "The Medication Name provided is empty, please input a valid name");
        }

        if (weight <= 0){
            return new Response("1", "The Medication Weight provided is less than or equal to Zero(0), please input a valid Weight");
        }

        if (code.isEmpty()){
            return new Response("1", "The Medication Code provided is empty, please input a valid Code");
        }

        if (image.isEmpty()){
            return new Response("1", "The Image link provided is empty, please input a valid Image Link");
        }

        //name (allowed only letters, numbers, ‘-‘, ‘_’);
        if (!name.matches("^[a-zA-Z0-9_-]*$")){
            return new Response("1", "The Name of Medication should only contain letters, numbers, hyphen(-) and  underscore(_), Kindly Provide a valid name");
        }

        //code (allowed only upper case letters, underscore and numbers);
        if (!code.matches("^[A-Z0-9-]*$")){
            return new Response("1", "The Code of Medication should only contain Uppercase letters, numbers and hyphen(-), Kindly provide a valid code");
        }

        //image (picture of the medication case). image url verif regex
        if(!image.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")){
            return new Response("1", "The image link should start with (http://) or (https://), Kindly provide a valid image link");
        }

        //image (picture of the medication case). image
        if(!image.endsWith(".jpg")){
            if (!image.endsWith(".png")){
                if (!image.endsWith(".web")){
                    return new Response("1", "The image link should end with a supported image type eg (.png, jpg, .web), Kindly provide a valid image link");
                }
            }
        }

        return new Response("0", "Success");
    }

    public Response validateIds(String id){

        String uniqueId = id.trim();

        //check null
        if (uniqueId.isEmpty()){
            return new Response("1", "The Id is empty, please provide a valid Id");
        }

        //check if its a valid number
        if(!isNumberValid(uniqueId)){
            return new Response("1", "The Id is not a valid number, please provide a valid Id");
        }

        //check if its parsable to long
        try {
            Long.parseLong(uniqueId);
        } catch(Exception e) {
            return new Response("1", "The Id is invalid, please provide a valid Id");
        }


        return new Response("0", "Success");
    }


    public boolean isNumberValid(String number) {

        return number.matches("^[0-9]+");
    }

}
