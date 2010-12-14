package iching.android.activities;

import iching.android.R;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.LayoutGameActivity;

import android.hardware.SensorManager;
import android.view.Display;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Randomizer extends LayoutGameActivity implements
		IAccelerometerListener, IOnSceneTouchListener
{
	private Camera camera;
	private PhysicsWorld physicsWorld;
	private Texture texture;
	private TextureRegion guaTextureRegion;

	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

	private final Vector2 vector2 = new Vector2();
//	private RepeatingSpriteBackground background;
	private int count;

	@Override
	public Engine onLoadEngine()
	{
		Display display = getWindowManager().getDefaultDisplay();
		CAMERA_WIDTH = display.getWidth();
		CAMERA_HEIGHT = display.getHeight();
		camera = new Camera(0, 0, display.getWidth(), display.getHeight());
		return new Engine(new EngineOptions(false, ScreenOrientation.PORTRAIT, new FillResolutionPolicy(), camera));
	}

	@Override
	public void onLoadResources()
	{
		texture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		guaTextureRegion = TextureRegionFactory.createFromResource(texture, this, R.drawable.bo, 0, 0);

		mEngine.getTextureManager().loadTexture(texture);
//		background = new RepeatingSpriteBackground(CAMERA_WIDTH, CAMERA_HEIGHT, mEngine.getTextureManager(), new AssetTextureSource(this, "gfx/background.png"));
		enableAccelerometerSensor(this);
	}

	@Override
	public Scene onLoadScene()
	{
		mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(2);
		scene.setBackground(new ColorBackground(1, 1, 1));
//		scene.setBackground(background);
		scene.setOnSceneTouchListener(this);

		physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_MERCURY), false);
		physicsWorld.setGravity(vector2);
		final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0,
				0.5f, 0.5f);
		PhysicsFactory.createBoxBody(physicsWorld, ground,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, left,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, roof,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, right,
				BodyType.StaticBody, wallFixtureDef);

		scene.getBottomLayer().addEntity(ground);
		scene.getBottomLayer().addEntity(left);
		scene.getBottomLayer().addEntity(roof);
		scene.getBottomLayer().addEntity(right);

		scene.registerUpdateHandler(physicsWorld);

		return scene;
	}

	@Override
	public void onLoadComplete()
	{

	}

	@Override
	protected int getLayoutID()
	{
		return R.layout.randomizer;
	}

	@Override
	protected int getRenderSurfaceViewID()
	{
		return R.id.xmllayoutexample_rendersurfaceview;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent)
	{
		if (physicsWorld != null)
		{
			if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
			{
				runOnUpdateThread(new Runnable()
				{
					@Override
					public void run()
					{
						Randomizer.this.addFace(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					}
				});
				return true;
			}
		}
		return false;
	}

	@Override
	public void onAccelerometerChanged(AccelerometerData pAccelerometerData)
	{
		vector2.set(pAccelerometerData.getX(),pAccelerometerData.getY());
		physicsWorld.setGravity(vector2);
	}

	private void addFace(final float pX, final float pY)
	{
		count++;
		if(count <= 8)
		{
			final Scene scene = mEngine.getScene();
	
			final Sprite gua;
			final Body body;
	
			gua = new Sprite(pX, pY, guaTextureRegion);
			body = PhysicsFactory.createBoxBody(physicsWorld, gua, BodyType.DynamicBody, FIXTURE_DEF);
			gua.setUpdatePhysics(false);
			scene.getTopLayer().addEntity(gua);
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(gua, body, true, true, false, false));
		}
	}
}
