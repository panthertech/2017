package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearMiddle extends ScoreGear {
	public static double kDriveToLiftDistance = 50.0;

	public ScoreGearMiddle(Robot robot) {
		super(robot);
		driveToLiftDistance = kDriveToLiftDistance;
	}

}
