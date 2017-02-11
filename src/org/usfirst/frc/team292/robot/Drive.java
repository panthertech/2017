package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
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
		frontRightTalon = new CANTalon(rearLeftPort);
		frontRightTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		rearLeftTalon = new CANTalon(frontRightPort);
		rearLeftTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		rearRightTalon = new CANTalon(rearRightPort);
		rearRightTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		robotDrive = new RobotDrive(frontLeftTalon, rearLeftTalon, frontRightTalon, rearRightTalon);
		robotDrive.setInvertedMotor(MotorType.kFrontRight, false);
		robotDrive.setInvertedMotor(MotorType.kRearRight, true);
		robotDrive.setInvertedMotor(MotorType.kFrontLeft, false);
		robotDrive.setInvertedMotor(MotorType.kRearLeft, true);
		robotDrive.setExpiration(0.1);
		this.gyro = gyro;
	}

	public void mecanum(double x, double y, double z) {
		robotDrive.mecanumDrive_Cartesian(x, y, z, 0);
	}

	public double getFrontleftPosition() {
		return frontLeftTalon.getPosition();
	}

	public double getRearleftPosition() {
		return rearLeftTalon.getPosition();
	}

	public double getFrontrightPositon() {
		return frontRightTalon.getPosition();
	}

	public double getRearrightPosition() {
		return rearRightTalon.getPosition();

	}
}
