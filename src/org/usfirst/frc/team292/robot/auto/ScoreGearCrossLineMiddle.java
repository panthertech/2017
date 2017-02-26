package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

public class ScoreGearCrossLineMiddle extends ScoreGearCrossLine {
	public static final double kDriveToLiftDistance = ScoreGearMiddle.kDriveToLiftDistance;

	public ScoreGearCrossLineMiddle(Robot robot) {
		super(robot);
		addCrossLineTarget(-12.0, 0.0);
		addCrossLineTarget(0.0, 90.0);
		addCrossLineTarget(48.0, 0.0);
		addCrossLineTarget(0.0, -90.0);
		addCrossLineTarget(96.0, 0.0);
	}

}
