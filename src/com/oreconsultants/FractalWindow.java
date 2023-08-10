package com.oreconsultants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class FractalWindow extends JFrame {
    int depth;
    double branchThicknessDecreaseAmount;
    double multiplier;
    Branch branchInput;
    int Screen_Width;
    int Screen_Height;
    double branchThickness;
    FractalWindow(int depth, double branchThicknessDecreaseAmount, double initialBranchThickness, double multiplier, Branch branch, int Screen_Width, int Screen_Height) {
        this.depth = depth;
        this.branchThicknessDecreaseAmount = branchThicknessDecreaseAmount;
        this.multiplier = multiplier;
        this.branchInput = branch;
        this.branchThickness = initialBranchThickness;
        this.Screen_Width = Screen_Width;
        this.Screen_Height = Screen_Height;

        this.setTitle("Recursive Tree Fractal");
        this.add(new FractalPanel());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }
    class FractalPanel extends JPanel {
        FractalPanel() {
            this.setPreferredSize(new Dimension(Screen_Width,Screen_Height));
            this.setLayout(null);
        }
        public void paintComponent(Graphics g) {
            Screen_Width = this.getWidth();
            Screen_Height = this.getHeight();
            super.paintComponent(g);
            draw(g);
        }
        public void draw(Graphics g) {
            SubBranch subBranch = new SubBranch(-15, 40, (Branch) null,null);
            //SubBranch subBranch2 = new SubBranch(45, 20, (Branch) null,null);
            SubBranch subBranch3 = new SubBranch(15, 40, (Branch) null,null);
            branchInput = new Branch(new SubBranch[]{subBranch,subBranch3},20,new Point(Screen_Width/2,Screen_Height/2),0);
            AffineTransform[] endPoints = drawBranch(g, branchInput,depth);
            drawTree(g, branchInput,endPoints,depth);
        }
        public void drawTree(Graphics g, Branch branch, AffineTransform[] endPoints, int depth) {
            if (endPoints!=null) {
                AffineTransform[] newEndPoints = new AffineTransform[0];
                Graphics2D g2d = (Graphics2D) g;
                branch = modifiersMadeToBranch(branch);
                for (int i = 0; i < endPoints.length; i++) {
                    AffineTransform beforeTranslations = g2d.getTransform();
                    g2d.setTransform(endPoints[i]);

                    // adding the newPos to the end of the array
                    AffineTransform[] newEndPointsTmp = drawBranch(g, branch, depth - 1);
                    if (newEndPointsTmp != null) {
                        // newEndPointsTmp may be null if e reached the end depth
                        AffineTransform[] newEndPointsCpy = new AffineTransform[newEndPoints.length];
                        System.arraycopy(newEndPoints, 0, newEndPointsCpy, 0, newEndPoints.length);
                        newEndPoints = new AffineTransform[newEndPoints.length + newEndPointsTmp.length];
                        System.arraycopy(newEndPointsCpy, 0, newEndPoints, 0, newEndPointsCpy.length);
                        System.arraycopy(newEndPointsTmp, 0, newEndPoints, newEndPointsCpy.length, newEndPointsTmp.length);
                    } else {
                        newEndPoints = null;
                    }
                    g2d.setTransform(beforeTranslations);
                }
                drawTree(g, branch, newEndPoints, depth - 1);
            }
        }
        public Branch modifiersMadeToBranch(Branch branch) {
            branch.setCoordinates(new Point(0, 0));
            branch.setAngle(0);
            branch.changeLengthOfAllSubBranches(multiplier);
            branchThickness -= branchThicknessDecreaseAmount;
            return branch;
        }
        public AffineTransform[] drawBranch(Graphics g, Branch branch, int depth) {
            if (depth > 0) {
                Graphics2D g2d = (Graphics2D) g;
                Point branchCoordinates = branch.getCoordinates();
                double branchLength = branch.getLength();
                double branchAngle = branch.getAngle();
                SubBranch[] subBranches = branch.getSubBranches();

                AffineTransform noTransforms = g2d.getTransform();
                g2d.translate(branchCoordinates.x, branchCoordinates.y);
                g2d.rotate(Math.toRadians(branchAngle));
                g2d.fillRect((int) -(branchThickness / 2), (int) -(branchLength / 2), (int) branchThickness, (int) branchLength);
                g2d.translate(-branchCoordinates.x, -branchCoordinates.y);
                AffineTransform[] endOfAllTopLevelSubBranches = new AffineTransform[0];

                for (int i = 0; i < subBranches.length; i++) {


                    if (!subBranches[i].getParentType()) {
                        // the current Subbranch has a branch as a parent [the branch we just drew]

                        AffineTransform branchTranslations = g2d.getTransform(); // the rotations before this SubBranch
                        Point subBranchCoordinates = new Point((branchCoordinates.x), (int) (branchCoordinates.y - (branchLength) / 2)); // Getting the top of the original Branch
                        g2d.translate(subBranchCoordinates.x, subBranchCoordinates.y); // setting that point to be the origin
                        g2d.rotate(Math.toRadians(subBranches[i].getAngle())); // rotating this subBranch by the prescribed angle
                        g2d.fillRect((int) -(branchThickness / 2), (int) -(subBranches[i].getLength()), (int) branchThickness, (int) subBranches[i].getLength()); // drawing this subBranch.
                        AffineTransform subBranchTranslations = g2d.getTransform();

                        boolean currentSubBranchHasNoChildren = true;

                        for (int j = 0; j < subBranches.length; j++) {
                            // loop through all subBranches
                            if ((subBranches[j].getParentType()) && (subBranches[j].getSubParent() == subBranches[i])) {
                                // if this subBranch [in loop j] is a child of this subBranch [in loop i]

                                currentSubBranchHasNoChildren = false;

                                SubBranch parentBranch = subBranches[j].getSubParent(); // the SubBranch that is the parent of the current SubBranch
                                double parentBranchLength = parentBranch.getLength(); // the length of the current SubBranch's parent
                                g2d.translate(0, -parentBranchLength); // moving up the origin [from the lastBranch], because the plane is already rotated, this will be the end of the current SubBranch's parent
                                g2d.rotate(Math.toRadians(subBranches[j].getAngle())); // rotating by the prescribed angle
                                g2d.fillRect((int) -(branchThickness / 2), (int) -(subBranches[j].getLength()), (int) branchThickness, (int) subBranches[j].getLength()); // drawing the SubBranch
                                g2d.setTransform(subBranchTranslations); // undoing the translations made by drawing this subBranch child (of another subBranch)
                            }
                            if (currentSubBranchHasNoChildren) {
                                AffineTransform beforeEndOfBranchTranslation = g2d.getTransform();
                                g2d.translate(0, -subBranches[i].getLength());
                                AffineTransform endOfBranch = g2d.getTransform();
                                g2d.setTransform(beforeEndOfBranchTranslation);

                                // adding a value to the array
                                AffineTransform[] previousVersion = new AffineTransform[endOfAllTopLevelSubBranches.length];
                                System.arraycopy(endOfAllTopLevelSubBranches, 0, previousVersion, 0, endOfAllTopLevelSubBranches.length);
                                endOfAllTopLevelSubBranches = new AffineTransform[endOfAllTopLevelSubBranches.length + 1];
                                System.arraycopy(previousVersion, 0, endOfAllTopLevelSubBranches, 0, previousVersion.length);

                                // there is one space left at the end!
                                endOfAllTopLevelSubBranches[endOfAllTopLevelSubBranches.length - 1] = endOfBranch;
                            }
                        }
                        g2d.setTransform(branchTranslations); // undoing the translations made by this subBranch and (if it had a subBranch child) its child
                    }
                }
                g2d.setTransform(noTransforms); // undoing all transformed made
                return endOfAllTopLevelSubBranches;
            }
            return null;
        }
    }
}
