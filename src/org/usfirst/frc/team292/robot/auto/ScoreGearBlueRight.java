package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearBlueRight extends ScoreGear {
	
	public ScoreGearBlueRight(Robot robot) {
		super(robot);
		driveOutDistance = kLoadingSideDriveOutDistance;
		turnAngle = kRightSideTurnAngle;
		driveToLiftDistance = kLoadingSideDriveToLiftDistance;
	}

}
