package sut.game01.core;

import static playn.core.PlayN.*;

import characters.Zealot;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
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

public class TestScreen extends Screen {

    public static float M_PER_PIXEL = 1 / 26.666667f;  //size of world
    public static int width = 24; // 640 px in physic unit (meter)
    public static int height = 18; // 480 px in physic unit (meter)

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true;
    private final ScreenStack ss;
    private final ImageLayer bg;
    private final ImageLayer backButton;
    private final ImageLayer coin;
    private Zealot zealot;
    private List<Zealot> zealotMap;
    private int i = -1;
    public static HashMap<Body,String> bodies = new HashMap<Body, String>();
    public static int k = 0;
    public static int j = 0;
    public static String debugString = "";
    public static String debugStringCoin = "";

    public TestScreen(final ScreenStack ss) {
        this.ss = ss;
        graphics().rootLayer().clear();
        zealotMap = new ArrayList<Zealot>();
        Image bgImage = assets().getImage("images/gameBg.png");
        this.bg = graphics().createImageLayer(bgImage);

        Image coinImage = assets().getImage("images/Coin.png");
        this.coin = graphics().createImageLayer(coinImage);
        coin.setTranslation(295,215);


        Image backImage = assets().getImage("images/back.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(530,10);
        backButton.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
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

        mouse().setListener(new Mouse.Adapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                zealotMap.add(new Zealot(world, (float)event.x(), (float)event.y()));
                i++;
                for (int c = 0 ; c <= i ; c++){
                    graphics().rootLayer().add(zealotMap.get(c).layer());
                }

            }
        });

    }

    @Override
    public  void  wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(backButton);
        this.layer.add(coin);

        if (showDebugDraw) {
            CanvasImage image = graphics().createImage(
                    (int) (width / TestScreen.M_PER_PIXEL),
                    (int) (height / TestScreen.M_PER_PIXEL));
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
            debugDraw.setCamera(0, 0, 1f / TestScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }

        Body ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(0, height), new Vec2(width, height));
        ground.createFixture(groundShape, 0.0f);

        Body coinCircle = world.createBody(new BodyDef());
        CircleShape coinCircleShape = new CircleShape();
        coinCircleShape.setRadius(1.0f);
        coinCircleShape.m_p.set(12f,9f);
        coinCircle.createFixture(coinCircleShape, 0.0f);

    }

    @Override
    public void update(int delta) {
        super.update(delta);
        for (int c = 0 ; c <= i ; c++){
            zealotMap.get(c).update(delta);
        }

        world.step(0.033f, 10, 10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        for (int c = 0 ; c <= i ; c++){
            zealotMap.get(c).paint(clock);
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
