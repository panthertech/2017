package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearRedLeft extends ScoreGear {

	public ScoreGearRedLeft(Robot robot) {
		super(robot);
		driveOutDistance = kLoadingSideDriveOutDistance;
		turnAngle = kLeftSideTurnAngle;
		driveToLiftDistance = kLoadingSideDriveToLiftDistance;
	}

}
