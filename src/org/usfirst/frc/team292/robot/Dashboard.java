package org.usfirst.frc.team292.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard extends Thread {
	Robot robot;
	String autoSelected;
	SendableChooser<String> chooser;
	String defaultMode;
	DriverStation ds;
	SmartDashboard db;

	public enum StartingPosition {
		Left, Middle, Right;
	}

	public Dashboard(Robot robot) {
		super();
		this.robot = robot;
		this.setPriority(MIN_PRIORITY);
		this.start();
		chooser = new SendableChooser<>();
		ds = DriverStation.getInstance();
		db = new SmartDashboard();
	}

	@Override
	public void run() {
		SmartDashboard.putBoolean("Gear Sensor", robot.gearSensor.gearPresent());
		SmartDashboard.putNumber("Shooter Speed", robot.shooter.getShooterSpeed());
		SmartDashboard.putNumber("Shooter %", robot.shooter.getShooterPercentVbus());
		SmartDashboard.putString("Driver Controller Type", robot.oi.getDriverControllerType().toString());
		SmartDashboard.putString("Operator Controller Type", robot.oi.getOperatorControllerType().toString());
	}

	public void setAutoModes(String[] modes) {
		defaultMode = modes[0];
		chooser.addDefault(defaultMode, defaultMode);
		for (int i = 1; i < modes.length; i++) {
			chooser.addObject(modes[i], modes[i]);
		}
		SmartDashboard.putData("Auto choices", chooser);
	}

	public String getSelectedAutoMode() {
		return SmartDashboard.getString("Auto Selector", defaultMode);
	}

	public StartingPosition getStartingPosition() {
		StartingPosition pos;
		switch ((int) SmartDashboard.getNumber("Starting Position", 1)) {
		case 0:
			pos = StartingPosition.Left;
			break;
		case 2:
			pos = StartingPosition.Right;
			break;
		case 1:
		default:
			pos = StartingPosition.Middle;
			break;
		}
		return pos;
	}
	
	public Alliance getAlliance() {
		return ds.getAlliance();
	}
}
