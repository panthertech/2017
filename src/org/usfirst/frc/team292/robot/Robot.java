package org.usfirst.frc.team292.robot;

import org.usfirst.frc.team292.robot.auto.*;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private static final int kGearCameraId = 1;
	private static final String kGearCameraName = "Gear Camera";
	private static final int kBoilerCameraId = 0;
	private static final String kBoilerCameraName = "Boiler Camera";
	public static final double kRobotLength = 40;
	
	final String doNothingAuto = "Do Nothing";
	final String crossLineAuto = "Cross Line";
	final String scoreGearAuto = "Score Gear";
	final String scoreGearCrossLineAuto = "Score Gear & Cross Line";
	/* The first entry in the autoModes array will be the default mode */
	final String[] autoModes = {doNothingAuto, crossLineAuto, scoreGearAuto, scoreGearCrossLineAuto};
	
	public AutonomousMode auto;
	public Dashboard db;
	public Drive drive;
	public Climber climber;
	public Intake intake;
	public Shooter shooter;
	public GearSensor gearSensor;
	public OperatorInterface oi;
	public NavModule gyro;
	public Camera gearCamera;
	public Camera boilerCamera;
	
	/* Variables for managing automatic gear placement */
	enum PlaceGearStates { Init, TurnInit, Turning, DriveInit, Driving, Done}
	public PlaceGearStates placeGearState;
	public double placeGearLastAngle;
	public double placeGearLastUpdateTime;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		gyro = new NavModule();
		drive = new Drive(0, 1, 2, 3, gyro);
		climber = new Climber(6);
		intake = new Intake(4);
		shooter = new Shooter(5, 6);
		gearSensor = new GearSensor(8);
		oi = new OperatorInterface();
		gearCamera = new GearCamera(kGearCameraName, kGearCameraId, gyro, 3);
		boilerCamera = new BoilerCamera(kBoilerCameraName, kBoilerCameraId, gyro, 2);

		db = new Dashboard(this);
		db.setAutoModes(autoModes);
		db.viewCamera(kGearCameraName);
		
		placeGearState = PlaceGearStates.Init;
	}

	/**
	 * This function is called periodically all modes
	 */
	@Override
	public void robotPeriodic() {
		if(oi.viewGearCamera()) {
			db.viewCamera(kGearCameraName);
		}
		if(oi.viewBoilerCamera()) {
			db.viewCamera(kBoilerCameraName);
		}
	}

	/**
	 * This function is called when entering autonomous
	 */
	@Override
	public void autonomousInit() {
		switch (db.getSelectedAutoMode()) {
		case crossLineAuto:
			auto = new CrossLine(this);
			break;
		case scoreGearAuto:
			switch(db.getStartingPosition()) {
			case RedLeft:
				auto = new ScoreGearRedLeft(this);
				break;
			case BlueLeft:
				auto = new ScoreGearBlueLeft(this);
				break;
			case RedMiddle:
			case BlueMiddle:
				auto = new ScoreGearMiddle(this);
				break;
			case RedRight:
				auto = new ScoreGearRedRight(this);
				break;
			case BlueRight:
				auto = new ScoreGearBlueRight(this);
				break;
			default:
				break;
			}
			break;
		case scoreGearCrossLineAuto:
			switch(db.getStartingPosition()) {
			case RedMiddle:
			case BlueMiddle:
				auto = new ScoreGearCrossLineMiddle(this);
				break;
			default:
				break;
			}
			break;
		case doNothingAuto:
		default:
			auto = new DoNothing(this);
			break;
		}
		
		if(auto == null) auto = new DoNothing(this);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		auto.periodic();
	}

	/**
	 * This function is called when entering operator control
	 */
	@Override
	public void teleopInit() {
		oi.updateControllerTypes();
		shooter.disableShooter();
		climber.stop();
		intake.off();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		if (oi.placeGear()) {
			if(drive.pidIsEnabled()) {
				drive.resetPID();
			}
			gearCamera.enableProcessing();
			placeGear();
		} else {
			placeGearState = PlaceGearStates.Init;
			gearCamera.disableProcessing();
			drive.mecanum(oi.getDriveX(), oi.getDriveY(), oi.getDriveZ());
		}

		if (oi.climb()) {
			climber.climb();
		} else if (oi.descend()) {
			climber.descend();
		} else {
			climber.maintain();
		}

		if (oi.enableIntake()) {
			intake.on();
		} else if (oi.reverseIntake()) {
			intake.reverse();
		} else {
			intake.maintain();
		}

		if (oi.shooterEnable() || oi.shoot()) {
			shooter.enableShooter();
			intake.off();
		}
		if (oi.shooterDisable()) {
			shooter.disableShooter();
		}
		shooter.shoot(oi.shoot());
	}

	/**
	 * This function is called when entering disabled
	 */
	@Override
	public void disabledInit() {

	}

	/**
	 * This function is called periodically during disabled
	 */
	@Override
	public void disabledPeriodic() {

	}
	
	/**
	 * This function automatically drives the robot to place a gear on the lift
	 */
	public boolean placeGear() {
		boolean retval = false;
		
		/* Check for a large change in the target angle */
		if(Math.abs(placeGearLastAngle - gearCamera.getTargetAngle()) > 5.0) {
			placeGearState = PlaceGearStates.Init;
		}
		placeGearLastAngle = gearCamera.getTargetAngle();
		
		if(Timer.getFPGATimestamp() - placeGearLastUpdateTime > 0.5) {
			drive.setTurnAngle(gearCamera.getTargetAngle());
			placeGearLastUpdateTime = Timer.getFPGATimestamp();
			if(placeGearState == PlaceGearStates.Driving) {
				drive.setDriveDistance(gearCamera.getTargetDistance() + 6.0);
			}
		}
		
		switch(placeGearState) {
		case Init:
		case TurnInit:
			drive.driveDistance(0.0, gearCamera.getTargetAngle(), false);
			placeGearState = PlaceGearStates.Turning;
		case Turning:
			if(drive.onTarget()) {
				placeGearState = PlaceGearStates.DriveInit;
			}
			break;
		case DriveInit:
			drive.driveDistance(gearCamera.getTargetDistance(), gearCamera.getTargetAngle(), false);
			placeGearState = PlaceGearStates.Driving;
		case Driving:
			if(drive.onTarget()) {
				placeGearState = PlaceGearStates.Done;
			}
			break;
		case Done:
			retval = true;
			break;
		default:
			break;
		}
		
		return retval;
	}
}
