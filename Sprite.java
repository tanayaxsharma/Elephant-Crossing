import javax.swing.*;
import java.awt.*;


class Sprite {
    private int x, y;
    private double xDir, yDir = 0;
    private ImageIcon image;
    private boolean show = true;
    private String filename = "";

    public Sprite() {

    }

    public Sprite(int x, int y, String filename) {
        this.x = x;
        this.y = y;
        this.filename = filename;
        loadImage(filename);
    }


    public Sprite(String filename) {
        this.filename = filename;
        loadImage(filename);
    }


    public void loadImage(String filename) {
        this.filename = filename;
        try {
            this.image = new ImageIcon(filename);
        } catch (Exception e) {
            System.out.println("Error loading image: " + filename);
            this.image = null;
        }
    }


    public void paintSprite(Graphics g, JPanel panel) {
        if (show) {
            image.paintIcon(panel, g, (int) x, (int) y);
        }
    }


    public void move() {
        x += xDir;
        y += yDir;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
        this.y = y;
    }


    public double getYDir() {
        return yDir;
    }


    public double getXDir() {
        return xDir;
    }


    public void setYDir(double yDir) {
        this.yDir = yDir;
    }


    public void setXDir(double xDir) {
        this.xDir = xDir;
    }

    public int getWidth() {
		if (image == null) {
			return 20;
        } else {
			return image.getIconWidth();
        }
	}

	public int getHeight() {
		if (image == null) {
            return 20;
        } else {
            return image.getIconHeight();
        }
	}

    public String getFileName() {
        return filename;
    }
}



