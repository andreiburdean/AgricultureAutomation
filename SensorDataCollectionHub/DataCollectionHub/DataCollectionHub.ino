#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>
#include <Adafruit_MPL3115A2.h>
#include "DHT.h"
#include <math.h>

//pin setup
#define DHT_PIN D7
#define DHT_TYPE DHT11
#define HW080_PIN D6
#define TEMP6000_PIN A0
#define MPL_SCL_PIN D1
#define MPL_SDA_PIN D2

//sensor setup
DHT dht(DHT_PIN, DHT_TYPE);
Adafruit_MPL3115A2 baro;

//WIFI setup

const String ssid     = "FC Petrolul Potcoava";
const String password = "whatever";

//endpoint setup
const String serverHost = "192.168.108.171";
const int serverPort = 5000;
const String serverURL = "http://" + serverHost + ":" + serverPort + "/receive-sensor-data";

void setup() {
  Serial.begin(9600);

  //sensor setup
  dht.begin();

  if (!baro.begin()) {
    Serial.println("The atmospheric pressure sensor is not properly connected. Check the wiring.");
    while(1);
  }

  baro.setSeaPressure(1000);
  pinMode(HW080_PIN, INPUT);
  delay(2000);

  //WIFI setup
  WiFi.begin(ssid, password);
  Serial.print("Connecting to the specified WiFi network.");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWiFi connected.");
}

void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    WiFiClient client;

    //sensor data reading
    float humidity = dht.readHumidity();
    float temperature = dht.readTemperature();
  
    if (isnan(humidity) || isnan(temperature)) {
      Serial.println("Failed to read from thr DHT sensor.");
    }

    float pressure = baro.getPressure();
    int luminosityVoltageValue = analogRead(TEMP6000_PIN);
    float luminosity = luminosityVoltageValue * (5 / 1023.0);
    int soilMoisture = digitalRead(HW080_PIN);

    //JSON data
    StaticJsonDocument<200> doc;
    doc["temperature"] = temperature;
    doc["humidity"] = humidity;
    doc["pressure"] = pressure;
    doc["luminosity"] = luminosity;
    doc["soilMoisture"] = soilMoisture;
        
    String jsonData;
    serializeJson(doc, jsonData);
    Serial.println(jsonData);

    //POST request
    http.begin(client, serverURL);
    http.addHeader("Content-Type", "application/json");
    int httpResponseCode = http.POST(jsonData);

    if (httpResponseCode > 0) {
    } else {
        Serial.print("Error sending request: ");
        Serial.println(httpResponseCode);
    }
    http.end();
  } else {
    Serial.println("The WiFi connection has disconnected, cannot send sensor data.");
  }

  delay(100);  
}