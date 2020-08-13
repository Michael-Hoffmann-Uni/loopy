package jpp.infinityloop.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MetaTile extends ImageView {
    private int x;
    private int y;
    private double angle;

    public MetaTile(Image img, int x, int y) {
        super(img);
        this.x = x;
        this.y = y;
        this.angle = 0;
    }

    public int getCol() {
        return this.x;
    }

    public int getRow() {
        return this.y;
    }

    public double getAngle() {
        return this.angle;
    }

    public void setAngle(double a) {
        this.angle = a;
    }

}
