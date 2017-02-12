package org.usfirst.frc.team292.robot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public class GearCamera extends Camera {
	protected static final int DIST_MULT = 2;

	public GearCamera(String cameraName, int device, Gyro gyro) {
		super(cameraName, device, gyro);
	}

	/*
	 * Processes the output of the GRIP pipeline to determine the angle and distance of the
	 * gear vision targets
	 * 
	 * @param pipeline The GripPipeline object processing the retroreflective image targets
	 */
	@Override
	protected void process(GripPipeline pipeline) {
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
				
				double widthX1 = r1.width;
				double widthX2 = r2.width;
				double width = (widthX1 + widthX2) / 2;
				dist = width * DIST_MULT;
			}
		}
	}
}
