package org.usfirst.frc.team292.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class NavModule implements Gyro {
	public AHRS sensor;

	public NavModule() {
		try {
			sensor = new AHRS(I2C.Port.kOnboard);

		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
		}
	}

	@Override
	public double getAngle() {
		return sensor.getAngle();

	}

	@Override
	public void calibrate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public double getRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void free() {
		// TODO Auto-generated method stub

	}

}