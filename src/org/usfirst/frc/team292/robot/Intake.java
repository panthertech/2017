package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;

public class Intake {
		CANTalon intakeTalon;
	public Intake(int intakePort){
		intakeTalon = new CANTalon(intakePort);
	}
	public void on(){
		intakeTalon.set(1);
	}
	public void off(){
		intakeTalon.set(0);
	}
	public void reverse(){
		intakeTalon.set(-.5);
	}
}
