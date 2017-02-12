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
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	/* The first entry in the autoModes array will be the default mode */
	final String[] autoModes = {defaultAuto, customAuto};
	
	AutonomousMode auto;
	Dashboard db;
	public Drive drive;
	public Climber climber;
	public Intake intake;
	public Shooter shooter;
	public GearSensor gearSensor;
	public OperatorInterface oi;
	public NavModule nav;
	public GearCamera gearCamera;
	public BoilerCamera boilerCamera;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		db = new Dashboard(this);
		db.setAutoModes(autoModes);
		
		nav = new NavModule();
		drive = new Drive(0, 1, 2, 3, nav);
		climber = new Climber(6);
		intake = new Intake(4);
		shooter = new Shooter(5, 6);
		gearSensor = new GearSensor(8);
		oi = new OperatorInterface();
		gearCamera = new GearCamera("cam0", 0, nav);
		boilerCamera = new BoilerCamera("cam1", 1, nav);
	}

	/**
	 * This function is called periodically all modes
	 */
	@Override
	public void robotPeriodic() {

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

	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		if (oi.placeGear()) {
			placeGear();
		} else {
			drive.mecanum(oi.getDriveX(), oi.getDriveY(), oi.getDriveZ());
		}

		if (oi.climb()) {
			climber.climb();
		} else {
			climber.stop();
		}

		if (oi.intake()) {
			intake.on();
		} else if (oi.reverseIntake()) {
			intake.reverse();
		} else {
			intake.off();
		}

		shooter.shoot(oi.shoot());

		if (oi.shootEnable()) {
			shooter.enableShooter();
		} else {
			shooter.disableShooter();
		}
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
	public void placeGear() {
		drive.mecanum(0, 0, 0);
	}
}
