package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;

public class Climber {
	CANTalon climberTalon;
	public Climber(int motorPort){
		climberTalon = new CANTalon (motorPort);
			
	}
	public void climb(){
		climberTalon.set(-1);
	}
	public void descend(){
		climberTalon.set(.25);
	}
	public void maintain(){
		climberTalon.set(-.25);
	}
	public void stop(){
		climberTalon.set(0);
	}
}
