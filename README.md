# CO2 Calculator Tool

This project is a CO2 emission calculator that uses city coordinates and transportation methods to calculate the CO2 emissions for a trip.

## Prerequisites
- Java
- IntelliJ IDEA or your preferred Java IDE
- Maven

## Resources
- **OpenRouteService API Docs**: [https://openrouteservice.org/](https://openrouteservice.org/)

## API Key Setup
The tool requires an API key for OpenRouteService. Store it in an environment variable "ORS_TOKEN":

## Setup Instructions

1. **Download the Project**:
  - Download the zip file of the project from upload link.
  - Extract the zip file to a directory of your choice.

2. **Open the Project in IntelliJ IDEA**:
  - Open IntelliJ IDEA.
  - Select `Open` and choose the extracted project folder.
  - IntelliJ will automatically detect the project structure and load it.

3. **Build the Project**:
  - Open the terminal in IntelliJ and run the following command to build the project using Maven:
    `mvn clean install`

4. **Run the Application**:
  - In IntelliJ, right-click on the main class `Co2CalculatorApplication.java` (inside the `src/main/java/com/siddhesh/co2calculator` directory).
  - Select `Run 'Co2CalculatorApplication'`.

5. **Test the Application**:
  - Once the Spring Boot application is running, open your browser or API testing tool (like Postman).
  - Access the following URL to test the CO2 calculator:
    ```
    http://localhost:8080/api/v1/co2-calculator?start=Hamburg&end=Berlin&transportation-method=diesel-car-medium
    ```
  - Replace the `start`, `end`, and `transportation-method` parameters with the desired values to calculate CO2 emissions.
  - Example Output:
    ```
    Your trip caused 49.2kg of CO2-equivalent.
    ```
  - Parameters can be passed in any order.

## Troubleshooting
- If you encounter issues with dependencies, try running `mvn clean install` again or check your Java version.
- Ensure that `localhost:8080` is available and the application is running.

## Testing
Run all unit tests with:
  `mvn test`
