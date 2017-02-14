package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
	RobotDrive robotDrive;
	CANTalon frontLeftTalon;
	CANTalon frontRightTalon;
	CANTalon rearLeftTalon;
	CANTalon rearRightTalon;
	Gyro gyro;

	public Drive(int frontLeftPort, int rearLeftPort, int frontRightPort, int rearRightPort, Gyro gyro) {
		frontLeftTalon = new CANTalon(frontLeftPort);
		frontLeftTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		frontLeftTalon.reverseSensor(true);
		
		rearLeftTalon = new CANTalon(rearLeftPort);
		rearLeftTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		rearLeftTalon.reverseSensor(true);
		
		frontRightTalon = new CANTalon(frontRightPort);
		frontRightTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		
		rearRightTalon = new CANTalon(rearRightPort);
		rearRightTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		
		robotDrive = new RobotDrive(frontLeftTalon, rearLeftTalon, frontRightTalon, rearRightTalon);
		robotDrive.setInvertedMotor(MotorType.kFrontRight, true);
		robotDrive.setInvertedMotor(MotorType.kRearRight, true);
		robotDrive.setInvertedMotor(MotorType.kFrontLeft, false);
		robotDrive.setInvertedMotor(MotorType.kRearLeft, false);
		robotDrive.setExpiration(0.1);
		this.gyro = gyro;
	}

	public void mecanum(double x, double y, double z) {
		mecanum(x, y, z, false);
		SmartDashboard.putNumber("Front Left", getFrontLeftPosition());
		SmartDashboard.putNumber("Rear Left", getRearLeftPosition());
		SmartDashboard.putNumber("Front Right", getFrontRightPosition());
		SmartDashboard.putNumber("Rear Right", getRearRightPosition());
	}

	public void mecanum(double x, double y, double z, boolean useGyroAngle) {
		if(useGyroAngle) {
			robotDrive.mecanumDrive_Cartesian(x, y, z, gyro.getAngle());
		} else {
			robotDrive.mecanumDrive_Cartesian(x, y, z, 0);
		}
	}
	
	public void stop() {
		robotDrive.stopMotor();
	}

	public double getFrontLeftPosition() {
		return frontLeftTalon.getPosition();
	}

	public double getRearLeftPosition() {
		return rearLeftTalon.getPosition();
	}

	public double getFrontRightPosition() {
		return frontRightTalon.getPosition();
	}

	public double getRearRightPosition() {
		return rearRightTalon.getPosition();
	}

	public void resetDistance() {
		frontLeftTalon.setPosition(0);
		rearLeftTalon.setPosition(0);
		frontRightTalon.setPosition(0);
		rearRightTalon.setPosition(0);
	}

	public double getDistance() {
		return (frontLeftTalon.getPosition() + rearLeftTalon.getPosition() + frontRightTalon.getPosition()
				+ rearRightTalon.getPosition()) / 4;
	}

	private double angleToTurn, distanceToDrive;
	
	public void initTurn(double angle) {
		angleToTurn = angle;
		gyro.reset();
	}

	public boolean turn() {
		boolean retval = false;

		if (angleToTurn < 0) {
			if (gyro.getAngle() < angleToTurn) {
				retval = true;
				robotDrive.stopMotor();
			} else {
				mecanum(0, 0, -0.5, true);
			}
		} else {
			if(gyro.getAngle() > angleToTurn) {
				retval = true;
				robotDrive.stopMotor();
			} else {
				mecanum(0, 0, 0.5, true);
			}
		}

		return retval;
	}
	
	public void initDriveDistance(double distance) {
		distanceToDrive = distance;
		gyro.reset();
		resetDistance();
	}

	public boolean driveDistance() {
		boolean retval = false;
		
		if(distanceToDrive < 0) {
			if(getDistance() < distanceToDrive) {
				retval = true;
				robotDrive.stopMotor();
			} else {
				mecanum(0, -0.5, 0, true);
			}
		} else {
			if(getDistance() > distanceToDrive) {
				retval = true;
				robotDrive.stopMotor();
			} else {
				mecanum(0, 0.5, 0, true);
			}
		}
		
		return retval;
	}
}
