package org.usfirst.frc.team292.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class NavModule implements Gyro, PIDSource {
	public AHRS sensor;
	public AnalogGyro gyro;

	public NavModule() {
		try {
			sensor = new AHRS(SerialPort.Port.kMXP);
		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
		}
		gyro = new AnalogGyro(0);
	}

	@Override
	public double getAngle() {
		double angle = gyro.getAngle();
		if(navxIsAttached()) {
			angle = sensor.getAngle();
		}
		return angle;
	}

	@Override
	public void calibrate() {
		gyro.calibrate();
	}

	@Override
	public void reset() {
		gyro.reset();
		sensor.zeroYaw();
	}

	@Override
	public double getRate() {
		double rate = gyro.getRate();
		if(navxIsAttached()) {
			rate = sensor.getRate();
		}
		return rate;
	}

	@Override
	public void free() {
		gyro.free();
		sensor.free();
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
		double get = gyro.pidGet();
		if(navxIsAttached()) {
			get = sensor.pidGet();
		}
		return get;
	}
	
	public String getSensorType() {
		String sensorType = "Analog Gyro";
		if(navxIsAttached()) {
			sensorType = "NavX";
		}
		return sensorType;
	}
	
	public boolean navxIsAttached() {
		//return sensor.isConnected() && !sensor.isCalibrating();
		return false;
	}
}