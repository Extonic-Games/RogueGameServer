package me.extain.server.map.tiled;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import me.extain.server.Physics.Box2DHelper;


public class TileMapHelper {

    private static float tileSize = 16;

    private static World world;

    public Array<Body> buildShapes(Map map, float tileSize) {
        TileMapHelper.tileSize = tileSize;

        world = Box2DHelper.getWorld();

        MapObjects objects = map.getLayers().get("Collision").getObjects();

        Array<Body> bodies = new Array<Body>();

        for(MapObject object : objects) {
            if (object instanceof TextureMapObject) {
                continue;
            }

            Shape shape;

            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject) object);
            } else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject) object);
            } else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject) object);
            } else if (object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject) object);
            } else if (object instanceof EllipseMapObject) {
                shape = getEllipse((EllipseMapObject) object);
            } else {
                continue;
            }

            BodyDef bodyDef = new BodyDef();
            //bodyDef.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bodyDef);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.restitution = 0f;
            fixtureDef.friction = 1f;
            fixtureDef.density = 1f;

            fixtureDef.filter.categoryBits = Box2DHelper.BIT_OBJECTS;
            fixtureDef.filter.maskBits = Box2DHelper.MASK_OBJECTS;

            body.setUserData(this);

            Fixture fixture = body.createFixture(fixtureDef);

            bodies.add(body);
            shape.dispose();

        }
        return bodies;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle =  rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2(rectangle.x + (rectangle.width * 0.5f), rectangle.y + (rectangle.getHeight() * 0.5f));
        polygon.setAsBox(rectangle.width * 0.5f, rectangle.height * 0.5f, size, 0);
        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape shape = new CircleShape();
        shape.setRadius(circle.radius);
        shape.setPosition(new Vector2(circle.x, circle.y));
        return shape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            worldVertices[i] = vertices[i];
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2];
            worldVertices[i].y = vertices[i * 2 + 1];
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }

    private static final int ELLIPSE_APPROX_POINTS = 32;
    private static Vector2[] ellipseVertices = new Vector2[ELLIPSE_APPROX_POINTS];

    private static ChainShape getEllipse(EllipseMapObject ellipseMapObject) {
        for (int idx = 0; idx < ELLIPSE_APPROX_POINTS; idx++) {
            float percentDone = (float) idx / (float) ELLIPSE_APPROX_POINTS;
            float currentEllipseAngle = percentDone * 2 * MathUtils.PI;
            Vector2 newPoint = calculatePointOnEllipseForAngle(currentEllipseAngle, ellipseMapObject.getEllipse());
            ellipseVertices[idx] = newPoint;
        }

        ChainShape chainShape = new ChainShape();
        chainShape.createChain(ellipseVertices);
        return chainShape;
    }

    private static Vector2 calculatePointOnEllipseForAngle(float currentEllipseAngle, Ellipse ellipse) {
        Vector2 newPoint = new Vector2();
        float radiusX = ellipse.width / 2f;
        float radiusY = ellipse.height / 2f;

        newPoint.x = (radiusX * (float) Math.cos(currentEllipseAngle) + (ellipse.x + radiusX));
        newPoint.y = (radiusY * (float) Math.sin(currentEllipseAngle) + (ellipse.y + radiusY));

        return newPoint;
    }

}
