public class Player {
    private int lives = 3;
    private int evolutionStage;
    private String evolution = "Charmander";
    public Sprite sprite;

    public int getLives(){
        return lives;
    }

    public void loseLife(){
        lives -= 1;

    }

    public void resetPlayer() {
        lives = 3;
    }

    Player(int stage) {
        evolutionStage = stage;
        switch (evolutionStage){
            case 1:
                evolution="Charmander";
                break;
            case 2:
                evolution="Charmeleon";
                break;
            case 3:
                evolution="Charizard";
                break;
        }
        sprite = new Sprite(getEvolution());
    }

    public String getEvolution() {
        return evolution;
    }

    public void fireLeft() {
        sprite.turnLeft(evolutionStage);
    }
    public void fireRight() {
        sprite.turnRight(evolutionStage);
    }
}
