package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearCrossLineBlueRight extends ScoreGearCrossLine {
	public static final double kDriveOutDistance = ScoreGearBlueRight.kDriveOutDistance;
	public static final double kTurnAngle = ScoreGearBlueRight.kTurnAngle;
	public static final double kDriveToLiftDistance = ScoreGearBlueRight.kDriveToLiftDistance;

	public ScoreGearCrossLineBlueRight(Robot robot) {
		super(robot);
		addCrossLineTarget(-12.0, 0.0);
		addCrossLineTarget(0.0, 60.0);
		addCrossLineTarget(48.0, 0.0);
	}

}
