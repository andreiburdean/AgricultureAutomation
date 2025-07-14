#include <Servo.h>

Servo servo;  

void setup() {
  servo.attach(9);  
}

void loop() {
  for (int pos = 0; pos <= 45; pos += 1) {  
    servo.write(pos);  
    delay(10); 
  }

  for (int pos = 45; pos >= 0; pos -= 1) {
    servo.write(pos); 
    delay(10);                              
  }
}
