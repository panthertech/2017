package org.usfirst.frc.team292.robot;

import org.usfirst.frc.team292.robot.auto.AutonomousMode;

import edu.wpi.first.wpilibj.IterativeRobot;

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
	
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	/* The first entry in the autoModes array will be the default mode */
	final String[] autoModes = {defaultAuto, customAuto};
	
	public AutonomousMode auto;
	public Dashboard db;
	public Drive drive;
	public Climber climber;
	public Intake intake;
	public Shooter shooter;
	public GearSensor gearSensor;
	public OperatorInterface oi;
	public NavModule nav;
	public Camera gearCamera;
	public Camera boilerCamera;
	
	/* Variables for managing automatic gear placement */
	boolean placeGearInit, turnComplete, distanceComplete;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		nav = new NavModule();
		drive = new Drive(0, 1, 2, 3, nav);
		climber = new Climber(6);
		intake = new Intake(4);
		shooter = new Shooter(5, 6);
		gearSensor = new GearSensor(8);
		oi = new OperatorInterface();
		gearCamera = new GearCamera(kGearCameraName, kGearCameraId, nav, 3);
		boilerCamera = new BoilerCamera(kBoilerCameraName, kBoilerCameraId, nav, 2);

		db = new Dashboard(this);
		db.setAutoModes(autoModes);
		db.viewCamera(kGearCameraName);
		
		placeGearInit = false;
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
		case defaultAuto:
		default:
			auto = new AutonomousMode(this);
			break;
		}
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
			if(!placeGearInit) {
				initPlaceGear();
				placeGearInit = true;
			}
			gearCamera.enableProcessing();
			placeGear();
		} else {
			placeGearInit = false;
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
	
	public void initPlaceGear() {
		turnComplete = false;
	}
	
	/**
	 * This function automatically drives the robot to place a gear on the lift
	 */
	public boolean placeGear() {
		boolean retval = false;
		
		if(!turnComplete) {
			turnComplete = drive.turn(gearCamera.getTargetAngle());
			distanceComplete = false;
		} else {
			if(!distanceComplete) {
				drive.stop();
				//distanceComplete = drive.driveDistance(gearCamera.getTargetDistance());
			} else {
				drive.mecanum(0, 0.2, 0);
				retval = true;
			}
		}
		
		return retval;
	}
}
