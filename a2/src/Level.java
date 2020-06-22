import java.util.ArrayList;

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

    private final ArrayList<String> LEVEL_1_ENEMIES = new ArrayList<String>() {{
        add("A");
        add("B");
        add("C");
    }};

    Level(GameLevel gameLevel){
        level = gameLevel.ordinal() + 1;
        score = 0;
        for (int i = 0; i < level*15; i++) {
            enemies.add(new Enemy());
        }
    }


}
