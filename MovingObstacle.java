import java.util.Random;

class MovingObstacle {
    private Random random = new Random();
    private String type;

    public MovingObstacle(String type) {
        this.type = type;
    }

    public Sprite setObstacle(int landY) {
        int side = random.nextInt(2);
        int speed = random.nextInt(5) + 8;
        speed += (type.equals("bison")) ? 2 : 0;

        Sprite obstacle = new Sprite();
        obstacle.setYDir(2);
        obstacle.setY(landY - 10);

        switch (side) {
            case 0:
                obstacle.setX(900);
                obstacle.setXDir(-(speed));
                obstacle.loadImage(randomObstacle(true));
                break;
            case 1:
                obstacle.setX(-200);
                obstacle.setXDir((speed));
                obstacle.loadImage(randomObstacle(false));
                break;
        }

        return obstacle;
    }

    public String randomObstacle(boolean left) {
        String image = "";

        switch (this.type) {
            case "car":
                int carColor = random.nextInt(4) + 1;

                if (left) {
                    image = "Images/left-cars/car" + carColor + ".png";
                } else {
                    image = "Images/right-cars/car" + carColor + ".png";
                }
                break;
            case "bison":
                if (left) {
                    image = "Images/bison/bisonLeft.png";
                } else {
                    image = "Images/bison/bisonRight.png";
                }
                break;
        }
        return image;
    }
}



