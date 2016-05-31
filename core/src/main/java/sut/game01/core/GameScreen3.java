package sut.game01.core;

import static playn.core.PlayN.*;

import characters.Bird;
import characters.Food;
import characters.Rock;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.DebugDrawBox2D;
import org.jbox2d.common.Vec2;
import playn.core.*;
import playn.core.util.Clock;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameScreen3 extends Screen {

    public static float M_PER_PIXEL = 1 / 26.666667f;  //size of world
    public static int width = 24; // 640 px in physic unit (meter)
    public static int height = 18; // 480 px in physic unit (meter)

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true;
    private boolean pause = false;
    private final ScreenStack ss;
    private final ImageLayer bg;
    private final ImageLayer settingButton;
    private final ImageLayer star0;
    private final ImageLayer star1;
    private final ImageLayer star2;
    private final ImageLayer star3;
    private final ImageLayer paused;
    private final ImageLayer resume;
    private final ImageLayer replay;
    private final ImageLayer replay1;
    private final ImageLayer menu;
    private final ImageLayer menu1;
    private final ImageLayer nextLevel;
    private final ImageLayer overMenu;
    private final ImageLayer overReplay;
    private final ImageLayer gameOver;
    private final ImageLayer cleared1;
    private final ImageLayer cleared2;
    private final ImageLayer cleared3;
    private Bird bird;
    private List<Food> foodMap;
    private List<Rock> rockMap;
    private int i = -1;
    public static HashMap<Object,String> bodies = new HashMap<Object, String>();
    public static int k = 0;
    public static int f = -2;
    public static int g = 0;
    public static int j = 0;
    public static float x = 0f;
    public float b = 0f;
    public float c = 0f;
    public static String debugString = "";
    public static String debugStringCoin = "";

    public GameScreen3(final ScreenStack ss) {
        this.ss = ss;
        graphics().rootLayer().clear();
        foodMap = new ArrayList<Food>();
        rockMap = new ArrayList<Rock>();
        Image bgImage = assets().getImage("images/gameBg3.png");
        this.bg = graphics().createImageLayer(bgImage);

        Image settingImage = assets().getImage("images/setting.png");
        this.settingButton = graphics().createImageLayer(settingImage);
        settingButton.setTranslation(550,10);
        settingButton.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                /*ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
                j = 0;
                k = 0;
                x = 0f;*/
                pause = true;
                layer.add(paused);
                layer.add(resume);
                layer.add(replay);
                layer.add(menu);
            }
        });


        Image star0Image = assets().getImage("images/star0.png");
        this.star0 = graphics().createImageLayer(star0Image);
        star0.setTranslation(230,15);

        Image star1Image = assets().getImage("images/star1.png");
        this.star1 = graphics().createImageLayer(star1Image);
        star1.setTranslation(230,15);

        Image star2Image = assets().getImage("images/star2.png");
        this.star2 = graphics().createImageLayer(star2Image);
        star2.setTranslation(230,15);

        Image star3Image = assets().getImage("images/star3.png");
        this.star3 = graphics().createImageLayer(star3Image);
        star3.setTranslation(230,15);

        Image pausedImage = assets().getImage("images/paused.png");
        this.paused = graphics().createImageLayer(pausedImage);
        //paused.setTranslation(150,150);

        Image resumeImage = assets().getImage("images/resume.png");
        this.resume = graphics().createImageLayer(resumeImage);
        resume.setTranslation(205,210);
        resume.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                /*ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
                j = 0;
                k = 0;
                x = 0f;*/
                layer.remove(paused);
                layer.remove(resume);
                layer.remove(replay);
                layer.remove(menu);
                pause = false;
            }
        });

        Image replayImage = assets().getImage("images/replay.png");
        this.replay = graphics().createImageLayer(replayImage);
        replay.setTranslation(280,210);
        replay.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                j = 0;
                k = 0;
                x = 0f;
                pause = false;
                ss.remove(ss.top());
                ss.push(new GameScreen3(ss));
            }
        });

        Image menuImage = assets().getImage("images/menu.png");
        this.menu = graphics().createImageLayer(menuImage);
        menu.setTranslation(360,210);
        menu.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                j = 0;
                k = 0;
                x = 0f;
                pause = false;
                ss.remove(ss.top());
                ss.push(new LevelSelect(ss));
            }
        });


        Image menu1Image = assets().getImage("images/replay1.png");
        this.menu1 = graphics().createImageLayer(menu1Image);
        menu1.setTranslation(247,343);
        menu1.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                j = 0;
                k = 0;
                x = 0f;
                pause = false;
                ss.remove(ss.top());
                ss.push(new LevelSelect(ss));
            }
        });

        Image replay1Image = assets().getImage("images/replay1.png");
        this.replay1 = graphics().createImageLayer(replay1Image);
        replay1.setTranslation(309,343);
        replay1.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                j = 0;
                k = 0;
                x = 0f;
                pause = false;
                ss.remove(ss.top());
                ss.push(new GameScreen3(ss));
            }
        });

        Image nextLevelImage = assets().getImage("images/replay1.png");
        this.nextLevel = graphics().createImageLayer(nextLevelImage);
        nextLevel.setTranslation(373,343);
        nextLevel.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                j = 0;
                k = 0;
                x = 0f;
                pause = false;
                ss.remove(ss.top());
                ss.push(new LevelSelect(ss));
            }
        });

        Image overMenuImage = assets().getImage("images/over.png");
        this.overMenu = graphics().createImageLayer(overMenuImage);
        overMenu.setTranslation(261,338);
        overMenu.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                j = 0;
                k = 0;
                x = 0f;
                pause = false;
                ss.remove(ss.top());
                ss.push(new LevelSelect(ss));
            }
        });


        Image overReplayImage = assets().getImage("images/over.png");
        this.overReplay = graphics().createImageLayer(overReplayImage);
        overReplay.setTranslation(340,338);
        overReplay.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                j = 0;
                k = 0;
                x = 0f;
                pause = false;
                ss.remove(ss.top());
                ss.push(new GameScreen3(ss));
            }
        });



        Image gameOverImage = assets().getImage("images/gameOver.png");
        this.gameOver = graphics().createImageLayer(gameOverImage);

        Image cleared1Image = assets().getImage("images/cleared1.png");
        this.cleared1 = graphics().createImageLayer(cleared1Image);

        Image cleared2Image = assets().getImage("images/cleared2.png");
        this.cleared2 = graphics().createImageLayer(cleared2Image);

        Image cleared3Image = assets().getImage("images/cleared3.png");
        this.cleared3 = graphics().createImageLayer(cleared3Image);

        Vec2 gravity = new Vec2(0.0f , 10.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);


        foodMap.add(new Food(world, 500f, 350f));
        foodMap.add(new Food(world, 800f, 100f));
        foodMap.add(new Food(world, 1650f, 325f));

        rockMap.add(new Rock(world, 200f, 100f));
        rockMap.add(new Rock(world, 200f, 250f));
        rockMap.add(new Rock(world, 200f, 400f));
        rockMap.add(new Rock(world, 300f, 100f));
        rockMap.add(new Rock(world, 300f, 250f));
        rockMap.add(new Rock(world, 300f, 400f));
        rockMap.add(new Rock(world, 400f, 100f));
        rockMap.add(new Rock(world, 400f, 250f));
        rockMap.add(new Rock(world, 400f, 400f));

        rockMap.add(new Rock(world, 600f, 100f));
        rockMap.add(new Rock(world, 600f, 175f));
        rockMap.add(new Rock(world, 600f, 250f));
        rockMap.add(new Rock(world, 800f, 250f));
        rockMap.add(new Rock(world, 800f, 325f));
        rockMap.add(new Rock(world, 800f, 400f));
        rockMap.add(new Rock(world, 1000f, 100f));
        rockMap.add(new Rock(world, 1000f, 175f));
        rockMap.add(new Rock(world, 1000f, 250f));

        rockMap.add(new Rock(world, 1200f, 100f));
        rockMap.add(new Rock(world, 1250f, 175f));
        rockMap.add(new Rock(world, 1350f, 325f));
        rockMap.add(new Rock(world, 1400f, 400f));

        rockMap.add(new Rock(world, 1500f, 100f));
        rockMap.add(new Rock(world, 1550f, 175f));
        rockMap.add(new Rock(world, 1600f, 250f));
        rockMap.add(new Rock(world, 1700f, 400f));

        rockMap.add(new Rock(world, 1800f, 100f));
        rockMap.add(new Rock(world, 1900f, 250f));
        rockMap.add(new Rock(world, 1950f, 325f));
        rockMap.add(new Rock(world, 2000f, 400f));


        bird = new Bird(world, 100f, 100f);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                for (Food food : foodMap) {
                    if ((contact.getFixtureA().getBody() == bird.getBody() && contact.getFixtureB().getBody() == food.getBody())) {
                        j = j + 10;
                        debugString = bodies.get(a) + " contacted with " + bodies.get(b);
                        debugStringCoin = "Point : " + j;
                        b.setActive(false);
                        food.layer().setVisible(false);
                    }
                    if ((contact.getFixtureA().getBody() == food.getBody() && contact.getFixtureB().getBody() == bird.getBody())) {
                        j = j + 10;
                        debugString = bodies.get(a) + " contacted with " + bodies.get(b);
                        debugStringCoin = "Point : " + j;
                        a.setActive(false);
                        food.layer().setVisible(false);
                    }
                }

                for (Rock rock : rockMap) {
                    if ((contact.getFixtureA().getBody() == bird.getBody() && contact.getFixtureB().getBody() == rock.getBody())) {
                        debugString = bodies.get(a) + " contacted with " + bodies.get(b);
                        debugStringCoin = "Point : " + j;
                        b.setActive(false);
                        rock.layer().setVisible(false);
                        bird.die();
                        for(Rock rock1 : rockMap){
                            rock1.getBody().setActive(false);
                        }
                        for(Food food1 : foodMap){
                            food1.getBody().setActive(false);
                        }
                    }
                    if ((contact.getFixtureA().getBody() == rock.getBody() && contact.getFixtureB().getBody() == bird.getBody())) {
                        debugString = bodies.get(a) + " contacted with " + bodies.get(b);
                        debugStringCoin = "Point : " + j;
                        a.setActive(false);
                        rock.layer().setVisible(false);
                        bird.die();
                        for(Rock rock1 : rockMap){
                            rock1.getBody().setActive(false);
                        }
                        for(Food food1 : foodMap){
                            food1.getBody().setActive(false);
                        }
                    }
                }
            }
            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });

        /*Random rand = new Random();
        int nRand = rand.nextInt(3) +1;
        if(nRand ==1)
            createFood(f1);
        else if(nRand ==2)
            createCan(canNum);
        else if(nRand ==3)
            createBottleGlass(bottleGlassNum);*/
    }

    @Override
    public  void  wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(settingButton);
        this.layer.add(bird.layer());

        for (Food food : foodMap){
            this.layer.add(food.layer());
        }

        for (Rock rock : rockMap){
            this.layer.add(rock.layer());
        }

        /*backButton.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
                j = 0;
                k = 0;
                x = 0f;
                layer.add(paused);
            }
        });*/

        if (showDebugDraw) {
            CanvasImage image = graphics().createImage(
                    (int) (width / GameScreen.M_PER_PIXEL),
                    (int) (height / GameScreen.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags( debugDraw.e_shapeBit |
                    debugDraw.e_jointBit |
                    debugDraw.e_aabbBit);
            debugDraw.setCamera(0, 0, 1f / GameScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }

        Body ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(0, height), new Vec2(width, height));
        ground.createFixture(groundShape, 0.0f);
        bodies.put(ground,"ground");

        Body groundT = world.createBody(new BodyDef());
        EdgeShape groundTShape = new EdgeShape();
        groundTShape.set(new Vec2(0, 0), new Vec2(24,0));
        groundT.createFixture(groundTShape, 0.0f);

        /*Body groundL = world.createBody(new BodyDef());
        EdgeShape groundLShape = new EdgeShape();
        groundLShape.set(new Vec2(0, 0), new Vec2(0,18));
        groundL.createFixture(groundLShape, 0.0f);*/

       /*Body groundR = world.createBody(new BodyDef());
        EdgeShape groundRShape = new EdgeShape();
        groundRShape.set(new Vec2(24, 0), new Vec2(24,18));
        groundR.createFixture(groundRShape, 0.0f);*/

    }

    @Override
    public void update(int delta) {
        if (b < 27) {
            if (x > -1250f && bird.state != Bird.State.DIE && pause == false) {
                x -= 0.3f * 5;
                bg.setTranslation(x, 0);
            } else if (x < -1250f){
                bird.getBody().applyForce(new Vec2(120f, 0f), bird.getBody().getPosition());
                b = bird.getBody().getPosition().x;
            }
        } else if (b > 27) {
            pause = true;
            if (j == 0) {
                this.layer.add(gameOver);
                this.layer.add(overMenu);
                this.layer.add(overReplay);
            } else if (j == 10) {
                this.layer.add(cleared1);
                this.layer.add(menu1);
                this.layer.add(replay1);
                this.layer.add(nextLevel);
            } else if (j == 20) {
                this.layer.add(cleared2);
                this.layer.add(menu1);
                this.layer.add(replay1);
                this.layer.add(nextLevel);
            } else if (j == 30) {
                this.layer.add(cleared3);
                this.layer.add(menu1);
                this.layer.add(replay1);
                this.layer.add(nextLevel);
            }
        }

        if (bird.state == Bird.State.DIE){
            c = bird.getBody().getPosition().y;
            if (c > 17) {
                this.layer.add(gameOver);
                this.layer.add(overMenu);
                this.layer.add(overReplay);
            }
        }

        if (pause == false) {
            super.update(delta);
            bird.update(delta);

            for (Food food : foodMap) {
                food.update(delta);
            }

            for (Rock rock : rockMap) {
                rock.update(delta);
            }

            world.step(0.033f, 10, 10);


            if (j == 0) {
                this.layer.add(star0);
            } else if (j == 10) {
                this.layer.add(star1);
            } else if (j == 20) {
                this.layer.add(star2);
            } else if (j == 30) {
                this.layer.add(star3);
            }
        }
    }

    @Override
    public void paint(Clock clock) {
        if (pause == false) {
            super.paint(clock);
            bird.paint(clock);
            for (Food food : foodMap) {
                food.paint(clock);
            }
            for (Rock rock : rockMap) {
                rock.paint(clock);
            }
        }

        if (showDebugDraw) {
            debugDraw.getCanvas().clear();
            debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
            debugDraw.getCanvas().drawText(debugString,50f,50f);
            debugDraw.getCanvas().drawText(debugStringCoin,50f,100f);
            world.drawDebugData();
        }
    }
}
