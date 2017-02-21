package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;

public class Drive {
	private static final double kDriveP = 0.75;
	private static final double kDriveI = 0.01;
	private static final double kDriveD = 0.00001;
	private static final double kDriveTolerance = 0.1;
	private static final double kTurnP = 0.01;
	private static final double kTurnI = 0.001;
	private static final double kTurnD = 0.001;
	private static final double kTurnTolerance = 0.5;
	
	private static final double kDistanceToRotationRatio = 8.0 * Math.PI;
	
	private RobotDrive robotDrive;
	private CANTalon frontLeftTalon;
	private CANTalon frontRightTalon;
	private CANTalon rearLeftTalon;
	private CANTalon rearRightTalon;
	private NavModule gyro;
	private PIDController drivePID;
	private PIDController turnPID;
	private double drivePIDOutputValue;
	private double turnPIDOutputValue;

	public Drive(int frontLeftPort, int rearLeftPort, int frontRightPort, int rearRightPort, NavModule gyro) {
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

		drivePID = new PIDController(kDriveP, kDriveI, kDriveD, new DrivePIDSource(), new DrivePIDOutput());
		drivePID.setAbsoluteTolerance(kDriveTolerance);
		turnPID = new PIDController(kTurnP, kTurnI, kTurnD, gyro, new TurnPIDOutput());
		turnPID.setAbsoluteTolerance(kTurnTolerance);
	}

	public void mecanum(double x, double y, double z) {
		drivePID.disable();
		turnPID.disable();
		robotDrive.mecanumDrive_Cartesian(x, y, z, 0);
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
				+ rearRightTalon.getPosition()) / 4.0;
	}
	
	private void resetPID() {
		drivePID.reset();
		turnPID.reset();
		drivePIDOutputValue = 0.0;
		turnPIDOutputValue = 0.0;
	}
	
	public void turn(double angle) {
		resetPID();
		turnPID.setSetpoint(angle);
		turnPID.enable();
	}
	
	public void driveDistance(double distance) {
		driveDistance(distance, gyro.getAngle());
	}
	
	public void driveDistance(double distance, double angle) {
		resetPID();
		drivePID.setSetpoint(distance / kDistanceToRotationRatio);
		turnPID.setSetpoint(angle);
		drivePID.enable();
		turnPID.enable();
	}
	
	public boolean onTarget() {
		return ((!drivePID.isEnabled() || drivePID.onTarget()) && (!turnPID.isEnabled() || turnPID.onTarget()));
	}

	private class DrivePIDOutput implements PIDOutput {
		@Override
		public void pidWrite(double output) {
			drivePIDOutputValue = -output;
			robotDrive.mecanumDrive_Cartesian(0, drivePIDOutputValue, turnPIDOutputValue, 0);
		}
	}

	private class TurnPIDOutput implements PIDOutput {
		@Override
		public void pidWrite(double output) {
			turnPIDOutputValue = output;
			robotDrive.mecanumDrive_Cartesian(0, drivePIDOutputValue, turnPIDOutputValue, 0);
		}
	}
	
	private class DrivePIDSource implements PIDSource {
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
			
		}

		@Override
		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kDisplacement;
		}

		@Override
		public double pidGet() {
			return getDistance();
		}
	}
}
