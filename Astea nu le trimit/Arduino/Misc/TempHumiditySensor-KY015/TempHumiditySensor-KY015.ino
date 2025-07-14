#define DHpin 8
byte dat[5];

byte read_data() {
  byte result = 0;
  for (byte i = 0; i < 8; i++) {
    unsigned long startTime = micros();
    // Wait for the pin to go HIGH (start of the bit pulse)
    while (digitalRead(DHpin) == LOW) {
      if (micros() - startTime > 100) {
        // Timeout (100 microseconds); return 0 or you could flag an error
        return 0;
      }
    }
    // Wait 30 microseconds to sample the pulse width
    delayMicroseconds(30);
    if (digitalRead(DHpin) == HIGH) {
      result |= (1 << (7 - i));
    }
    startTime = micros();
    // Wait for the pin to go LOW (end of the bit pulse)
    while (digitalRead(DHpin) == HIGH) {
      if (micros() - startTime > 100) {
        break;
      }
    }
  }
  return result;
}

bool start_test() {
  // Start signal: pull data line LOW for at least 18ms (for DHT11)
  pinMode(DHpin, OUTPUT);
  digitalWrite(DHpin, LOW);
  delay(18);
  digitalWrite(DHpin, HIGH);
  delayMicroseconds(40);
  pinMode(DHpin, INPUT);

  // Sensor response: it should pull the line LOW then HIGH
  unsigned long startTime = micros();
  while (digitalRead(DHpin) == HIGH) {
    if (micros() - startTime > 100) {
      return false;  // Timeout waiting for LOW
    }
  }
  startTime = micros();
  while (digitalRead(DHpin) == LOW) {
    if (micros() - startTime > 100) {
      return false;  // Timeout waiting for HIGH
    }
  }
  startTime = micros();
  while (digitalRead(DHpin) == HIGH) {
    if (micros() - startTime > 100) {
      return false;  // Timeout waiting for the end of the response pulse
    }
  }

  // Read 5 bytes of data
  for (int i = 0; i < 5; i++) {
    dat[i] = read_data();
  }

  // End communication: set pin back to HIGH
  pinMode(DHpin, OUTPUT);
  digitalWrite(DHpin, HIGH);
  return true;
}

void setup() {
  Serial.begin(9600);
  pinMode(DHpin, OUTPUT);
  digitalWrite(DHpin, HIGH);
}

void loop() {
  if (!start_test()) {
    Serial.println("Sensor not responding.");
    delay(2000);
    return;
  }

  byte checksum = (dat[0] + dat[1] + dat[2] + dat[3]) & 0xFF;
  if (dat[4] != checksum) {
    Serial.println("Checksum error!");
  } else {
    // For a DHT11 sensor:
    int humidity = dat[0];
    int temperature = dat[2];

    Serial.print("Humidity: ");
    Serial.print(humidity);
    Serial.println(" %");

    Serial.print("Temperature: ");
    Serial.print(temperature);
    Serial.println(" C");
  }

  delay(1000);
}
