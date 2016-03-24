package sut.game01.core;

import static playn.core.PlayN.*;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import playn.core.Mouse;
import tripleplay.ui.*;
import  tripleplay.ui.layout.*;

public class HomeScreen extends Screen {

    private ScreenStack ss;
    private final TestScreen testScreen;
    private final ImageLayer homeBg;
    private final ImageLayer startButton;
    private final ImageLayer logo;

    public HomeScreen(final ScreenStack ss) {
        this.ss = ss;
        this.testScreen = new TestScreen(ss);
        Image homeBgImage = assets().getImage("images/homeBg.png");
        this.homeBg = graphics().createImageLayer(homeBgImage);

        Image logoImage = assets().getImage("images/logo.png");
        this.logo = graphics().createImageLayer(logoImage);
        logo.setTranslation(110,40);

        Image startButtonImage = assets().getImage("images/start.png");
        this.startButton = graphics().createImageLayer(startButtonImage);
        startButton.setTranslation(240,375);
        startButton.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(testScreen);
            }
        });
    }

    @Override
    public  void  wasShown() {
        super.wasShown();
        this.layer.add(homeBg);
        this.layer.add(logo);
        this.layer.add(startButton);
    }
}