package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.Robot;

public class DriveAuto extends AutonomousMode {

	public DriveAuto(Robot robot) {
		super(robot);
		robot.drive.resetDistance();
		robot.gyro.reset();
		robot.drive.driveDistance(12, 0);
	}
	
	public void periodic () {
		
	}
}
