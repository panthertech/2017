package org.usfirst.frc.team292.robot.auto;

import java.util.ArrayList;

import org.usfirst.frc.team292.robot.*;

public abstract class ScoreGearCrossLine extends ScoreGear {
	
	private ScoreGearCrossLineStates scoreGearCrossLineState;
	private ArrayList<CrossLineTarget> crossLineTargets;
	private int crossLineTargetIndex;
	private boolean crossLineTargetInit;
	
	private enum ScoreGearCrossLineStates {
		Init,
		ScoreGear,
		CrossLine,
		Done
	}
	
	private class CrossLineTarget {
		public double distance;
		public double angle;
		public CrossLineTarget(double distance, double angle) {
			this.distance = distance;
			this.angle = angle;
		}
	}

	public ScoreGearCrossLine(Robot robot) {
		super(robot);
		scoreGearCrossLineState = ScoreGearCrossLineStates.Init;
		crossLineTargets = new ArrayList<CrossLineTarget>();
		crossLineTargetIndex = 0;
		crossLineTargetInit = false;
	}

	@Override
	public void periodic() {
		switch(scoreGearCrossLineState) {
		case Init:
			scoreGearCrossLineState = ScoreGearCrossLineStates.ScoreGear;
		case ScoreGear:
			if(scoreGear()) {
				scoreGearCrossLineState = ScoreGearCrossLineStates.CrossLine;
			}
			break;
		case CrossLine:
			if(crossLine()) {
				scoreGearCrossLineState = ScoreGearCrossLineStates.Done;
			}
			break;
		case Done:
		default:
			break;
		}
	}
	
	public void addCrossLineTarget(double distance, double angle) {
		crossLineTargets.add(new CrossLineTarget(distance, angle));
	}

	public boolean crossLine() {
		boolean retval = false;
		
		if(crossLineTargetIndex == crossLineTargets.size()) {
			retval = true;
		} else {
			if(crossLineTargetInit) {
				if(onTarget()) {
					crossLineTargetIndex++;
					crossLineTargetInit = false;
				}
			} else {
				if(crossLineTargets.get(crossLineTargetIndex).distance != 0.0) {
					robot.drive.driveDistance(crossLineTargets.get(crossLineTargetIndex).distance, crossLineTargets.get(crossLineTargetIndex).angle, true);
				} else {
					robot.drive.turn(crossLineTargets.get(crossLineTargetIndex).angle, true);
				}
				crossLineTargetInit = true;
			}
		}
		
		return retval;
	}

}
