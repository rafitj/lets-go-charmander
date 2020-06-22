import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

enum GameLevel{
    ONE,
    TWO,
    THREE,
    FOUR
}

public class Level {
    private ArrayList<Enemy> enemies;
    public  Player player;

    public int score;
    public int level;
    public double spawnDelay;

    private HashMap<GameLevel, ArrayList<String>> enemy_names;

    private final ArrayList<String> LEVEL_1_ENEMIES = new ArrayList<String>() {{
        add("Geodude");
        add("Grimer");
        add("Magnemite");
    }};


    private final ArrayList<String> LEVEL_2_ENEMIES = new ArrayList<String>() {{
        add("Steelix");
        add("Skarmory");
        add("Forretress");
    }};

    private final ArrayList<String> LEVEL_3_ENEMIES = new ArrayList<String>() {{
        add("Gardevoir");
        add("Salamence");
        add("Ludicolo");
    }};

    Level(GameLevel gameLevel){
        enemies = new ArrayList<>();
        enemy_names = new HashMap<>();
        enemy_names.put(GameLevel.ONE,LEVEL_1_ENEMIES);
        enemy_names.put(GameLevel.TWO,LEVEL_2_ENEMIES);
        enemy_names.put(GameLevel.THREE,LEVEL_3_ENEMIES);
        Random rand = new Random();
        level = gameLevel.ordinal() + 1;
        score = 0;
        player = new Player(level);
        ArrayList<String> lvlEnemies = enemy_names.get(gameLevel);
        for (int i = 0; i < level*15; i++) {
            int randIndx = rand.nextInt(lvlEnemies.size());
            String enemy_name = lvlEnemies.get(randIndx);
            enemies.add(new Enemy(enemy_name, gameLevel));
        }
    }

    public void spawnEnemies() {
        int i = 0;
        Random rand = new Random();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
               enemies.get(i).spawn(rand.nextInt(1));
            }
        };
        timer.start();
    }


    public String getLevelInfo() {
        String region;
        switch (level) {
            case 1:
                region = "Kanto";
                break;
            case 2:
                region = "Jhoto";
                break;
            case 3:
                region = "Hoenn";
            default:
                region = "Legendary";
        }
        return region + " Region";
    }

}
