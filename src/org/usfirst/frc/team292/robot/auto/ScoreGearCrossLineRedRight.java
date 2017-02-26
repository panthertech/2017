package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearCrossLineRedRight extends ScoreGearCrossLine {
	public static final double kDriveOutDistance = ScoreGearRedRight.kDriveOutDistance;
	public static final double kTurnAngle = ScoreGearRedRight.kTurnAngle;
	public static final double kDriveToLiftDistance = ScoreGearRedRight.kDriveToLiftDistance;

	public ScoreGearCrossLineRedRight(Robot robot) {
		super(robot);
		addCrossLineTarget(-12.0, 0.0);
		addCrossLineTarget(0.0, 60.0);
		addCrossLineTarget(48.0, 0.0);
	}

}
