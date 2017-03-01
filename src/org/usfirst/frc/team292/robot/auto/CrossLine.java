package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.Robot;

public class CrossLine extends AutonomousMode {

	public CrossLine(Robot robot) {
		super(robot);
		robot.drive.driveDistance(100);
	}
	
	public void periodic () {
		if(robot.drive.pidIsEnabled()) {
			if(robot.drive.onTargetDistance()) {
				robot.drive.resetPID();
			}
		} else {
			robot.drive.stop();
		}
	}
}
