package org.usfirst.frc.team292.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard extends Thread {
	Robot robot;
	String autoSelected;
	SendableChooser<String> chooser;
	String defaultMode;

	public Dashboard(Robot robot) {
		super();
		this.robot = robot;
		this.setPriority(MIN_PRIORITY);
		this.start();
		chooser = new SendableChooser<>();
	}

	@Override
	public void run() {
		SmartDashboard.putBoolean("Gear Sensor", robot.gearSensor.gearPresent());
		SmartDashboard.putNumber("Shooter Speed", robot.shooter.getShooterSpeed());
		SmartDashboard.putNumber("Shooter %", robot.shooter.getShooterPercentVbus());
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
}
