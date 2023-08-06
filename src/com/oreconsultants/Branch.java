package com.oreconsultants;

import java.awt.*;

public class Branch {
    private final SubBranch[] subBranches;
    private double length;
    private final Point coordinates; // branches need locations while SubBranches don't
    private final double angle;
    public Branch(SubBranch[] subBranches, double length, Point coordinates, double angle) {
        this.subBranches = subBranches;
        this.length = length;
        this.coordinates = coordinates;
        this.angle = angle;
    }
    public SubBranch[] getSubBranches() {
        return subBranches;
    }
    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }
    public void changeLengthOfAllSubBranches(double multiplier) {
        for (SubBranch subBranch : subBranches) {
            subBranch.setLength(subBranch.getLength() * multiplier);
        }
    }
    public Point getCoordinates() {
        return coordinates;
    }
    public double getAngle() {
        return angle;
    }
}
