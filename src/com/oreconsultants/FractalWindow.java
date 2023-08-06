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
        int n = 0;
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
            SubBranch subBranch = new SubBranch(180, 40, (Branch) null,null);
            Branch branch = new Branch(new SubBranch[]{subBranch},20,new Point(46,57),n);
            subBranch.setBranchParent(branch);
            drawBranch(g,branch);
            n++;repaint();
        }
        public void drawBranch(Graphics g, Branch branch) {
            Graphics2D g2d = (Graphics2D) g;
            Point branchCoordinates = branch.getCoordinates();
            double branchLength = branch.getLength();
            double branchAngle = branch.getAngle();
            SubBranch[] subBranches = branch.getSubBranches();
            {
            g2d.translate(branchCoordinates.x, branchCoordinates.y);
            AffineTransform old = g2d.getTransform();
            g2d.rotate(Math.toRadians(branchAngle));
            g2d.fillRect((int) -(branchThickness/2), (int) -(branchLength/2), (int)branchThickness, (int) branchLength);
            g2d.setTransform(old);
            g2d.translate(-branchCoordinates.x, -branchCoordinates.y);
            }
            for (int i = 0; i < subBranches.length; i++) {
                branchThickness-=branchThicknessDecreaseAmount;
                Point subBranchCoordinates = new Point((branchCoordinates.x), (int) (branchCoordinates.y-(branchLength)));
                AffineTransform old = g2d.getTransform();
                g2d.translate(subBranchCoordinates.x,subBranchCoordinates.y);
                g2d.rotate(Math.toRadians(subBranches[i].getAngle()));
                g2d.fillRect((int) -(branchThickness/2),0,(int)branchThickness,(int)subBranches[i].getLength());
                g2d.setTransform(old);
                //g2d.translate(-(subBranchCoordinates.x+((double)branchThickness/2)),-(subBranchCoordinates.y+(branchLength/2)));
            }
        }
    }
}
