package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearBlueLeft extends ScoreGear {

	public ScoreGearBlueLeft(Robot robot) {
		super(robot);
		driveOutDistance = kBoilerSideDriveOutDistance;
		turnAngle = kLeftSideTurnAngle;
		driveToLiftDistance = kBoilerSideDriveToLiftDistance;
	}

}
