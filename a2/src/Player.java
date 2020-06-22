public class Player {
    private int lives = 3;

    public int getLives(){
        return lives;
    }

    public void loseLife(){
        lives = lives-1;
    }

    public void resetPlayer() {
        lives = 3;
    }
}
