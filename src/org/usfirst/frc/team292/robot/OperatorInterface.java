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
		double value;

		switch (driverControllerType) {
		case Joystick:
			value = applyDeadbandJoystick(driverJoystick.getX());
			break;
		case XboxController:
			value = applyDeadbandXbox(driverXbox.getX(Hand.kLeft));
			break;
		case Invalid:
		default:
			value = 0;
			break;
		}

		return value;
	}

	public double getDriveY() {
		double value;

		switch (driverControllerType) {
		case Joystick:
			value = applyDeadbandJoystick(driverJoystick.getY());
			break;
		case XboxController:
			value = applyDeadbandXbox(driverXbox.getY(Hand.kLeft));
			break;
		case Invalid:
		default:
			value = 0;
			break;
		}

		return value;
	}

	public double getDriveZ() {
		double value;

		switch (driverControllerType) {
		case Joystick:
			value = applyDeadbandJoystick(driverJoystick.getZ());
			break;
		case XboxController:
			value = applyDeadbandXbox(driverXbox.getX(Hand.kRight));
			break;
		case Invalid:
		default:
			value = 0;
			break;
		}

		return value;
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
		return driverXbox.getAButton();
	}

	public boolean shootEnable() {
		return driverXbox.getXButton();
	}

	public boolean climb() {
		return driverXbox.getYButton();
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

	public boolean intake() {
		return driverXbox.getBButton();
	}

	public boolean reverseIntake() {
		return driverXbox.getBumper(Hand.kRight);
	}
}
