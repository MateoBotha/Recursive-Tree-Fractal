package com.oreconsultants;

public class SubBranch {
    private final double angle;
    private Branch branchParent;
    private SubBranch subParent;
    private final boolean parentType; // false = Branch, True = SubBranch
    private Branch child; // I may make the code, so you can have a child of a subBranch, but... How will we know their angle and have it stored easily
    private double length;
    SubBranch (double angle, double length, Branch parent, Branch child) {
        this.angle = angle;
        this.branchParent = parent;
        this.length = length;
        parentType = false;

        this.child = child;
    }
    SubBranch (double angle, double length, SubBranch parent, Branch child) {
        this.angle = angle;
        this.subParent = parent;
        this.length = length;
        parentType = true;

        this.child = child;
    }
    public double getAngle() {
        return angle;
    }
    public Branch getBranchParent() {
        if (parentType) {
            // if it is a subBranch
            return null;
        } else {
            return branchParent;
        }
    }
    public SubBranch getSubParent() {
        if (!parentType) {
            // if the parent is a Branch
            return null;
        } else {
            return subParent;
        }
    }
    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }
    public void setBranchParent(Branch branchParent) {
        this.branchParent = branchParent;
    }
    public void setSubParent(SubBranch subParent) {
        this.subParent = subParent;
    }
}
