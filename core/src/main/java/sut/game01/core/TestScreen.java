package sut.game01.core;

import static playn.core.PlayN.*;

import characters.Zealot;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import playn.core.Touch;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

public class TestScreen extends Screen {

    private final ScreenStack ss;
    private final ImageLayer bg;
    private final ImageLayer backButton;
    private Zealot zealot;

    public TestScreen(final ScreenStack ss) {
        this.ss = ss;

        Image bgImage = assets().getImage("images/gameBg.png");
        this.bg = graphics().createImageLayer(bgImage);

        Image backImage = assets().getImage("images/back.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(530,10);
        backButton.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }
        });


    }

    @Override
    public  void  wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(backButton);
        zealot = new Zealot(560f, 280f);
        this.layer.add(zealot.layer());

    }

    @Override
    public void update(int delta) {
        super.update(delta);
        zealot.update(delta);
    }
}
