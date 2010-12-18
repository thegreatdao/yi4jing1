package iching.android.activities;

import iching.android.R;
import iching.android.utils.IChingHelper;

import java.util.HashMap;
import java.util.Map;

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
import android.os.Bundle;
import android.view.Display;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Randomizer extends LayoutGameActivity implements IAccelerometerListener, IOnSceneTouchListener
{
	private Camera camera;
	private PhysicsWorld physicsWorld;
	private Texture texture;
	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

	private final Vector2 vector2 = new Vector2();
	private int count;
	private Map<String, String[]> icons;

	@Override
	public Engine onLoadEngine()
	{
		icons = getIcons();
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
		mEngine.getTextureManager().loadTexture(texture);
		enableAccelerometerSensor(this);
	}

	@Override
	public Scene onLoadScene()
	{
		mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(2);
		scene.setBackground(new ColorBackground(1, 1, 1));
		scene.setOnSceneTouchListener(this);

		physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.LIGHT_SUNRISE), true);
		physicsWorld.setGravity(vector2);
		final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
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
		Toast.makeText(this, "click on the screen", Toast.LENGTH_LONG).show();
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

	private void addFace(float pX, float pY)
	{
		String[] iconsForOneGong = icons.get("2");
		if(count < 8)
		{
			Scene scene = mEngine.getScene();
			texture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

			TextureRegion guaTextureRegion = TextureRegionFactory.createFromResource(texture, this, IChingHelper.getId(iconsForOneGong[count], R.drawable.class), 0, 0);
			Sprite gua = new Sprite(pX, pY, guaTextureRegion);
			Body body = PhysicsFactory.createBoxBody(physicsWorld, gua, BodyType.DynamicBody, FIXTURE_DEF);
			scene.getTopLayer().addEntity(gua);
			mEngine.getTextureManager().loadTexture(texture);
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(gua, body, true, true, false, false));
		}
		count++;
	}
	
	private Map<String, String[]> getIcons()
	{
		Bundle extras = getIntent().getExtras();
		Map<String, String[]> icons = new HashMap<String, String[]>();
		for(int i=1; i<=8; i++)
		{
			icons.put(Integer.toString(i), (String[])extras.get(Integer.toString(i)));
		}
		return icons;
	}
}
