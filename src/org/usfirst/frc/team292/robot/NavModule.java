package org.usfirst.frc.team292.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class NavModule implements Gyro, PIDSource {
	public AHRS sensor;
	public AnalogGyro gyro;

	public NavModule() {
		try {
			sensor = new AHRS(I2C.Port.kOnboard);

		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
		}
		gyro = new AnalogGyro(0);
	}

	@Override
	public double getAngle() {
		return gyro.getAngle();
	}

	@Override
	public void calibrate() {
		gyro.calibrate();
	}

	@Override
	public void reset() {
		gyro.reset();
	}

	@Override
	public double getRate() {
		return gyro.getRate();
	}

	@Override
	public void free() {
		gyro.free();
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		return gyro.pidGet();
	}
}