public class Player {
    private int lives = 3;
    private int evolutionStage = 1;
    private String evolution = "Charmander";
    public Sprite sprite = new Sprite(getEvolution());

    public int getLives(){
        return lives;
    }

    public void loseLife(){
        lives -= 1;

    }

    public void resetPlayer() {
        lives = 3;
    }

    public void evolve() {
        evolutionStage += 1;
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



}
