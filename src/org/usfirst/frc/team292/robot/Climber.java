package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;

public class Climber {
	private static final double kClimbSpeed = -1.0;
	private static final double kMaintainSpeed = 0.0;
	private static final double kDescendSpeed = -0.5;
	
	private CANTalon climberTalon;
	private boolean climbLast;

	public Climber(int motorPort) {
		climberTalon = new CANTalon(motorPort);
		climbLast = false;
	}

	public void climb() {
		climbLast = true;
		climberTalon.set(kClimbSpeed);
	}

	public void descend() {
		climbLast = false;
		climberTalon.set(kDescendSpeed);
	}

	public void maintain() {
		if(climbLast) {
			climberTalon.set(kMaintainSpeed);
		} else {
			climberTalon.set(0);
		}
	}

	public void stop() {
		climbLast = false;
		climberTalon.set(0);
	}
	
	public boolean getClimberEnabled() {
		return climbLast;
	}
}
