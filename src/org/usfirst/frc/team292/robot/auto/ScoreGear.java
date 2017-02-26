package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

import edu.wpi.first.wpilibj.Timer;

public abstract class ScoreGear extends AutonomousMode {
	public static final double kDriveOutDistance = 0.0;
	public static final double kTurnAngle = 0.0;
	public static final double kDriveToLiftDistance = 0.0;
	public static final double kMinOnTargetTime = 0.25;
	
	private ScoreGearStates scoreGearState;
	private double lastOffTargetTime;
	
	private enum ScoreGearStates {
		Init,
		DriveOut,
		Turn,
		DriveToLift,
		ScoreGear,
		Done
	}
	
	public ScoreGear(Robot robot) {
		super(robot);
		robot.gearCamera.enableProcessing();
		scoreGearState = ScoreGearStates.Init;
		lastOffTargetTime = Timer.getFPGATimestamp();
	}

	@Override
	public void periodic() {
		scoreGear();
	}
	
	public boolean onTarget() {
		boolean onTarget = false;
		
		if(robot.drive.onTarget()) {
			if(Timer.getFPGATimestamp() - lastOffTargetTime > kMinOnTargetTime) {
				onTarget = true;
			}
		} else {
			lastOffTargetTime = Timer.getFPGATimestamp();
		}
		
		return onTarget;
	}
	
	public boolean scoreGear() {
		boolean retval = false;
		
		switch(scoreGearState) {
		case Init:
			robot.drive.driveDistance(kDriveOutDistance);
			scoreGearState = ScoreGearStates.DriveOut;
			break;
		case DriveOut:
			if(onTarget()) {
				robot.drive.turn(kTurnAngle, true);
				scoreGearState = ScoreGearStates.Turn;
			}
			break;
		case Turn:
			if(onTarget()) {
				robot.drive.driveDistance(kDriveToLiftDistance);
				scoreGearState = ScoreGearStates.DriveToLift;
			}
			break;
		case DriveToLift:
			if(onTarget()) {
				scoreGearState = ScoreGearStates.ScoreGear;
			}
			break;
		case ScoreGear:
			if(robot.placeGear() && !robot.gearSensor.gearPresent()) {
				scoreGearState = ScoreGearStates.Done;
			}
			break;
		case Done:
		default:
			retval = true;
			break;
		}
		
		return retval;
	}

}
