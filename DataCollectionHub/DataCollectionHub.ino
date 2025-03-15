#include <ESP8266WiFi.h>
#include <ArduinoWebsockets.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>
#include <Adafruit_MPL3115A2.h>
#include "DHT.h"

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

//websocket client
using namespace websockets;
WebsocketsClient client;

//WIFI setup
// const char* ssid     = "DIGI-22tb";
// const char* password = "qGj6e94d";
const char* ssid     = "DIGI-hdP9";
const char* password = "xaM3ApJwYu";

//endpoint setup
// const char* serverHost = "192.168.100.137";
const char* serverHost = "192.168.1.130";
const int   serverPort = 5000;
const char* serverPathWS = "/ws";
const char* serverURL = "http://192.168.1.130:5000/receive-sensor-data";

void setup() {
  Serial.begin(9600);

  //sensor setup
  dht.begin();

  if (!baro.begin()) {
    Serial.println("Could not find MPL3115A2 sensor. Check wiring.");
    while(1);
  }
  baro.setSeaPressure(1013.26);

  pinMode(HW080_PIN, INPUT);

  delay(2000);

  //WIFI setup
  WiFi.begin(ssid, password);
  Serial.print("Connecting to WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWiFi connected");

  //endpoint setup
  if (client.connect(serverHost, serverPort, serverPathWS)) {
    Serial.println("Connected to WebSocket server");
  } else {
    Serial.println("Failed to connect to WebSocket server");
  }
}

void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    WiFiClient client;

    //sensor data reading
    float humidity = dht.readHumidity();
    float temperature = dht.readTemperature();
  
    if (isnan(humidity) || isnan(temperature)) {
      Serial.println("Failed to read from DHT sensor!");
    }

    float pressure = baro.getPressure();

    int luminosityVoltageValue = analogRead(TEMP6000_PIN);
    float luminosity = luminosityVoltageValue * (5 / 1023.0);

    int soilMoisture = digitalRead(HW080_PIN);

    // JSON data
    StaticJsonDocument<200> doc;
    doc["temperature"] = temperature;
    doc["humidity"] = humidity;
    doc["pressure"] = pressure;
    doc["luminosity"] = luminosity;
    doc["soilMoisture"] = soilMoisture;
        
    String jsonData;
    serializeJson(doc, jsonData);

    Serial.print("Sending sensor data: ");
    Serial.println(jsonData);

    //POST request
    http.begin(client, serverURL);
    http.addHeader("Content-Type", "application/json");
    int httpResponseCode = http.POST(jsonData);

    if (httpResponseCode > 0) {
        // Serial.print("Server Response: ");
        // Serial.println(httpResponseCode);
        // String response = http.getString();
        // Serial.println(response);
    } else {
        Serial.print("Error sending request: ");
        Serial.println(httpResponseCode);
    }
    http.end();
  } else {
    Serial.println("WiFi disconnected, cannot send data.");
  }

  delay(1000);  
}