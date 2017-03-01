package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class DoNothing extends AutonomousMode {
	
	public DoNothing(Robot robot) {
		super(robot);
	}

	@Override
	public void periodic() {
		robot.drive.stop();
	}
	
}
