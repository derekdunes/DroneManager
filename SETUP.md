 Drones Manager Setup
---


## Steps to setup up Application

- Pull Project and open in IntelliJ IDEA
- Install all Dependencies needed for the application(this is done automatically when imported into intellj)
- Download and Install Docker into your device
- Open the docker-compose file in intellij and click the play button to start/pull the mysql docker container for the application
- When the container has started, then go to the ManagerApplication and start the application

## Steps to setup up Application

Develop a service via REST API that allows clients to communicate with the drones (i.e. **dispatch controller**). The specific communicaiton with the drone is outside the scope of this task.

The Endpoints for the key Operations:

- registering a drone (localhost:8080/drone/create);
- loading a drone with new medication items(localhost:8080/drone/medication/new?id=3);
- loading a drone with already created medication items(localhost:8080/drone/medication/joinById?droneId=2&medicationId=2);
- checking loaded medication items for a given drone (localhost:8080/drone/check_medication?id=2);
- checking available drones for loading(localhost:8080/drone/available/get);
- check drone battery level for a given drone(localhost:8080/drone/battery/level?id=1);
- For more endpoints import the postman collection(https://www.getpostman.com/collections/69f430e9c364b31df562)

## To run the unit tests 

> (due to testcontainer dependency mvn test command does not work well to run all test at once)

- Go to the test Directory
- Open the test file and run it at the class level 
- DroneServiceTest and MedicationServiceTest needs docker installed to work