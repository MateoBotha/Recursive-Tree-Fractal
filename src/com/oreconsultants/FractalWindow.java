package com.oreconsultants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class FractalWindow extends JFrame {
    int depth;
    double branchThicknessDecreaseAmount;
    double multiplier;
    Branch branchInput; // shouldn't change
    int Screen_Width;
    int Screen_Height;
    double branchThickness;
    final double initialBranchThickness;
    /**
     * The <code>FractalWindow</code> is a class that allows the user to generate Fractal Trees easily.
     * @param depth How many recursions should occur.
     * @param branchThicknessDecreaseAmount an amount that the <code>branchThickness</code> decreases by after every Generation TODO: look into making <code>branchThicknessDecreaseAmount</code> be a multiplier.
     * @param initialBranchThickness the starting thickness of the Tree.
     * @param multiplier the amount to multiply the <code>Branch Length</code> by.
     * @param branch The branch to be recursively drawn.
     * @param Screen_Width The width of the window That will open.
     * @param Screen_Height The height of the window that will open.
     **/
    FractalWindow(int depth, double branchThicknessDecreaseAmount, double initialBranchThickness, double multiplier, Branch branch, int Screen_Width, int Screen_Height) {
        this.depth = depth;
        this.branchThicknessDecreaseAmount = branchThicknessDecreaseAmount;
        this.multiplier = multiplier;
        this.branchInput = branch;
        this.branchThickness = initialBranchThickness;
        this.Screen_Width = Screen_Width;
        this.Screen_Height = Screen_Height;
        this.initialBranchThickness = branchThickness;

        this.setTitle("Recursive Tree Fractal");
        this.add(new FractalPanel());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }
    class FractalPanel extends JPanel {
        /**
         * The Constructor for this {@link javax.swing.JPanel} and it basically just sets up the window... Again nothing to worry about
         **/
        FractalPanel() {
            this.setPreferredSize(new Dimension(Screen_Width,Screen_Height));
            this.setLayout(null);
        }
        /**
         * The <code>paintComponent</code> meathead that sets up the <code>Graphics</code> object and allows for us to draw to the {@link javax.swing.JPanel} and
         * also updates the <code>Screen Width</code> and <code>Screen Height</code> varables... even though the frame is a constant size TODO: allow the window to be resized and allow the user to get access to the new positions and reposition everything, as if the user was writing code in the <code>draw</code> meathoad and can redraw everytime something happens and has access to some data [like for example the <code>Screen Width</code> and <code>Screen Height</code>]
         **/
        public void paintComponent(Graphics g) {
            Screen_Width = this.getWidth();
            Screen_Height = this.getHeight();
            super.paintComponent(g);
            draw(g);
        }
        /**
         * The <code>draw</code> Meathead that pulls everything together, and draws the correct things on the screen.
         * @param g The <code>Graphics</code> object. This is supplied by the <code>paintComponent</code> meathead... Again you shouldn't care about this.
         **/
        public void draw(Graphics g) {
            Branch TreeCopy = resetTree();

            AffineTransform[] endPoints = drawBranch(g, TreeCopy,depth);
            drawTree(g, TreeCopy,endPoints,depth);
        }
        /**
         * The function that recursively calls the <code>drawBranch</code> meathead and actually draws the tree.
         * @param g The <code>Graphics</code> object to draw the Tree with
         * @param branch The <code>Branch</code> object that should be recursively drawn
         * @param endPoints The end Transforms of the previous Generation. Basically where to draw the next <code>Branches</code>.
         * @param depth how many recursions should occur.
         **/
        public void drawTree(Graphics g, Branch branch, AffineTransform[] endPoints, int depth) {
            if (endPoints!=null) {
                AffineTransform[] newEndPoints = new AffineTransform[0];
                Graphics2D g2d = (Graphics2D) g;
                branch = modifiersMadeToBranch(branch);
                for (int i = 0; i < endPoints.length; i++) {
                    AffineTransform beforeTranslations = g2d.getTransform();
                    g2d.setTransform(endPoints[i]);

                    // adding the newPos to the end of the array
                    AffineTransform[] newEndPointsTmp = drawBranch(g, branch, depth-1);
                    if (newEndPointsTmp != null) {
                        // newEndPointsTmp may be null if we reached the end depth
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
        /**
         * A small meathead that gets called to modify some data for the next Generation of <code>Branches</code>. You shouldn't really worry about this. TODO: allow the user to change this meathead [make it abstract]
         * @param branch The Branch To Be Modified [input].
         * @return The Modified <code>Branch</code> [output].
         **/
        public Branch modifiersMadeToBranch(Branch branch) {
            branch.setCoordinates(new Point(0, 0));
            branch.setAngle(0);
            branch.changeLengthOfAllSubBranches(multiplier);
            branchThickness -= branchThicknessDecreaseAmount;
            return branch;
        }
        /**
         * Resets the Tree and some parameters like <code>branchThickness</code> to be back to their defaults.
         * @return The Reset-ed Tree, that isn't just a pointer to the default.
         **/
        public Branch resetTree() {
            // this meathead just resets all the data about the tree back to its initial value. It returns the Branch after being reset

            branchThickness = initialBranchThickness; // reset-ing the thickness of the tree
            SubBranch[] subBranches = new SubBranch[branchInput.getSubBranches().length];
            for (int i = 0; i < subBranches.length; i++) {
                SubBranch currentBranch = branchInput.getSubBranches()[i]; // we can't just use this because it will be a pointer
                SubBranch subBranch = new SubBranch(currentBranch.getAngle(),currentBranch.getLength(),(Branch) null, null); // the last two are set to null [by the way what I'm doing is copying a refer type by copying its data, because reference types are just pointers to the other] TODO: make SubBranches not have parent but only children of type SubBranch [So that we can make complex trees, the Branch will be drawn ontop anyways]
                subBranches[i] = subBranch; // setting the copy of 'branchInput.getSubBranches()' to be the same as it... but without it being a pointer to the original
            }
            return new Branch(subBranches,branchInput.getLength(),new Point(branchInput.getCoordinates().x,branchInput.getCoordinates().y),branchInput.getAngle()); // making the copy of Branch [that isn't a pointer to the branchInput]
        }
        /**
         * Draws a <code>branch</code>, A <code>branch</code> contains <code>SubBranches</code>, and this code should draw one <code>branch</code>.
         * @param g the <code>Graphics</code> Object to draw the <code>Branch</code> with
         * @param branch the <code>Branch</code> to be drawn
         * @param depth the <code>depth</code> of... TODO: remove depth from this code because it serves no purpose now that <code>drawTree</code> exists
         **/
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
