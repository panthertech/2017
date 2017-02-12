package org.usfirst.frc.team292.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;

public class OperatorInterface {
	private final int kDriverJoystickPort = 0;
	private final int kDriverXboxPort = 1;
	private final int kOperatorJoystickPort = 2;
	private final int kOperatorXboxPort = 3;

	private DriverStation ds;
	private Joystick driverJoystick;
	private XboxController driverXbox;
	private Joystick operatorJoystick;
	private XboxController operatorXbox;

	private ControllerType driverControllerType;
	private ControllerType operatorControllerType;

	public enum ControllerType {
		Joystick, XboxController, Invalid
	}

	public OperatorInterface() {
		ds = DriverStation.getInstance();

		driverJoystick = new Joystick(kDriverJoystickPort);
		driverXbox = new XboxController(kDriverXboxPort);
		operatorJoystick = new Joystick(kOperatorJoystickPort);
		operatorXbox = new XboxController(kOperatorXboxPort);

		updateControllerTypes();
	}

	/**
	 * Updates driver and operator controller types based on what is detected to
	 * be plugged in. For safety reasons, joysticks have priority over Xbox
	 * controllers.
	 */
	public void updateControllerTypes() {
		/*
		 * Update driver controller type
		 */
		if (ds.getJoystickType(kDriverJoystickPort) >= 0) {
			driverControllerType = ControllerType.Joystick;
		} else if (ds.getJoystickType(kDriverXboxPort) >= 0) {
			driverControllerType = ControllerType.XboxController;
		} else {
			driverControllerType = ControllerType.Invalid;
		}

		/*
		 * Update operator controller type
		 */
		if (ds.getJoystickType(kOperatorJoystickPort) >= 0) {
			operatorControllerType = ControllerType.Joystick;
		} else if (ds.getJoystickType(kOperatorXboxPort) >= 0) {
			operatorControllerType = ControllerType.XboxController;
		} else {
			operatorControllerType = ControllerType.Invalid;
		}
	}

	public ControllerType getDriverControllerType() {
		return driverControllerType;
	}

	public ControllerType getOperatorControllerType() {
		return operatorControllerType;
	}

	public double getDriveX() {
		double retval;

		switch (driverControllerType) {
		case Joystick:
			retval = applyDeadbandJoystick(driverJoystick.getX());
			break;
		case XboxController:
			retval = applyDeadbandXbox(driverXbox.getX(Hand.kLeft));
			break;
		case Invalid:
		default:
			retval = 0;
			break;
		}

		return retval;
	}

	public double getDriveY() {
		double retval;

		switch (driverControllerType) {
		case Joystick:
			retval = applyDeadbandJoystick(driverJoystick.getY());
			break;
		case XboxController:
			retval = applyDeadbandXbox(driverXbox.getY(Hand.kLeft));
			break;
		case Invalid:
		default:
			retval = 0;
			break;
		}

		return retval;
	}

	public double getDriveZ() {
		double retval;

		switch (driverControllerType) {
		case Joystick:
			retval = applyDeadbandJoystick(driverJoystick.getZ());
			break;
		case XboxController:
			retval = applyDeadbandXbox(driverXbox.getX(Hand.kRight));
			break;
		case Invalid:
		default:
			retval = 0;
			break;
		}

		return retval;
	}

	private double applyDeadbandJoystick(double x) {
		return x;
	}

	private double applyDeadbandXbox(double x) {
		double deadband = 0.1;
		if (x >= deadband)
			x = 1 / (1 - deadband) * (x - deadband);
		else if (x <= -deadband)
			x = 1 / (1 - deadband) * (x + deadband);
		else
			x = 0;
		return x;
	}

	public boolean shoot() {
		boolean retval;
		
		switch(operatorControllerType) {
		case Joystick:
			retval = operatorJoystick.getTrigger();
			break;
		case XboxController:
			retval = (operatorXbox.getTriggerAxis(Hand.kRight) > 0.5) || (operatorXbox.getTriggerAxis(Hand.kLeft) > 0.5);
			break;
		case Invalid:
		default:
			retval = false;
			break;
		}
		
		return retval;
	}

	public boolean shooterEnable() {
		boolean retval;
		
		switch(operatorControllerType) {
		case Joystick:
			retval = operatorJoystick.getRawButton(3);
			break;
		case XboxController:
			retval = operatorXbox.getBumper(Hand.kRight);
			break;
		case Invalid:
		default:
			retval = false;
			break;
		}
		
		return retval;
	}
	
	public boolean shooterDisable() {
		boolean retval;
		
		switch(operatorControllerType) {
		case Joystick:
			retval = operatorJoystick.getRawButton(2);
			break;
		case XboxController:
			retval = operatorXbox.getBumper(Hand.kLeft);
			break;
		case Invalid:
		default:
			retval = false;
			break;
		}
		
		return retval;
	}

	public boolean climb() {
		boolean retval;
		
		switch(operatorControllerType) {
		case Joystick:
			retval = operatorJoystick.getRawButton(11);
			break;
		case XboxController:
			retval = operatorXbox.getYButton();
			break;
		case Invalid:
		default:
			retval = false;
			break;
		}
		
		return retval;
	}

	public boolean descend() {
		boolean retval;
		
		switch(operatorControllerType) {
		case Joystick:
			retval = operatorJoystick.getRawButton(10);
			break;
		case XboxController:
			retval = operatorXbox.getXButton();
			break;
		case Invalid:
		default:
			retval = false;
			break;
		}
		
		return retval;
	}

	public boolean placeGear() {
		boolean retval;

		switch (driverControllerType) {
		case Joystick:
			retval = driverJoystick.getRawButton(11);
			break;
		case XboxController:
			retval = driverXbox.getAButton();
			break;
		case Invalid:
		default:
			retval = false;
			break;
		}

		return retval;
	}

	public boolean enableIntake() {
		boolean retval;
		
		switch(operatorControllerType) {
		case Joystick:
			retval = operatorJoystick.getRawButton(7);
			break;
		case XboxController:
			retval = operatorXbox.getAButton();
			break;
		case Invalid:
		default:
			retval = false;
			break;
		}
		
		return retval;
	}
	
	public boolean reverseIntake() {
		boolean retval;
		
		switch(operatorControllerType) {
		case Joystick:
			retval = operatorJoystick.getRawButton(6);
			break;
		case XboxController:
			retval = operatorXbox.getBButton();
			break;
		case Invalid:
		default:
			retval = false;
			break;
		}
		
		return retval;
	}
	
	public boolean viewGearCamera() {
		boolean retval;
		
		switch(operatorControllerType) {
		case Joystick:
			retval = operatorJoystick.getRawButton(8);
			break;
		case XboxController:
			retval = operatorXbox.getBackButton();
			break;
		case Invalid:
		default:
			retval = false;
			break;
		}
		
		return retval;
	}
	
	public boolean viewBoilerCamera() {
		boolean retval;
		
		switch(operatorControllerType) {
		case Joystick:
			retval = operatorJoystick.getRawButton(9);
			break;
		case XboxController:
			retval = operatorXbox.getStartButton();
			break;
		case Invalid:
		default:
			retval = false;
			break;
		}
		
		return retval;
	}
}
