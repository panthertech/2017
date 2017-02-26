package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearCrossLineBlueLeft extends ScoreGearCrossLine {
	public static final double kDriveOutDistance = ScoreGearBlueLeft.kDriveOutDistance;
	public static final double kTurnAngle = ScoreGearBlueLeft.kTurnAngle;
	public static final double kDriveToLiftDistance = ScoreGearBlueLeft.kDriveToLiftDistance;

	public ScoreGearCrossLineBlueLeft(Robot robot) {
		super(robot);
		addCrossLineTarget(-12.0, 0.0);
		addCrossLineTarget(0.0, -60.0);
		addCrossLineTarget(48.0, 0.0);
	}

}
