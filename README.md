# SensorsAPI

REST API for storing sensor data.

The API consumes and produces JSON for communication and supports two types of sensors: Temperature and Humidity.
Both type of the sensors have UUID, Name and Location as properties.

The specific properties for are:

1. Temperature

  - temperature

2. Humidity

  - temperature
  - humidity

The API is accessed from http://localhost:8080/sensors/api/

Available operations are:

- http://localhost:8080/sensors/api/(temperature|humidity) - POST
  - Creates new sensor resource. Returns empty body with Location header where the resource can be found
  - The resource identity is UUID
- http://localhost:8080/sensors/api/(temperature|humidity)/{UUID} - GET, PUT, DELETE
  - GET - shows the resource data
  - PUT - updates the resource data
  - DELETE - removes the resource
- http://localhost:8080/sensors/api/(temperature|humidity)/list - GET
  - Lists all available sensor UUIDs
