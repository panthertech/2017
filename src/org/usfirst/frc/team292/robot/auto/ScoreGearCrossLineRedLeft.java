package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearCrossLineRedLeft extends ScoreGearCrossLine {
	public static final double kDriveOutDistance = ScoreGearRedLeft.kDriveOutDistance;
	public static final double kTurnAngle = ScoreGearRedLeft.kTurnAngle;
	public static final double kDriveToLiftDistance = ScoreGearRedLeft.kDriveToLiftDistance;

	public ScoreGearCrossLineRedLeft(Robot robot) {
		super(robot);
		addCrossLineTarget(-12.0, 0.0);
		addCrossLineTarget(0.0, -60.0);
		addCrossLineTarget(48.0, 0.0);
	}

}
