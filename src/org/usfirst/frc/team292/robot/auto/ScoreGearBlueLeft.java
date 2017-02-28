package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearBlueLeft extends ScoreGear {
	public static final double kDriveOutDistance = 60.0;
	public static final double kTurnAngle = 55.0;
	public static final double kDriveToLiftDistance = 25.0;

	public ScoreGearBlueLeft(Robot robot) {
		super(robot);
		driveOutDistance = kDriveOutDistance;
		turnAngle = kTurnAngle;
		driveToLiftDistance = kDriveToLiftDistance;
	}

}
