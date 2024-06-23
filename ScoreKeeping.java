import java.io.*;
import java.util.Properties;

public class ScoreKeeping {

    private static final String SCORE_FILE = "Score.properties";
    private static final String SCORE_KEY = "highScore";

    public ScoreKeeping() {
        if (!new File(SCORE_FILE).exists()) {
            writeScore(0);
        }
    }

    public void updateScores(int score) {
        int fileScore = readScore();
        if (score > fileScore) {
            writeScore(score);
        }
    }

    public int readScore() {
        Properties properties = new Properties();
        int fileScore = 0;

        try (FileInputStream fis = new FileInputStream(SCORE_FILE)) {
            properties.load(fis);
            fileScore = Integer.parseInt(properties.getProperty
            (SCORE_KEY, "0"));
        } catch (FileNotFoundException e) {
            writeScore(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileScore;
    }

    private void writeScore(int score) {
        Properties properties = new Properties();
        properties.setProperty(SCORE_KEY, String.valueOf(score));

        try (FileOutputStream fos = new FileOutputStream(SCORE_FILE)) {
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
