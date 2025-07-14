void setup() {
  Serial.begin(9600);
  Serial.println("TEMT6000 Ambient Light Sensor Test");
}

void loop() {
  int sensorValue = analogRead(A0);
  
  float voltage = sensorValue * (5.0 / 1023.0);
  
  Serial.print("Sensor Value: ");
  Serial.print(sensorValue);
  Serial.print("  Voltage: ");
  Serial.print(voltage, 3);
  Serial.println(" V");
  
  delay(1000);
}
