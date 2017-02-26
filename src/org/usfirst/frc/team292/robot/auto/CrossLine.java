package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.Robot;

public class CrossLine extends AutonomousMode {

	public CrossLine(Robot robot) {
		super(robot);
		robot.drive.driveDistance(200);
	}
	
	public void periodic () {
		
	}
}
