package org.usfirst.frc.team292.robot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Camera {
	protected static final int IMG_WIDTH = 320;
	protected static final int IMG_HEIGHT = 240;
	protected static final int FOV = 61;
	protected static final double FOCAL_LENGTH = 230;
	protected static final double TARGET_WIDTH = 1;
	
	protected Gyro gyro;

	protected double targetAngle;
	protected double lastValidAngle;
	protected double dist;

	private Solenoid light;
	private boolean processingEnabled;

	public Camera(String cameraName, int device, Gyro gyro, int lightPort) {
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(cameraName, device);
		camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		this.gyro = gyro;
		light = new Solenoid(lightPort);
		targetAngle = gyro.getAngle();
		lastValidAngle = gyro.getAngle();
		processingEnabled = false;
		VisionThread visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
			if(processingEnabled) {
				light.set(true);
				process(pipeline);
			} else {
				light.set(false);
				targetAngle = lastValidAngle = dist = 0;
			}
		});
		visionThread.start();
	}

	/**
	 * The process method should be overridden by classes extending the Camera class.  This version
	 * calculates the target angle of the first object returned by the vision pipeline. Because this
	 * is a non-specific target, the calculated distance will not be valid.
	 * 
	 * @param pipeline The GripPipeline object processing the retroreflective image targets
	 */
	protected void process(GripPipeline pipeline) {
		if (!pipeline.filterContoursOutput().isEmpty()) {
			Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));

			double centerX = r.x + (r.height / 2);
			double offset = centerX - (IMG_WIDTH / 2);
			double scaleOffset = offset / (IMG_WIDTH);
			targetAngle = gyro.getAngle() + scaleOffset * FOV / 2;
			lastValidAngle = gyro.getAngle();
			
			double width = r.width;
			dist = (TARGET_WIDTH * FOCAL_LENGTH) / width;
		}
	}

	/**
	 * Returns the adjusted angle of the target based on the current and last valid gyro angles
	 * and the target angle calculated by vision processing.
	 * 
	 * @return The angle of the target 
	 */
	public double getTargetAngle() {
		return targetAngle;
	}
	
	public boolean onTarget() {
		return (Math.abs(targetAngle - gyro.getAngle()) < 0.5);
	}


	/**
	 * Returns the approximate distance to the target calculated by vision processing.
	 * 
	 * @return The distance to the target 
	 */
	public double getTargetDistance() {
		return dist;
	}
	
	public void enableProcessing() {
		processingEnabled = true;
	}
	
	public void disableProcessing() {
		processingEnabled = false;
	}
}
