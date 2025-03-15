const int sensorDigitalPin = D6; 

void setup() {
  Serial.begin(9600);
  pinMode(sensorDigitalPin, INPUT);
  Serial.println("Digital sensor output test started.");
}

void loop() {
  int sensorValue = digitalRead(sensorDigitalPin);
  
  Serial.print("Digital output: ");
  Serial.println(sensorValue);
  
  delay(1000);
}


// H2-103 v0.1 Humidity Sensor Test for ESP8266 (NodeMCU)
// This example reads the sensor's analog voltage (from A0) and converts it to a humidity percentage.
// Note: Calibration is required to obtain accurate humidity values.

// const int sensorPin = A0;  // Connect sensor output to A0

// void setup() {
//   Serial.begin(9600);
//   Serial.println("H2-103 v0.1 Humidity Sensor Test");
//   delay(2000);
// }

// void loop() {
//   int rawValue = analogRead(sensorPin);

//   // float rawValue;
  
//   Serial.print("Raw Analog Value: ");
//   Serial.print(rawValue);
//   Serial.print("\n");
//   // Serial.print(" -> Humidity: ");
//   // Serial.print(humidityPercent, 1);
//   // Serial.println("%");
  
//   delay(2000);
// }
