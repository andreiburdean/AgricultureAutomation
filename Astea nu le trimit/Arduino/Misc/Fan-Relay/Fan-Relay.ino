int pinOut = 7;

void setup() {
  Serial.begin(9600);
  pinMode(pinOut, OUTPUT);
}

void loop() {             
  digitalWrite(pinOut, HIGH);
  delay(10000);
  digitalWrite(pinOut, LOW);
  delay(10000);           
}