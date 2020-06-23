import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private HashMap<GameLevel, ArrayList<Image>> enemy_map;

    private static Image loadPokemonImg(String pokemon){
        return new Image("assets/pokemon/"+pokemon+".gif",250,250,true,true);
    }


    private final static ArrayList<Image> LEVEL_1_ENEMIES = new ArrayList<Image>(List.of(loadPokemonImg("Geodude"),
                                                         loadPokemonImg("Grimer"),loadPokemonImg("Magnemite")));

    private final static ArrayList<Image> LEVEL_2_ENEMIES = new ArrayList<Image>(List.of(loadPokemonImg("Steelix"),
                                                            loadPokemonImg("Forretress"),loadPokemonImg("Skarmory")));

    private final static ArrayList<Image> LEVEL_3_ENEMIES = new ArrayList<Image>(List.of(loadPokemonImg("Gardevoir"),
                                                        loadPokemonImg("Salamence"),loadPokemonImg("Ludicolo")));

    Level(){
        enemies = new ArrayList<>();
        enemy_map = new HashMap<>();
        enemy_map.put(GameLevel.ONE,LEVEL_1_ENEMIES);
        enemy_map.put(GameLevel.TWO,LEVEL_2_ENEMIES);
        enemy_map.put(GameLevel.THREE,LEVEL_3_ENEMIES);
        level = 1;
        score = 0;
        player = new Player(level);
        player.setEnemies(enemies);
    }

    public void newLevel(GameLevel gameLevel, Group parentGroup) {
        level = gameLevel.ordinal()+1;
        Random rand = new Random();
        ArrayList<Image> lvlEnemies = enemy_map.get(gameLevel);
        for (int i = 0; i < level*10; i++) {
            int randIndx = rand.nextInt(lvlEnemies.size());
            Image enemy_img = lvlEnemies.get(randIndx);
            enemies.add(new Enemy(enemy_img, gameLevel, player, parentGroup));
        }
    }


    public void spawnEnemies(Group g) {
        Random rand = new Random();
        AnimationTimer timer = new AnimationTimer() {
            private int i = 0;
            private long lastSpawn = 0 ;
            @Override
            public void handle(long now) {
                if (i == enemies.size()){
                    this.stop();
                } else if (now/1000000000 - lastSpawn/1000000000 >= (5-level)) {
                    enemies.get(i).spawn(rand.nextBoolean(), g);
                    i+=1;
                    lastSpawn = now;
                }
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
