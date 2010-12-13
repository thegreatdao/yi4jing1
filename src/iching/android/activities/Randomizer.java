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

public class Randomizer extends LayoutGameActivity implements IAccelerometerListener, IOnSceneTouchListener
{
	private Camera mCamera;
	private PhysicsWorld mPhysicsWorld;
	private Texture mTexture;
	private TextureRegion mFaceTextureRegion;

	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

	private final Vector2 mTempVector = new Vector2();
	
	@Override
	public Engine onLoadEngine()
	{
		Display display = getWindowManager().getDefaultDisplay();
		CAMERA_WIDTH = display.getWidth();
		CAMERA_HEIGHT = display.getHeight();
		this.mCamera = new Camera(0, 0, display.getWidth(), display.getHeight());
		return new Engine(new EngineOptions(false, ScreenOrientation.PORTRAIT, new FillResolutionPolicy(), this.mCamera));
	}

	@Override
	public void onLoadResources()
	{
		this.mTexture = new Texture(128, 128,TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		mFaceTextureRegion = TextureRegionFactory.createFromResource(mTexture, this, R.drawable.kun, 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
		
		this.enableAccelerometerSensor(this);
	}

	@Override
	public Scene onLoadScene()
	{
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(2);
		scene.setBackground(new ColorBackground(1, 1, 1, 1));
		scene.setOnSceneTouchListener(this);

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		scene.getBottomLayer().addEntity(ground);
		scene.getBottomLayer().addEntity(left);
		scene.getBottomLayer().addEntity(roof);
		scene.getBottomLayer().addEntity(right);

		scene.registerUpdateHandler(this.mPhysicsWorld);

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
		if(this.mPhysicsWorld != null) {
			if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
				this.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
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
		this.mTempVector.set(pAccelerometerData.getY(), pAccelerometerData.getX());
		this.mPhysicsWorld.setGravity(this.mTempVector);
	}
	
	private void addFace(final float pX, final float pY) {
		final Scene scene = this.mEngine.getScene();

		final Sprite face;
		final Body body;

		face = new Sprite(pX, pY, this.mFaceTextureRegion);
		body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, face, BodyType.DynamicBody, FIXTURE_DEF);
		face.setUpdatePhysics(true);
		scene.getTopLayer().addEntity(face);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body, true, true, false, false));
	}
}
