package org.usfirst.frc.team292.robot.auto;

import org.usfirst.frc.team292.robot.*;

import edu.wpi.first.wpilibj.Timer;

public abstract class ScoreGear extends AutonomousMode {
	public static final double kMiddleDriveToLiftDistance = 70.0;
	
	public static final double kBoilerSideDriveOutDistance = 60.0;
	public static final double kBoilerSideDriveToLiftDistance = 25.0;
	
	public static final double kLoadingSideDriveOutDistance = 55.0;
	public static final double kLoadingSideDriveToLiftDistance = 40.0;
		
	public static final double kLeftSideTurnAngle = 55.0;
	public static final double kRightSideTurnAngle = -kLeftSideTurnAngle;
	
	public static final double kMinOnTargetTime = 0.25;
	
	public double driveOutDistance;
	public double turnAngle;
	public double driveToLiftDistance;
	
	private ScoreGearStates scoreGearState;
	private double lastOffTargetAngleTime;
	private double lastOffTargetDistanceTime;
	
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
		lastOffTargetAngleTime = Timer.getFPGATimestamp();
		lastOffTargetDistanceTime = Timer.getFPGATimestamp();
	}

	@Override
	public void periodic() {
		scoreGear();
	}
	
	public boolean onTargetAngle() {
		boolean onTarget = false;
		
		if(robot.drive.onTargetAngle()) {
			if(Timer.getFPGATimestamp() - lastOffTargetAngleTime > kMinOnTargetTime) {
				onTarget = true;
			}
		} else {
			lastOffTargetAngleTime = Timer.getFPGATimestamp();
		}
		
		return onTarget;
	}
	
	public boolean onTargetDistance() {
		boolean onTarget = false;
		
		if(robot.drive.onTargetDistance()) {
			if(Timer.getFPGATimestamp() - lastOffTargetDistanceTime > kMinOnTargetTime) {
				onTarget = true;
			}
		} else {
			lastOffTargetDistanceTime = Timer.getFPGATimestamp();
		}
		
		return onTarget;
	}
	
	public boolean scoreGear() {
		boolean retval = false;
		
		switch(scoreGearState) {
		case Init:
			robot.drive.driveDistance(driveOutDistance);
			scoreGearState = ScoreGearStates.DriveOut;
			break;
		case DriveOut:
			if(onTargetDistance()) {
				robot.drive.turn(turnAngle, true);
				scoreGearState = ScoreGearStates.Turn;
			}
			break;
		case Turn:
			if(onTargetAngle()) {
				robot.drive.driveDistance(driveToLiftDistance);
				scoreGearState = ScoreGearStates.DriveToLift;
			}
			break;
		case DriveToLift:
			if(onTargetDistance()) {
				scoreGearState = ScoreGearStates.ScoreGear;
				robot.placeGearInit();
			}
			break;
		case ScoreGear:
//			if(robot.placeGear() && !robot.gearSensor.gearPresent()) {
//				scoreGearState = ScoreGearStates.Done;
//			}
			scoreGearState = ScoreGearStates.Done;
			break;
		case Done:
		default:
			if(robot.drive.pidIsEnabled()) {
				robot.drive.resetPID();
			}
			robot.drive.mecanum(0.0, -0.1, 0.0);
			retval = true;
			break;
		}
		
		return retval;
	}

}
