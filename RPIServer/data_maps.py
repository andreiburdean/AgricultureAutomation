#the id that identifies this RPi5 on the server
RASPBERRY_ID: int = 1

#the map that holds the sensor readings
#gets updated every time the server receives new readings
sensor_data_map = {
    "temperature": 0,
    "humidity": 0,
    "pressure": 0,
    "luminosity": 0,
    "soil_moisture": 0
}

#holds the parameters based on which the server decides wether or not to control the actuators
sensor_control_map = {
    "succesful_server_startup_response": 0,
    "temperature": 0,
    "humidity": 0,
    "luminosity": 0,
    "soil_moisture": 0
}

#holds the manual control values for when the user wants to manually control the actuators
manual_control_map = {
    "switch_control": 0,
    "fan": 0,
    "pump": 0,
    "led": 0
}
