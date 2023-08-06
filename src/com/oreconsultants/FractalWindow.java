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
        double branchThicknessDecreaseAmount = 0.0;
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
            SubBranch subBranch2 = new SubBranch(45, 20, (Branch) null,null);
            SubBranch subBranch3 = new SubBranch(45, 50, (Branch) null,null);
            Branch branch = new Branch(new SubBranch[]{subBranch,subBranch3,subBranch2},20,new Point(Screen_Width/2,Screen_Height/2),n);
            subBranch2.setSubParent(subBranch);
            drawBranch(g,branch);
            n+=0.1;repaint();
        }
        public void drawBranch(Graphics g, Branch branch) {
            Graphics2D g2d = (Graphics2D) g;
            Point branchCoordinates = branch.getCoordinates();
            double branchLength = branch.getLength();
            double branchAngle = branch.getAngle();
            SubBranch[] subBranches = branch.getSubBranches();

            AffineTransform noTransforms = g2d.getTransform();
            g2d.translate(branchCoordinates.x, branchCoordinates.y);
            g2d.rotate(Math.toRadians(branchAngle));
            g2d.fillRect((int) -(branchThickness/2), (int) -(branchLength/2), (int)branchThickness, (int) branchLength);
            g2d.translate(-branchCoordinates.x, -branchCoordinates.y);

            for (int i = 0; i < subBranches.length; i++) {
                if (!subBranches[i].getParentType()) {
                    // the current Subbranch has a branch as a parent [the branch we just drew]

                    AffineTransform branchTranslations = g2d.getTransform(); // the rotations before this SubBranch
                    Point subBranchCoordinates = new Point((branchCoordinates.x), (int) (branchCoordinates.y - (branchLength) / 2)); // Getting the top of the original Branch
                    g2d.translate(subBranchCoordinates.x, subBranchCoordinates.y); // setting that point to be the origin
                    g2d.rotate(Math.toRadians(subBranches[i].getAngle())); // rotating this subBranch by the prescribed angle
                    g2d.fillRect((int) -(branchThickness / 2), (int) -(subBranches[i].getLength()), (int) branchThickness, (int) subBranches[i].getLength()); // drawing this subBranch.
                    AffineTransform subBranchTranslations = g2d.getTransform();

                    for (int j = 0; j < subBranches.length; j++) {
                        // loop through all subBranches
                        if ((subBranches[j].getParentType())&&(subBranches[j].getSubParent()==subBranches[i])) {
                            // if this subBranch [in loop j] is a child of this subBranch [in loop i]

                            SubBranch parentBranch = subBranches[j].getSubParent(); // the SubBranch that is the parent of the current SubBranch
                            double parentBranchLength = parentBranch.getLength(); // the length of the current SubBranch's parent
                            g2d.translate(0, -parentBranchLength); // moving up the origin [from the lastBranch], because the plane is already rotated, this will be the end of the current SubBranch's parent
                            g2d.rotate(Math.toRadians(subBranches[j].getAngle())); // rotating by the prescribed angle
                            g2d.fillRect((int) -(branchThickness / 2), (int) -(subBranches[j].getLength()), (int) branchThickness, (int) subBranches[j].getLength()); // drawing the SubBranch
                            g2d.setTransform(subBranchTranslations); // undoing the translations made by drawing this subBranch child (of another subBranch)
                        }
                    }
                    g2d.setTransform(branchTranslations); // undoing the translations made by this subBranch and (if it had a subBranch child) its child
                }
            }
            g2d.setTransform(noTransforms); // undoing all translations made in the process of drawing this branch
            branchThickness-=branchThicknessDecreaseAmount;
        }
    }
}
