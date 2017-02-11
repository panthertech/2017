package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Servo;

public class Shooter {

	CANTalon shooterTalon;
	Servo feederServo;

	public Shooter(int shooterPort, int feederPort) {
		shooterTalon = new CANTalon(shooterPort);
		feederServo = new Servo(feederPort);

		shooterTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
	}

	public void enableShooter() {
		shooterTalon.changeControlMode(TalonControlMode.Speed);
		shooterTalon.setP(.04);
		shooterTalon.setI(.0002);
		shooterTalon.setD(.0001);
		shooterTalon.set(4500);
	}

	public void disableShooter() {
		shooterTalon.changeControlMode(TalonControlMode.PercentVbus);
		shooterTalon.set(0);
	}

	public double getShooterSpeed() {
		return shooterTalon.getSpeed();
	}

	public double getShooterPercentVbus() {
		return shooterTalon.getOutputVoltage() / shooterTalon.getBusVoltage();
	}

	public void shoot(boolean shoot) {
		if (shoot) {
			feederServo.set(.3);
		} else {
			feederServo.set(0);
		}
	}
}
