public class Enemy {
    public int speed = 1;
    private String pokemon;
    public Sprite sprite;

    Enemy(String p, GameLevel lvl) {
        pokemon = p;
        speed = lvl.ordinal()+1;
    }


    public void spawn(int left) {
        sprite = new Sprite(pokemon);
        sprite.spawn(left);
    }

}
