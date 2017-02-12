package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Servo;

public class Shooter {
	private static final double kTargetSpeed = 4500;
	private static final double kAllowedSpeedDifference = 200;
	private static final double kFeederShootPosition = 0.3;
	private static final double kFeederHoldPosition = 0.0;
	private static final double kShooterP = 0.04;
	private static final double kShooterI = 0.0002;
	private static final double kShooterD = 0.0001;
	
	private CANTalon shooterTalon;
	private Servo feederServo;
	private boolean shooterEnabled;
	private boolean shooterEnabledPrev;

	public Shooter(int shooterPort, int feederPort) {
		shooterTalon = new CANTalon(shooterPort);
		shooterTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		feederServo = new Servo(feederPort);
		shooterEnabled = false;
	}

	public void enableShooter() {
		shooterEnabled = true;
	}

	public void disableShooter() {
		shooterEnabled = false;
	}

	public double getShooterSpeed() {
		return shooterTalon.getSpeed();
	}

	public double getShooterPercent() {
		return shooterTalon.getOutputVoltage() * 100 / shooterTalon.getBusVoltage();
	}
	
	public boolean shooterEnabled() {
		return shooterEnabled;
	}

	public void shoot(boolean shoot) {
		if (shoot) {
			shooterEnabled = true;
			if((shooterTalon.getSpeed() > kTargetSpeed - kAllowedSpeedDifference) &&
					(shooterTalon.getSpeed() < kTargetSpeed + kAllowedSpeedDifference)) {
				feederServo.set(kFeederShootPosition);
			} else {
				feederServo.set(kFeederHoldPosition);
			}
		} else {
			feederServo.set(kFeederHoldPosition);
		}
		
		if(shooterEnabled) {
			if(!shooterEnabledPrev) {
				shooterTalon.changeControlMode(TalonControlMode.Speed);
				shooterTalon.setP(kShooterP);
				shooterTalon.setI(kShooterI);
				shooterTalon.setD(kShooterD);
				shooterEnabledPrev = true;
			}
			shooterTalon.set(kTargetSpeed);
		} else {
			if(shooterEnabledPrev) {
				shooterTalon.changeControlMode(TalonControlMode.PercentVbus);
				shooterEnabledPrev = false;
			}
			shooterTalon.set(0);
		}
	}
}
