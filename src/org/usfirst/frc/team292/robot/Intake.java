package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;

public class Intake {
	private static final double kIntakeSpeed = -0.6;
	private static final double kReverseSpeed = 0.5;
	
	private CANTalon intakeTalon;
	private boolean intake;

	public Intake(int intakePort) {
		intakeTalon = new CANTalon(intakePort);
	}

	public void on() {
		intake = true;
		intakeTalon.set(kIntakeSpeed);
	}

	public void reverse() {
		intake = false;
		intakeTalon.set(kReverseSpeed);
	}
	
	public void maintain() {
		if(intake) {
			intakeTalon.set(kIntakeSpeed);
		} else {
			intakeTalon.set(0);
		}
	}

	public void off() {
		intake = false;
		intakeTalon.set(0);
	}
	
	public boolean getIntakeEnabled() {
		return intake;
	}
}
