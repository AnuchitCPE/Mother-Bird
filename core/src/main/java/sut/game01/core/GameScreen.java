package sut.game01.core;

import static playn.core.PlayN.*;

import characters.Bird;
import characters.Food;
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
    //private final ImageLayer food;
    private Bird bird;
    private Food food1;
    private Food food2;
    private Food food3;
    private List<Bird> birdMap;
    private int i = -1;
    public static HashMap<Body,String> bodies = new HashMap<Body, String>();
    public static int k = 0;
    public static int f = -2;
    public static int f1 = 0;
    public static int j = 0;
    public static float x = 0f;
    public static String debugString = "";
    public static String debugStringCoin = "";

    public GameScreen(final ScreenStack ss) {
        this.ss = ss;
        graphics().rootLayer().clear();
        birdMap = new ArrayList<Bird>();
        Image bgImage = assets().getImage("images/gameBg.png");
        this.bg = graphics().createImageLayer(bgImage);

        /*Image foodImage = assets().getImage("images/food.png");
        this.food = graphics().createImageLayer(foodImage);
        food.setTranslation(300,220);*/


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

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                if (bodies.get(a) != null){
                    j = j + 10;
                    debugString = bodies.get(a) + " contacted with " + bodies.get(b);
                    debugStringCoin = "Point : " + j;
                    //bird.state = Bird.State.DIE;
                    b.applyForce(new Vec2(80f, -100f), b.getPosition());
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
        food1 = new Food(world, 600f, 240f);
        food2 = new Food(world, 1200f, 100f);
        food3 = new Food(world, 300f, 400f);
        bird = new Bird(world, 50f, 100f);

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

        this.layer.add(food1.layer());
        this.layer.add(food2.layer());
        this.layer.add(food3.layer());

        mouse().setListener(new Mouse.Adapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                birdMap.add(new Bird(world, (float)event.x(), (float)event.y()));
                i++;
                for (int c = 0 ; c <= i ; c++){
                    graphics().rootLayer().add(birdMap.get(c).layer());
                }

            }
        });

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

        /*Body foodCircle = world.createBody(new BodyDef());
        CircleShape foodCircleShape = new CircleShape();
        foodCircleShape.setRadius(0.85f);
        foodCircleShape.m_p.set(12f,9f);
        foodCircle.createFixture(foodCircleShape, 0.0f);
        bodies.put(foodCircle,"food");*/

    }

    @Override
    public void update(int delta) {
        super.update(delta);
        for (int c = 0 ; c <= i ; c++){
            birdMap.get(c).update(delta);
        }

        if (x > -1250f){
            x -= 0.3f * 5;
            bg.setTranslation(x,0);
           // foods.body().applyForce(new Vec2(10,0),foods.body().getPosition());
        }
        bird.update(delta);
        food1.update(delta);
        food2.update(delta);
        food3.update(delta);

        world.step(0.033f, 10, 10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        for (int c = 0 ; c <= i ; c++){
            birdMap.get(c).paint(clock);
        }
        food1.paint(clock);
        food2.paint(clock);
        food3.paint(clock);
        bird.paint(clock);

        if (showDebugDraw) {
            debugDraw.getCanvas().clear();
            debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
            debugDraw.getCanvas().drawText(debugString,100f,50f);
            debugDraw.getCanvas().drawText(debugStringCoin,100f,100f);
            world.drawDebugData();
        }
    }


    /*public void createFood(int foodTotal) {
        this.f1 = foodTotal;
    }*/
}
