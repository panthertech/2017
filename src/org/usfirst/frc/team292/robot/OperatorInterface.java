package org.usfirst.frc.team292.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class OperatorInterface {
	private XboxController xbox;
	public OperatorInterface() {
xbox = new XboxController (0);
	}
	public double getDriveX (){
		return applydeadband (xbox.getX(Hand.kLeft));
	}
	public double getDriveY (){
		return applydeadband (xbox.getY(Hand.kLeft));
	}
	public double getDriveZ (){
		return applydeadband (xbox.getX(Hand.kRight));
		
	}
	private double applydeadband(double x){
		double deadband = 0.1;
		if(x >= deadband) x = 1/(1 - deadband)*(x - deadband);
		else if (x <= -deadband) x = 1/(1 - deadband)*(x + deadband);
		else x = 0;
		return x;
		
	}
}

