package org.usfirst.frc.team292.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
	private Thread thread;
	private Robot robot;
	private String[] autoModes;
	private String defaultMode;
	private DriverStation ds;
	private SmartDashboard db;

	public enum StartingPosition {
		Left, Middle, Right;
	}

	public Dashboard(Robot robot) {
		this.robot = robot;
		ds = DriverStation.getInstance();
		db = new SmartDashboard();
		thread = new DashboardThread();
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}
	
	private class DashboardThread extends Thread {
		public DashboardThread() {
			super("Dashboard Thread");
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					SmartDashboard.putBoolean("Gear Sensor", robot.gearSensor.gearPresent());
					SmartDashboard.putNumber("Shooter Speed", robot.shooter.getShooterSpeed());
					SmartDashboard.putNumber("Shooter %", robot.shooter.getShooterPercent());
					SmartDashboard.putString("Driver Controller Type", robot.oi.getDriverControllerType().toString());
					SmartDashboard.putString("Operator Controller Type", robot.oi.getOperatorControllerType().toString());
					SmartDashboard.putBoolean("Intake Enabled", robot.intake.getIntakeEnabled());
					SmartDashboard.putBoolean("Climber Enabled", robot.climber.getClimberEnabled());
					SmartDashboard.putNumber("Wheel Position Front Left", robot.drive.getFrontLeftPosition());
					SmartDashboard.putNumber("Wheel Position Rear Left", robot.drive.getRearLeftPosition());
					SmartDashboard.putNumber("Wheel Position Front Right", robot.drive.getFrontRightPosition());
					SmartDashboard.putNumber("Wheel Position Rear Right", robot.drive.getRearRightPosition());
				} catch (NullPointerException e) {
                    DriverStation.reportError("Dashboard Error: " + e.toString(), true);
				}

				try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    DriverStation.reportError("Dashboard Error: " + e.toString(), true);
                }
			}
		}
	}

	public void setAutoModes(String[] modes) {
		defaultMode = modes[0];
		autoModes = modes;
		db.putStringArray("Auto choices", autoModes);
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

	public void viewCamera(String deviceName) {
		SmartDashboard.putString("Selected Camera Name", deviceName);
	}
}
