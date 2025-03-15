int buzzer = 8;

void setup() {
	pinMode(buzzer, OUTPUT); 
}

void loop() {
	for (int i = 0; i < 125; i++) { 
		digitalWrite(buzzer, HIGH);
		delay(5); 
		digitalWrite(buzzer, LOW); 
		delay(5);
	}
	delay(50);
	for (int j = 0; j < 200; j++) { 
		digitalWrite(buzzer, HIGH);
		delay(2);
		digitalWrite(buzzer, LOW);
		delay(2);
	}
	delay(200);
}