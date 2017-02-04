package org.usfirst.frc.team292.robot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class GearCamera {
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	private static final int FOV = 61;
	private VisionThread visionThread;
	private Gyro gyro;

	double targetAngle;
	double lastValidAngle;

	public GearCamera(Gyro gyro) {
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		targetAngle = gyro.getAngle();
		lastValidAngle = gyro.getAngle();
		visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
			process(pipeline);
		});
		visionThread.start();
	}

	private void process(GripPipeline pipeline) {
		if (pipeline.filterContoursOutput().size() >= 2) {
			Rect r1 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
			Rect r2 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(1));

			double centerY1 = r1.y + (r1.height / 2);
			double centerY2 = r2.y + (r2.height / 2);
			double dif = Math.abs(centerY1 - centerY2);
			if (dif <= 50) {
				double centerX1 = r1.x + (r1.width / 2);
				double centerX2 = r2.x + (r2.width / 2);
				double centerX = (centerX1 + centerX2) / 2;
				double offset = centerX - (IMG_WIDTH / 2);
				double scaleOffset = offset / (IMG_WIDTH);
				targetAngle = scaleOffset * FOV / 2;
				lastValidAngle = gyro.getAngle();
			}
		}
	}

	public double getTargetAngle() {
		return lastValidAngle - gyro.getAngle() + targetAngle;
	}

	public double getTargetDistance() {
		return 0;
	}
}
