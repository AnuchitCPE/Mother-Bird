package sut.game01.core;

import static playn.core.PlayN.*;

import playn.core.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import playn.core.Mouse;

public class LevelSelect extends Screen {

    private ScreenStack ss;
    private final GameScreen gameScreen;
    private final ImageLayer levelBg;
    private final ImageLayer startButton;
    private final ImageLayer backButton;
    private final ImageLayer level1Button;
    private final ImageLayer level2Button;
    private final ImageLayer level3Button;

    public LevelSelect(final ScreenStack ss) {
        this.ss = ss;
        this.gameScreen = new GameScreen(ss);

        Image levelBgImage = assets().getImage("images/levelSelect.png");
        this.levelBg = graphics().createImageLayer(levelBgImage);

        Image backButtonImage = assets().getImage("images/backHome.png");
        this.backButton = graphics().createImageLayer(backButtonImage);
        backButton.setTranslation(115,319);
        backButton.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
            }
        });

        Image startButtonImage = assets().getImage("images/backHome.png");
        this.startButton = graphics().createImageLayer(startButtonImage);
        startButton.setTranslation(469,317);
        startButton.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.push(new GameScreen(ss));
            }
        });

        Image level1ButtonImage = assets().getImage("images/level.png");
        this.level1Button = graphics().createImageLayer(level1ButtonImage);
        level1Button.setTranslation(146,203);
        level1Button.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.push(new GameScreen(ss));
            }
        });

        Image level2ButtonImage = assets().getImage("images/level.png");
        this.level2Button = graphics().createImageLayer(level2ButtonImage);
        level2Button.setTranslation(275,203);
        level2Button.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.push(new GameScreen2(ss));
            }
        });

        Image level3ButtonImage = assets().getImage("images/level.png");
        this.level3Button = graphics().createImageLayer(level3ButtonImage);
        level3Button.setTranslation(407,203);
        level3Button.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.push(new GameScreen3(ss));
            }
        });
    }

    @Override
    public  void  wasShown() {
        super.wasShown();
        this.layer.add(levelBg);
        this.layer.add(backButton);
        this.layer.add(startButton);
        this.layer.add(level1Button);
        this.layer.add(level2Button);
        this.layer.add(level3Button);
    }
}