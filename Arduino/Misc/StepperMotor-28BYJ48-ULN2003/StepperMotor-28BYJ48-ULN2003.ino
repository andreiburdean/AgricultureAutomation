#include <Stepper.h>

const int stepsPerRevolution = 2048;
Stepper myStepper = Stepper(stepsPerRevolution, 8, 10, 9, 11);

void setup() {}

void loop() {
	myStepper.setSpeed(15);
	myStepper.step(stepsPerRevolution);
}