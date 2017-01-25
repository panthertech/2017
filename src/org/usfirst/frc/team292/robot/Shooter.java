package org.usfirst.frc.team292.robot;

import com.ctre.CANTalon;

public class Shooter {

	CANTalon shooterTalon;
	CANTalon feederTalon;
	public Shooter(int shooterPort, int feederPort){
		shooterTalon = new CANTalon (shooterPort);
		feederTalon = new CANTalon (feederPort);
	}
	public void enableShooter(boolean enable){
		
	}
	public void shoot(){
		
	}
}
