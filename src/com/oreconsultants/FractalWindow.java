package com.oreconsultants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class FractalWindow extends JFrame {
    FractalWindow() {
        this.setTitle("Recursive Tree Fractal");
        this.add(new FractalPanel());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }
    static class FractalPanel extends JPanel {
        int Screen_Width = 500;
        int Screen_Height = 500;
        double branchThickness = 10;
        double branchThicknessDecreaseAmount = 0.1;
        double n = 0;
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
            SubBranch subBranch = new SubBranch(-45, 40, (Branch) null,null);
            //SubBranch subBranch2 = new SubBranch(45, 20, (Branch) null,null);
            SubBranch subBranch3 = new SubBranch(45, 40, (Branch) null,null);
            Branch branch = new Branch(new SubBranch[]{subBranch,subBranch3},20,new Point(Screen_Width/2,Screen_Height/2),0);
            //subBranch2.setSubParent(subBranch);
            drawBranch(g,branch,3,true);
            //n+=0.1;repaint();
        }
        public void drawBranch(Graphics g, Branch branch, int depth,boolean lastBranchInGeneration) {
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
                Branch newBranch = new Branch(branch.getSubBranches(),branch.getLength(),branch.getCoordinates(),branch.getAngle());
                if (lastBranchInGeneration) {
                    newBranch.changeLengthOfAllSubBranches(0.5);
                    branchThickness -= branchThicknessDecreaseAmount;
                }
                // recursively looping
                for (AffineTransform endOfAllTopLevelSubBranch : endOfAllTopLevelSubBranches) {
                    boolean getLastBranchInGeneration = (endOfAllTopLevelSubBranches[endOfAllTopLevelSubBranches.length-1]==endOfAllTopLevelSubBranch)&&lastBranchInGeneration;
                    g2d.setTransform(endOfAllTopLevelSubBranch);
                    newBranch.setLength(0);
                    newBranch.setCoordinates(new Point(0,0));
                    newBranch.setAngle(0);
                    if (getLastBranchInGeneration) {
                        g.setColor(Color.CYAN);
                    } else {
                        g.setColor(Color.black);
                    }
                    drawBranch(g, newBranch, depth - 1,getLastBranchInGeneration);
                }

                g2d.setTransform(noTransforms); // undoing all translations made in the process of drawing this branch
            }
        }
    }
}
