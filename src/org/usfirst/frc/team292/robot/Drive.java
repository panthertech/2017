package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;

public class Drive {
	RobotDrive robotDrive;
	
	public Drive (int frontLeftPort, int rearLeftPort, int frontRightPort, int rearRightPort){
		CANTalon fronLeftTalon = new CANTalon (frontLeftPort);
		CANTalon rearLeftTalon = new CANTalon (rearLeftPort);
		CANTalon frontRightTalon = new CANTalon (frontRightPort);
		CANTalon rearRightTalon = new CANTalon (rearRightPort);
		robotDrive = new RobotDrive(fronLeftTalon, rearLeftTalon, frontRightTalon, rearRightTalon);
		robotDrive.setInvertedMotor(MotorType.kFrontRight, true); // invert the
																	// left side
																	// motors
		robotDrive.setInvertedMotor(MotorType.kRearRight, true); // you may need
																// to change or
																// remove this
																// to match your
																// robot
		robotDrive.setExpiration(0.1);
	}
	public void mecanum (double x, double y, double z){
		robotDrive.mecanumDrive_Cartesian(x, y, z, 0);
	}
	public boolean driveDistance (double xdist, double ydist){
		return false;
	}
	public boolean turn (double degrees){
		return false;
	}

}
