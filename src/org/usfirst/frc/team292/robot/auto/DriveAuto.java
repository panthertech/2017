package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.Robot;

public class DriveAuto extends AutonomousMode {

	public DriveAuto(Robot robot) {
		super(robot);
		robot.drive.initDriveDistance(5);
	}
	
	public void periodic () {
		robot.drive.driveDistance();
	}
	

}
