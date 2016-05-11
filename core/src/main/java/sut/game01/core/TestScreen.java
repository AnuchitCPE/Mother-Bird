package sut.game01.core;

import static playn.core.PlayN.*;

import characters.Zealot;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.*;
import playn.core.DebugDrawBox2D;
import org.jbox2d.common.Vec2;
import playn.core.*;
import playn.core.util.Clock;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;
import java.util.List;

public class TestScreen extends Screen {

    public static float M_PER_PIXEL = 1 / 26.666667f;  //size of world
    public static int width = 24; // 640 px in physic unit (meter)
    public static int height = 12ccd; // 480 px in physic unit (meter)

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true;
    private final ScreenStack ss;
    private final ImageLayer bg;
    private final ImageLayer backButton;
    private Zealot zealot;
    private List<Zealot> zealotMap;
    private int i = 0;

    public TestScreen(final ScreenStack ss) {
        this.ss = ss;
        zealotMap = new ArrayList<Zealot>();
        Image bgImage = assets().getImage("images/gameBg.png");
        this.bg = graphics().createImageLayer(bgImage);

        Image backImage = assets().getImage("images/back.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(530,10);

        Vec2 gravity = new Vec2(0.0f , 10.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        zealot = new Zealot(world, 560f, 280f);

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
        //this.layer.add(zealot.layer());

        /*mouse().setListener(new Mouse.Adapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyType.DYNAMIC;
                bodyDef.position = new Vec2(
                        event.x() * M_PER_PIXEL,
                        event.y() * M_PER_PIXEL);
                Body body = world.createBody(bodyDef);

                PolygonShape shape = new PolygonShape();
                shape.setAsBox(1, 1);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 0.4f;
                fixtureDef.friction = 0.1f;
                fixtureDef.restitution = 0.35f;

                body.createFixture(fixtureDef);
                body.setLinearDamping(0.2f);
            }
        });*/

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


        mouse().setListener(new Mouse.Adapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                Zealot zealot = new Zealot(world, (float)event.x(), (float)event.y());
                zealotMap.add(zealot);
            }
        });
        this.layer.add(zealot.layer());

        for(Zealot z: zealotMap){
            System.out.println("add");
            this.layer.add(z.layer());
        }


    }

    @Override
    public void update(int delta) {
        super.update(delta);
        zealot.update(delta);
        for(Zealot z: zealotMap){
            this.layer.add(z.layer());
            z.update(delta);
        }
        world.step(0.033f, 10, 10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        for(Zealot z: zealotMap){
            z.paint(clock);
        }
        if (showDebugDraw) {
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }
}
