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


public class Food {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private World world;
    private Body body;
    public enum State {
        IDLE
    }

    ;

    private State state = State.IDLE;

    private int e = 0;
    private int offset = 0;


    public Food(final World world, final float x_px, final float y_px) {
            sprite = SpriteLoader.getSprite("images/food.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x_px, y_px);
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
        bodyDef.position = new Vec2(0, 0);//แปลง pixel ให้เป็น m คือ เอา pixel ไปคูณ กับค่าคงที่
        bodyDef.gravityScale = 0f;
        body = world.createBody(bodyDef);
        //bodyDef.active = new Boolean(true);

        GameScreen.bodies.put(body, "food_" + GameScreen.f);
        GameScreen.f++;

        //PolygonShape shape = new PolygonShape();
        /*CircleShape shape = new CircleShape();
        shape.setRadius(0.7f);*/
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.layer().width() * GameScreen.M_PER_PIXEL / 2, sprite.layer().height()* GameScreen.M_PER_PIXEL / 2);

        FixtureDef fixtureDef = new FixtureDef();//น้ำหนัก
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;

        body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0f);

        return body;
    }


    public void update(int delta) {
        if (hasLoaded == false) return;
        e = e +delta;
        if (e > 150) {
            switch(state){
                case IDLE: offset =0; break;


            }
            spriteIndex = offset + ((spriteIndex +1 ) %1);
            sprite.setSprite(spriteIndex);
            e = 0;
            body.applyForce(new Vec2(-4,0),body.getPosition());
        }

    }

    public void paint(Clock clock) {
        if(hasLoaded == false)return;


        sprite.layer().setTranslation(
                (body.getPosition().x /GameScreen.M_PER_PIXEL) +0,
                body.getPosition().y / GameScreen.M_PER_PIXEL);

    }

    public Body getBody() {
        return body;
    }

    /*public Body body(){
        return this.body;
    }
    public void move(){

    }*/
}