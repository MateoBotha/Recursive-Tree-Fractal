package com.oreconsultants;

public class SubBranch {
    private final double angle;
    private Branch branchParent;
    private SubBranch subParent;
    private boolean parentType; // false = Branch, True = SubBranch
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
    /**
     * This will override and set the <code>SubBranch</code> parent to be <code>null</code>.
     * Because there can't be two parents for one branch... at least yet.
     * @param branchParent The <code>Branch</code> to set as this SubBranches Parent.
     **/
    public void setBranchParent(Branch branchParent) {
        this.parentType = false; // setting the parentType to honour the new branch.
        this.branchParent = branchParent;
        this.subParent = null; // setting it to null because we can't have two parents
    }
    /**
     * This will override and set the <code>Branch</code> parent to be <code>null</code>.
     * Because there can't be two parents for one branch... at least yet.
     * @param subParent The <code>Branch</code> to set as this SubBranches Parent.
     **/
    public void setSubParent(SubBranch subParent) {
        this.parentType = true;
        this.subParent = subParent;
        this.branchParent = null;
    }
    /**
     * The <code>ParentType</code> variable is either 'true' or 'false', which makes sense
     * because it is a boolean. Anyway, that's all I have to say.
     * @return True if the parent is a <code>SubBranch</code>, false if the parent is a <code>Branch</code>
     **/
    public boolean getParentType() {
        return parentType;
    }
}
