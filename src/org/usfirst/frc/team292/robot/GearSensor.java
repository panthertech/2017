package org.usfirst.frc.team292.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

public class GearSensor {
	DigitalInput gearSwitch;
	Solenoid sensor1;
	Solenoid sensor2;

	public GearSensor(int sensorPort) {
		gearSwitch = new DigitalInput(sensorPort);
		sensor1 = new Solenoid(0);
		sensor2 = new Solenoid(1);
		sensor1.set(true);
		sensor2.set(true);
	}

	public boolean gearPresent() {
		return !gearSwitch.get();
	}
}
