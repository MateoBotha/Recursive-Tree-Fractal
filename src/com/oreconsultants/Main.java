package com.oreconsultants;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // a quick demo programme
        int Screen_Width = 500;
        int Screen_Height = 500;
        SubBranch subBranch = new SubBranch(-15, 40, (Branch) null,null);
        //SubBranch subBranch2 = new SubBranch(45, 20, (Branch) null,null);
        SubBranch subBranch3 = new SubBranch(15, 40, (Branch) null,null);
        Branch branch = new Branch(new SubBranch[]{subBranch,subBranch3},20,new Point(Screen_Width/2,Screen_Height/2),0);
        //subBranch2.setSubParent(subBranch);
        new FractalWindow(8,0.5,10,0.5,branch, Screen_Width,Screen_Height);
    }
}
