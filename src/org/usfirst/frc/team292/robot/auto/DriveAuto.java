package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.Robot;

public class DriveAuto extends AutonomousMode {

	public DriveAuto(Robot robot) {
		super(robot);
	}
	public void periodic () {
		if (robot.drive.getFrontleftPosition() >= 5) {
			robot.drive.mecanum(0, 0, 0);
		} else {
			robot.drive.mecanum(0, .5, 0);
		}
	}
	

}
