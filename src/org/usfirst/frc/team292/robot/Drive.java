package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;

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
		frontLeftTalon.setInverted(false);
		frontLeftTalon.reverseSensor(true);
		
		rearLeftTalon = new CANTalon(rearLeftPort);
		rearLeftTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		rearLeftTalon.setInverted(false);
		rearLeftTalon.reverseSensor(true);
		
		frontRightTalon = new CANTalon(frontRightPort);
		frontRightTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		frontRightTalon.setInverted(true);
		frontRightTalon.reverseSensor(false);
		
		rearRightTalon = new CANTalon(rearRightPort);
		rearRightTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		rearRightTalon.setInverted(true);
		rearRightTalon.reverseSensor(false);
		
		robotDrive = new RobotDrive(frontLeftTalon, rearLeftTalon, frontRightTalon, rearRightTalon);
		
		this.gyro = gyro;
	}

	public void mecanum(double x, double y, double z) {
		mecanum(x, y, z, false);
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

	public boolean turn(double angle) {
		boolean retval = false;
		
		if(angleToTurn != angle) {
			gyro.reset();
			angleToTurn = angle;
		}

		if (angleToTurn < 0) {
			if (gyro.getAngle() < angleToTurn) {
				retval = true;
				robotDrive.stopMotor();
			} else {
				mecanum(0, 0, -0.25);
			}
		} else {
			if(gyro.getAngle() > angleToTurn) {
				retval = true;
				robotDrive.stopMotor();
			} else {
				mecanum(0, 0, 0.25);
			}
		}

		return retval;
	}
	
	public boolean driveDistance(double distance) {
		boolean retval = false;
		
		if(distanceToDrive != distance)
		{
			gyro.reset();
			resetDistance();
			distanceToDrive = distance;
		}
		
		if(distanceToDrive < 0) {
			if(getDistance() < distanceToDrive) {
				retval = true;
				robotDrive.stopMotor();
			} else {
				mecanum(0, -0.5, 0);
			}
		} else {
			if(getDistance() > distanceToDrive) {
				retval = true;
				robotDrive.stopMotor();
			} else {
				mecanum(0, 0.5, 0);
			}
		}
		
		return retval;
	}
}
