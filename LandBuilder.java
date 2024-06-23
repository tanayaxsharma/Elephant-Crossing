import java.util.Random;


class LandBuilder {
    private static final int SIZE = 8;
    private static final String ROAD_IMAGE = "Images/environment/road.png";
    private static final String GRASS_IMAGE = "Images/environment/grass.jpg";
    private static final String WATER_IMAGE = "Images/environment/water.png";
    private static final String TREE_IMAGE = "Images/environment/tree.png";
    private static final String SHRUB_IMAGE = "Images/environment/shrub.png";
    private static final String SAND_IMAGE = "Images/environment/sand.png";
    private static final String LOTUS_IMAGE = "Images/environment/lotus.png";


    private Random random = new Random();

    public Sprite[] getRow() {
        int type = random.nextInt(0, 4);

        switch (type) {
            case 0:
                return buildLand(SIZE, ROAD_IMAGE);
            case 1:
                return buildLand(SIZE, SAND_IMAGE);
            case 2:
                return buildMixedLand(SIZE, GRASS_IMAGE, TREE_IMAGE, SHRUB_IMAGE);
            case 3:
                return buildMixedLand(SIZE, WATER_IMAGE, LOTUS_IMAGE, WATER_IMAGE);
            default:
                return new Sprite[0];
        }
    }


    private Sprite[] buildLand(int size, String image) {
        Sprite[] land = new Sprite[size];

        for (int i = 0; i < size; i++) {
            land[i] = new Sprite(image);
        }

        return land;
    }


    private Sprite[] buildMixedLand(int size, String standard, String mixed1, String mixed2) {
        Sprite[] land = new Sprite[size];

        for (int i = 0; i < size; i++) {
            int type = random.nextInt(6);
            land[i] = buildSingleBlock(type, standard, mixed1, mixed2);
        }

        return land;
    }


    private Sprite buildSingleBlock(int type, String standard, String mixed1, String mixed2) {
        Sprite block = new Sprite();

        switch (type) {
            case 0:
                block = new Sprite(standard);
                break;
            case 1:
                block = new Sprite(standard);
                break;
            case 2:
                block = new Sprite(standard);
                break;
            case 3:
                block = new Sprite(mixed1);
                break;
            case 4:
                block = new Sprite(mixed1);
                break;
            case 5:
                block = new Sprite(mixed2);
                break;
        }

        return block;
    }


    public Sprite[] buildMixedGrass() {
        return buildMixedLand(SIZE, GRASS_IMAGE, TREE_IMAGE, SHRUB_IMAGE);
    }

    public Sprite[] buildMixedWater() {
        return buildMixedLand(SIZE, WATER_IMAGE, LOTUS_IMAGE, WATER_IMAGE);
    }

    public static String getLandType(Sprite sprite) {
        String fileName = sprite.getFileName();
        switch (fileName) {
            case WATER_IMAGE:
            case LOTUS_IMAGE:
                return "water";
            case GRASS_IMAGE:
            case SHRUB_IMAGE:
            case TREE_IMAGE:
                return "grass";
            case SAND_IMAGE:
                return "sand";
            default:
                return "road";
        }
    }
}
