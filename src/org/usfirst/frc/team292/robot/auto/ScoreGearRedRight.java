package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearRedRight extends ScoreGear {

	public ScoreGearRedRight(Robot robot) {
		super(robot);
		driveOutDistance = kBoilerSideDriveOutDistance;
		turnAngle = kRightSideTurnAngle;
		driveToLiftDistance = kBoilerSideDriveToLiftDistance;
	}

}
