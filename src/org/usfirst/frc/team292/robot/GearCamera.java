package org.usfirst.frc.team292.robot;

import java.util.ArrayList;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public class GearCamera extends Camera {
	protected static final double TARGET_WIDTH = 2;

	public GearCamera(String cameraName, int device, Gyro gyro, int lightPort) {
		super(cameraName, device, gyro, lightPort);
	}
	
	private class TargetConfidence {
		public Rect r;
		public double confidence;
		public TargetConfidence(Rect r, double confidence) {
			this.r = r;
			this.confidence = confidence;
		}
	}
	
	private class TargetPairConfidence {
		public Rect r1, r2;
		public double confidence;
		public TargetPairConfidence(Rect r1, Rect r2, double confidence) {
			this.r1 = r1;
			this.r2 = r2;
			this.confidence = confidence;
		}
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
			ArrayList<TargetConfidence> possibleTargets = new ArrayList<TargetConfidence>();
			ArrayList<TargetPairConfidence> possibleTargetPairs = new ArrayList<TargetPairConfidence>();
			
			for (MatOfPoint contour : pipeline.filterContoursOutput()) {
				Rect r = Imgproc.boundingRect(contour);
				
				// check how of the bounding rectangle the contour actually takes up
				double areaConfidence = Imgproc.contourArea(contour) / r.area();
				
				// check the shape - should be more 'vertical' (5hx2w)
				double shapeConfidence = 0.0;
				double ratio = r.height / r.width;
				if(ratio > 2.5) {
					shapeConfidence = 2.5 / ratio;
				} else {
					shapeConfidence = ratio / 2.5;
				}
				
				// calculate overall confidence
				double confidence = areaConfidence * shapeConfidence;
				possibleTargets.add(new TargetConfidence(r, confidence));
			}
			
			if(possibleTargets.size() >= 2) {
				for (TargetConfidence t1 : possibleTargets) {
					possibleTargets.remove(t1);
					if(!possibleTargets.isEmpty()) {
						for (TargetConfidence t2 : possibleTargets) {
							double diff = Math.abs(t1.r.y - t2.r.y);
							double heightConfidence = (IMG_HEIGHT - diff) / IMG_HEIGHT;
							double confidence = heightConfidence * t1.confidence * t2.confidence;
							possibleTargetPairs.add(new TargetPairConfidence(t1.r, t2.r, confidence));
						}
					}
				}
				
				double maxConfidence = 0.0;
				Rect r1 = null;
				Rect r2 = null;
				
				for(TargetPairConfidence t: possibleTargetPairs) {
					if(t.confidence > maxConfidence) {
						maxConfidence = t.confidence;
						r1 = t.r1;
						r2 = t.r2;
					}
				}

				if(r1 != null && r2 != null) {
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
					dist = (TARGET_WIDTH * FOCAL_LENGTH) / width;
				}
			}
		}
	}
}
