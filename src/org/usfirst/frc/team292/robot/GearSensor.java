package org.usfirst.frc.team292.robot;

import edu.wpi.first.wpilibj.DigitalInput;

public class GearSensor {
	DigitalInput gearSwitch;

	public GearSensor(int sensorPort) {
		gearSwitch = new DigitalInput(sensorPort);
	}

	public boolean gearPresent() {
		return gearSwitch.get();
	}
}
