package com.oreconsultants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

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
            Branch branch = new Branch(new SubBranch[]{subBranch,subBranch2},20,new Point(Screen_Width/2,Screen_Height/2),n);
            subBranch.setBranchParent(branch);
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

            g2d.translate(branchCoordinates.x, branchCoordinates.y);
            AffineTransform old = g2d.getTransform();
            g2d.rotate(Math.toRadians(branchAngle));
            g2d.fillRect((int) -(branchThickness/2), (int) -(branchLength/2), (int)branchThickness, (int) branchLength);
            //g2d.setTransform(old);
            g2d.translate(-branchCoordinates.x, -branchCoordinates.y);

            for (int i = 0; i < subBranches.length; i++) {
                if (!subBranches[i].getParentType()) {
                    // the current Subbranch has a branch as a parent [the branch we just drew]

                    Point subBranchCoordinates = new Point((branchCoordinates.x), (int) (branchCoordinates.y - (branchLength) / 2));
                    //AffineTransform old = g2d.getTransform();
                    g2d.translate(subBranchCoordinates.x, subBranchCoordinates.y);
                    g2d.rotate(Math.toRadians(subBranches[i].getAngle()));
                    g2d.fillRect((int) -(branchThickness / 2), (int) -(subBranches[i].getLength()), (int) branchThickness, (int) subBranches[i].getLength());
                    //g2d.translate(-(subBranchCoordinates.x),-(subBranchCoordinates.y));
                } else {
                    // the current subBranch has a subbranch as a parent, we must draw it on top of its parent.

                    g2d.setTransform(old);
                    SubBranch currentSubBranchParent = subBranches[i].getSubParent();
                    double parentBranchAngle = currentSubBranchParent.getAngle()+branchAngle; // were adding the angle of the original Branch because this SubBranch is drawn differently, and the previous angles don't affect it... So were making it affect it
                    double parentBranchLength = currentSubBranchParent.getLength();

                    Point subBranchCoordinates = new Point((int) ((0)-(Math.cos(Math.toRadians(parentBranchAngle))*parentBranchLength)), (int) ((-(branchLength/2))+(Math.sin(Math.toRadians(parentBranchAngle))* parentBranchLength))); // the top of the current branches parent [using trigonometry]
                    g2d.translate(subBranchCoordinates.x, subBranchCoordinates.y); // setting the end of the current subBranches' parent point to be the origin.
                    g2d.rotate(Math.toRadians(subBranches[i].getAngle())); // rotating by the prescribed angle
                    g2d.fillRect((int) -(branchThickness / 2), (int) -(subBranches[i].getLength()), (int) branchThickness, (int) subBranches[i].getLength()); // drawing the branch
                }
            }
            branchThickness-=branchThicknessDecreaseAmount;
        }
    }
}
