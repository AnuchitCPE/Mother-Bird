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

public class GameScreen extends Screen {

    public static float M_PER_PIXEL = 1 / 26.666667f;  //size of world
    public static int width = 24; // 640 px in physic unit (meter)
    public static int height = 18; // 480 px in physic unit (meter)

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true;
    private final ScreenStack ss;
    private final ImageLayer bg;
    private final ImageLayer backButton;
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
    public static String debugString = "";
    public static String debugStringCoin = "";

    public GameScreen(final ScreenStack ss) {
        this.ss = ss;
        graphics().rootLayer().clear();
        foodMap = new ArrayList<Food>();
        rockMap = new ArrayList<Rock>();
        Image bgImage = assets().getImage("images/gameBg.png");
        this.bg = graphics().createImageLayer(bgImage);

        Image backImage = assets().getImage("images/back.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(530,10);
        backButton.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
                j = 0;
                k = 0;
                x = 0f;
            }
        });



        Vec2 gravity = new Vec2(0.0f , 10.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        foodMap.add(new Food(world, 900f, 240f));
        foodMap.add(new Food(world, 1200f, 100f));
        foodMap.add(new Food(world, 300f, 400f));

        rockMap.add(new Rock(world, 700f, 230f));
        rockMap.add(new Rock(world, 1100f, 110f));
        rockMap.add(new Rock(world, 200f, 420f));
        //rockMap.add(new Rock(world, 450f, 110f));
        //rockMap.add(new Rock(world, 700f, 320f));
        rockMap.add(new Rock(world, 550f, 275f));
        rockMap.add(new Rock(world, 1350f, 333f));
        rockMap.add(new Rock(world, 975f, 123f));
        rockMap.add(new Rock(world, 489f, 450f));
        rockMap.add(new Rock(world, 200f, 200f));
        //rockMap.add(new Rock(world, 450f, 300f));
        rockMap.add(new Rock(world, 550f, 400f));
        rockMap.add(new Rock(world, 750f, 100f));
        //rockMap.add(new Rock(world, 8050f, 250f));
        rockMap.add(new Rock(world, 1050f, 370f));
        rockMap.add(new Rock(world, 1150f, 420f));
        rockMap.add(new Rock(world, 1550f, 160f));

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
                        //bird.die();
                        //a.setActive(false);
                    }
                    if ((contact.getFixtureA().getBody() == food.getBody() && contact.getFixtureB().getBody() == bird.getBody())) {
                        j = j + 10;
                        debugString = bodies.get(a) + " contacted with " + bodies.get(b);
                        debugStringCoin = "Point : " + j;
                        a.setActive(false);
                        food.layer().setVisible(false);
                        //bird.die();
                        //b.setActive(false);
                    }
                }

                for (Rock rock : rockMap) {
                    if ((contact.getFixtureA().getBody() == bird.getBody() && contact.getFixtureB().getBody() == rock.getBody())) {
                        //j = j + 10;
                        debugString = bodies.get(a) + " contacted with " + bodies.get(b);
                        debugStringCoin = "Point : " + j;
                        b.setActive(false);
                        rock.layer().setVisible(false);
                        bird.die();
                        //a.setActive(false);
                    }
                    if ((contact.getFixtureA().getBody() == rock.getBody() && contact.getFixtureB().getBody() == bird.getBody())) {
                        //j = j + 10;
                        debugString = bodies.get(a) + " contacted with " + bodies.get(b);
                        debugStringCoin = "Point : " + j;
                        a.setActive(false);
                        rock.layer().setVisible(false);
                        bird.die();
                        //b.setActive(false);
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
        this.layer.add(backButton);
        this.layer.add(bird.layer());

        for (Food food : foodMap){
            this.layer.add(food.layer());
        }

        for (Rock rock : rockMap){
            this.layer.add(rock.layer());
        }

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
        super.update(delta);

        if (x > -1250f){
            x -= 0.3f * 5;
            bg.setTranslation(x,0);
        }
        bird.update(delta);

        for (Food food : foodMap){
            food.update(delta);
        }

        for (Rock rock : rockMap){
            rock.update(delta);
        }

        world.step(0.033f, 10, 10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        bird.paint(clock);
        for (Food food : foodMap){
            food.paint(clock);
        }
        for (Rock rock : rockMap){
            rock.paint(clock);
        }

        if (showDebugDraw) {
            debugDraw.getCanvas().clear();
            debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
            debugDraw.getCanvas().drawText(debugString,100f,50f);
            debugDraw.getCanvas().drawText(debugStringCoin,100f,100f);
            world.drawDebugData();
        }
    }
}
