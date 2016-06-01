package sut.game01.core;

import static playn.core.PlayN.*;

import playn.core.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import playn.core.Mouse;

public class HomeScreen extends Screen {

    private ScreenStack ss;
    private final GameScreen gameScreen;
    private final LevelSelect levelSelect;
    private final ImageLayer homeBg;
    private final ImageLayer startButton;
    private final ImageLayer logo;
    private final ImageLayer creditButton;
    private final ImageLayer creditBg;
    private final ImageLayer backButton;

    public HomeScreen(final ScreenStack ss) {
        this.ss = ss;
        this.gameScreen = new GameScreen(ss);
        this.levelSelect = new LevelSelect(ss);

        Image homeBgImage = assets().getImage("images/homeBg.png");
        this.homeBg = graphics().createImageLayer(homeBgImage);

        Image logoImage = assets().getImage("images/logo.png");
        this.logo = graphics().createImageLayer(logoImage);
        logo.setTranslation(110,40);

        Image startButtonImage = assets().getImage("images/startButton.png");
        this.startButton = graphics().createImageLayer(startButtonImage);
        startButton.setTranslation(240,320);
        startButton.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.push(new LevelSelect(ss));
            }
        });

        Image creditButtonImage = assets().getImage("images/creditButton.png");
        this.creditButton = graphics().createImageLayer(creditButtonImage);
        creditButton.setTranslation(240,380);
        creditButton.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                layer.add(creditBg);
                layer.add(backButton);
            }
        });

        Image creditBgImage = assets().getImage("images/creditBg.png");
        this.creditBg = graphics().createImageLayer(creditBgImage);

        Image backButtonImage = assets().getImage("images/backButton.png");
        this.backButton = graphics().createImageLayer(backButtonImage);
        backButton.setTranslation(285,347);
        backButton.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                layer.remove(creditBg);
                layer.remove(backButton);
            }
        });
    }

    @Override
    public  void  wasShown() {
        super.wasShown();
        this.layer.add(homeBg);
        this.layer.add(logo);
        this.layer.add(startButton);
        this.layer.add(creditButton);
        /*keyboard().setListener(new Keyboard.Adapter(){
            @Override
            public void onKeyUp(Keyboard.Event event) {
                if (event.key() == Key.ENTER) {
                    ss.push(gameScreen);
                }
            }
        });*/
    }
}