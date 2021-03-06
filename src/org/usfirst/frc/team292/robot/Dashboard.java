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
		RedLeft, RedMiddle, RedRight, BlueLeft, BlueMiddle, BlueRight, Invalid;
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
					SmartDashboard.putString("Driver Controller Type", robot.oi.getDriverControllerType().toString());
					SmartDashboard.putString("Operator Controller Type", robot.oi.getOperatorControllerType().toString());
					SmartDashboard.putBoolean("Climber Enabled", robot.climber.getClimberEnabled());
					SmartDashboard.putNumber("Wheel Position Front Left", robot.drive.getFrontLeftPosition());
					SmartDashboard.putNumber("Wheel Position Rear Left", robot.drive.getRearLeftPosition());
					SmartDashboard.putNumber("Wheel Position Front Right", robot.drive.getFrontRightPosition());
					SmartDashboard.putNumber("Wheel Position Rear Right", robot.drive.getRearRightPosition());
					SmartDashboard.putNumber("Gyro Angle", robot.gyro.getAngle());
					SmartDashboard.putNumber("Camera Target Angle", robot.gearCamera.getTargetAngle());
					SmartDashboard.putNumber("Camera Target Distance", robot.gearCamera.getTargetDistance());
					SmartDashboard.putString("Place Gear State", robot.placeGearState.toString());
					SmartDashboard.putNumber("Drive Distance", robot.drive.getDistance());
					SmartDashboard.putNumber("Place Gear Angle", robot.placeGearAngle);
					SmartDashboard.putNumber("Place Gear Distance", robot.placeGearDistance);
					SmartDashboard.putString("Gyro Type", robot.gyro.getSensorType());
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
		boolean validMode = false;
		String selectedMode = SmartDashboard.getString("Auto Selector", defaultMode);
		
		for(int i = 0; i < autoModes.length; i++) {
			if(autoModes[i].equals(selectedMode)) {
				validMode = true;
				break;
			}
		}
		
		if(!validMode) {
			selectedMode = defaultMode;
		}
		
		return selectedMode;
	}

	public StartingPosition getStartingPosition() {
		StartingPosition pos;
		switch ((int) SmartDashboard.getNumber("Starting Position", 1)) {
		case 0:
			switch(getAlliance()) {
			case Red:
				pos = StartingPosition.RedLeft;
				break;
			case Blue:
				pos = StartingPosition.BlueLeft;
				break;
			case Invalid:
			default:
				pos = StartingPosition.Invalid;
				break;
			}
			break;
		case 1:
			switch(getAlliance()) {
			case Red:
				pos = StartingPosition.RedMiddle;
				break;
			case Blue:
				pos = StartingPosition.BlueMiddle;
				break;
			case Invalid:
			default:
				pos = StartingPosition.Invalid;
				break;
			}
			break;
		case 2:
			switch(getAlliance()) {
			case Red:
				pos = StartingPosition.RedRight;
				break;
			case Blue:
				pos = StartingPosition.BlueRight;
				break;
			case Invalid:
			default:
				pos = StartingPosition.Invalid;
				break;
			}
			break;
		default:
			pos = StartingPosition.Invalid;
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
