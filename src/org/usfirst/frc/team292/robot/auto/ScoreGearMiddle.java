package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearMiddle extends ScoreGear {

	public ScoreGearMiddle(Robot robot) {
		super(robot);
		driveOutDistance = 0.0;
		turnAngle = 0.0;
		driveToLiftDistance = kMiddleDriveToLiftDistance;
	}

}
