package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.Robot;

public class DriveAuto extends AutonomousMode {

	public DriveAuto(Robot robot) {
		super(robot);
	}
	
	public void periodic () {
		robot.drive.driveDistance(5);
	}
}
