#include <Wire.h>
#include "Adafruit_SI1145.h"

Adafruit_SI1145 uv = Adafruit_SI1145();

void setup() {
  Serial.begin(9600);
  
  Wire.begin(D2, D1);
  
  Serial.println("Adafruit SI1145 test on ESP8266 NodeMCU");

  if (!uv.begin()) {
    Serial.println("Didn't find SI1145 sensor");
    while (1);
  }

  Serial.println("SI1145 sensor found!");
}

void loop() {
  Serial.println("===================");
  
  uint16_t visible = uv.readVisible();
  Serial.print("Visible: ");
  Serial.println(visible);
  
  uint16_t ir = uv.readIR();
  Serial.print("IR: ");
  Serial.println(ir);
  
  float uvIndex = uv.readUV() / 100.0;
  Serial.print("UV: ");
  Serial.println(uvIndex);
  
  delay(1000);  
}
