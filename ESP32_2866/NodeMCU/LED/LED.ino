const int ledPin = D1;
bool ledState = false;

void setup() {
    Serial.begin(9600);
    pinMode(ledPin, OUTPUT);
    digitalWrite(ledPin, LOW); 
}

void loop() {
    digitalWrite(ledPin, HIGH);
    ledState = true;
    digitalWrite(ledPin, LOW);
    ledState = false;
}
