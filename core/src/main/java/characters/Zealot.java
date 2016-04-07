package characters;

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import sprite.Sprite;
import sprite.SpriteLoader;

public class Zealot {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private int count = 0;

    public enum State {
        IDLE, RUN, ATTK
    };

    private  State state = State.IDLE;

    private  int e = 0;
    private  int offset = 0;

    public void setCount(int count) {
        this.count = count;
    }

    public Zealot(final float x, final float y) {
        sprite = SpriteLoader.getSprite("images/zealot.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
                sprite.layer().setTranslation(x, y + 13f);
                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });
    }

    public Layer layer() {
        return sprite.layer();
    }

    public  void update(int delta) {
        if (hasLoaded == false) return;
        e = e + delta;
        switch (count%3) {
            case 0: state = State.IDLE; break;
            case 1: state = State.RUN; break;
            case 2: state = State.ATTK; break;
        }
        if (e > 150) {
            switch (state) {
                case IDLE: offset = 0; break;
                case RUN: offset = 4; break;
                case ATTK: offset = 8; break;
            }
            spriteIndex = offset + ((spriteIndex + 1) % 4);
            sprite.setSprite(spriteIndex);
            e = 0;
        }
    }
}
