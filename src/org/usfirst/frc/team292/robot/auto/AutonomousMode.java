package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public abstract class AutonomousMode {
	Robot robot;
	
	public AutonomousMode(Robot robot) {
		this.robot = robot;
		robot.drive.resetDistance();
		robot.gyro.reset();
	}
	
	public abstract void periodic();
	
}
