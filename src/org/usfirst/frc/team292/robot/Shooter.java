package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Servo;

public class Shooter {

	CANTalon shooterTalon;
	Servo feederServo;

	public Shooter(int shooterPort, int feederPort) {
		shooterTalon = new CANTalon(shooterPort);
		feederServo = new Servo(feederPort);
	}

	public void enableShooter() {
		shooterTalon.set(.25);
	}

	public void disableShooter() {
		shooterTalon.set(0);
	}

	public void shoot(boolean shoot) {
		if(shoot){
			feederServo.set(.3);
		}else{
			feederServo.set(0);
		}
	}
}
