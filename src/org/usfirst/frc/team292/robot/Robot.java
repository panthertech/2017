package org.usfirst.frc.team292.robot;

import org.usfirst.frc.team292.robot.auto.AutonomousMode;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	AutonomousMode auto;
	public Drive drive;
	public Climber climber;
	public Intake intake;
	public Shooter shooter;
	public GearSensor gearSensor;
	public OperatorInterface oi;
	public NavModule nav;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);

		nav = new NavModule();
		drive = new Drive(0, 1, 2, 3, nav);
		climber = new Climber(6);
		intake = new Intake(4);
		shooter = new Shooter(5, 6);
		gearSensor = new GearSensor(8);
		oi = new OperatorInterface();
	}

	/**
	 * This function is called periodically all modes
	 */
	@Override
	public void robotPeriodic() {

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);

		switch (autoSelected) {
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

		SmartDashboard.putBoolean("Gear Sensor", gearSensor.gearPresent());
		SmartDashboard.putNumber("Shooter Speed", shooter.getShooterSpeed());
		SmartDashboard.putNumber("Shooter %", shooter.getShooterPercentVbus());
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

	public void placeGear() {
		drive.mecanum(0, 0, 0);
	}
}
