import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

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
    private GameLevel gameLevel;
    private Text enemiesLeftText;
    private MiniGame game;

    private HashMap<GameLevel, ArrayList<Image>> enemy_map;
    private HashMap<Image, Pair<Integer, Integer>> sprite_offsets;

    private static Image loadPokemonImg(String pokemon, double size){
        return new Image("assets/pokemon/"+pokemon+".gif",size,size,true,true);
    }


    private final static ArrayList<Image> LEVEL_1_ENEMIES = new ArrayList<Image>(List.of(loadPokemonImg("Geodude", 250),
                                                         loadPokemonImg("Grimer", 210),loadPokemonImg("Magnemite", 190)));

    private final static ArrayList<Image> LEVEL_2_ENEMIES = new ArrayList<Image>(List.of(loadPokemonImg("Steelix", 400),
                                                            loadPokemonImg("Forretress", 240),loadPokemonImg("Skarmory", 400)));

    private final static ArrayList<Image> LEVEL_3_ENEMIES = new ArrayList<Image>(List.of(loadPokemonImg("Gardevoir", 300),
                                                        loadPokemonImg("Salamence", 300),loadPokemonImg("Ludicolo", 300)));

    Level(){
        enemies = new ArrayList<>();
        enemy_map = new HashMap<>();
        enemy_map.put(GameLevel.ONE,LEVEL_1_ENEMIES);
        enemy_map.put(GameLevel.TWO,LEVEL_2_ENEMIES);
        enemy_map.put(GameLevel.THREE,LEVEL_3_ENEMIES);
        sprite_offsets = new HashMap<>();
        sprite_offsets.put(LEVEL_1_ENEMIES.get(0),new Pair<>(0,0));
        sprite_offsets.put(LEVEL_1_ENEMIES.get(1),new Pair<>(0,0));
        sprite_offsets.put(LEVEL_1_ENEMIES.get(2),new Pair<>(0,0));
        sprite_offsets.put(LEVEL_2_ENEMIES.get(0),new Pair<>(0,-200));
        sprite_offsets.put(LEVEL_2_ENEMIES.get(1),new Pair<>(0,0));
        sprite_offsets.put(LEVEL_2_ENEMIES.get(2),new Pair<>(0,-100));
        sprite_offsets.put(LEVEL_3_ENEMIES.get(0),new Pair<>(0,0));
        sprite_offsets.put(LEVEL_3_ENEMIES.get(1),new Pair<>(0,0));
        sprite_offsets.put(LEVEL_3_ENEMIES.get(2),new Pair<>(0,0));
        level = 1;
        score = 0;
    }


    public void updateEnemyCount(Enemy e) {
       enemies.remove(e);
       enemiesLeftText.setText(enemies.size() + " Pokemon Left");
       if (enemies.size() == 0) {
           if(level == 3) {
               game.setGamestate(GameState.GAME_WIN);
           } else {
               game.setGamestate(GameState.LEVEL_COMPLETE);

           }
          game.updateStage();
       }
    }

    public Group getLevelEnemiesSprites() {
        Group container = new Group();
        ArrayList<Image> lvlEnemies = enemy_map.get(gameLevel);
        int i = 0;
        for (Image enemyImage : lvlEnemies) {
            ImageView iv = new ImageView(enemyImage);
            iv.setTranslateX(i*250 + 300);
            i+=1;
            container.getChildren().add(iv);
        }
        container.setTranslateX(-75);
        container.setLayoutY(200);
        return container;
    }

    public Group getEnemiesLeft(){
        Group g = new Group();
        enemiesLeftText = new Text(enemies.size() + " Pokemon Left");
        enemiesLeftText.setFill(Color.BLACK);
        enemiesLeftText.setOpacity(0.9);
        enemiesLeftText.setStyle("-fx-font: 30 arial; -fx-font-weight: bold;");
        enemiesLeftText.setLayoutX(1000);
        enemiesLeftText.setLayoutY(670);
        g.getChildren().add(enemiesLeftText);
        return  g;
    }

    public void newLevel(MiniGame miniGame,GameLevel gLvl, Group parentGroup) {
        gameLevel = gLvl;
        game = miniGame;
        level = gLvl.ordinal()+1;
        player = new Player(miniGame, level);
        Random rand = new Random();
        ArrayList<Image> lvlEnemies = enemy_map.get(gameLevel);
        enemies.clear();
        for (int i = 0; i < level*10; i++) {
            int randIndx = rand.nextInt(lvlEnemies.size());
            Image enemy_img = lvlEnemies.get(randIndx);
            Pair<Integer,Integer> offsets = sprite_offsets.get(enemy_img);
            enemies.add(new Enemy(enemy_img, this, player, parentGroup, offsets.getKey(), offsets.getValue() ));
        }
        player.setEnemies(enemies);
    }


    public void spawnEnemies(Group g) {
        int numEnemies = enemies.size();
        ArrayList<Enemy> enemiesToSpawn = (ArrayList<Enemy>)enemies.clone();
        Random rand = new Random();
        AnimationTimer timer = new AnimationTimer() {
            private int i = 0;
            private long lastSpawn = 0 ;
            @Override
            public void handle(long now) {
                if (i == numEnemies){
                    this.stop();
                } else if (now/1000000000 - lastSpawn/1000000000 >= 0.5+(6-(level*2))) {
                    enemiesToSpawn.get(i).spawn(rand.nextBoolean(), g);
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
