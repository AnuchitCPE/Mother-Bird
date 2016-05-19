package characters;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sprite.Sprite;
import sprite.SpriteLoader;
import sut.game01.core.GameScreen;

public class Bird {
    public Sprite sprite;
    public int spriteIndex = 0;
    private boolean hasLoaded = false;
    private Body body;

    public enum State {
        IDLE, FLY, DIE
    };

    public State state = State.IDLE;

    private int e = 0;
    public int offset = 0;

    public Bird(final World world, final float x_px, final float y_px) {
        sprite = SpriteLoader.getSprite("images/bird.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
                sprite.layer().setTranslation(x_px, y_px + 13f);

                body = initPhysicsBody(world,
                        GameScreen.M_PER_PIXEL * x_px,
                        GameScreen.M_PER_PIXEL * y_px);
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

    private Body initPhysicsBody(World world, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        GameScreen.bodies.put(body, "test_" + GameScreen.k);
        GameScreen.k++;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(90 * GameScreen.M_PER_PIXEL / 2, sprite.layer().height()* GameScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y), 0f);
        return body;

    }

    public  void update(int delta) {
        if (hasLoaded == false) return;
        PlayN.keyboard().setListener(new Keyboard.Adapter(){
            @Override
            public void onKeyUp(Keyboard.Event event) {
                if(event.key() == Key.SPACE) {
                    switch (state) {
                        case IDLE: state = State.FLY; break;
                        case FLY: state = State.DIE; break;
                        case DIE: state = State.IDLE; break;
                    }
                }else if (event.key() == Key.LEFT) {
                    state = State.IDLE;
                    body.applyForce(new Vec2(-80f,0f), body.getPosition());
                }else if (event.key() == Key.RIGHT) {
                    state = State.IDLE;
                    body.applyForce(new Vec2(80f,0f), body.getPosition());
                }else if (event.key() == Key.UP) {
                    state = State.FLY;
                    body.applyForce(new Vec2(0f, -700f), body.getPosition());
                }
            }
        });
        e = e + delta;
        if (e > 150) {
            switch (state) {
                case IDLE: offset = 0; break;
                case FLY: offset = 5; break;
                case DIE: offset = 10; break;
            }
            if (spriteIndex != 14) {
                spriteIndex = offset + ((spriteIndex + 1) % 5);
                sprite.setSprite(spriteIndex);
            }else{
                sprite.setSprite(spriteIndex);
            }
            e = 0;
        }

    }

    public void paint(Clock clock) {
        if (!hasLoaded) return;
        //sprite.layer().setRotation(body.getAngle());
        sprite.layer().setTranslation(
                (body.getPosition().x / GameScreen.M_PER_PIXEL) - 10,
                body.getPosition().y / GameScreen.M_PER_PIXEL);
    }

    public Body getBody() {
        return body;
    }

    public void die(){
        state = state.DIE;
        e = e + 5;
        if (e > 150) {
            switch (state) {
                case IDLE: offset = 0; break;
                case FLY: offset = 5; break;
                case DIE: offset = 10; break;
            }
            if (spriteIndex != 14) {
                spriteIndex = offset + ((spriteIndex + 1) % 5);
                sprite.setSprite(spriteIndex);
            }else{
                sprite.setSprite(spriteIndex);
            }
            e = 0;
        }
    }
}
