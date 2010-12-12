package iching.android.activities;

import iching.android.R;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.LayoutGameActivity;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;

public class Randomizer extends LayoutGameActivity implements IAccelerometerListener, IOnSceneTouchListener
{
	private Camera mCamera;
	private PhysicsWorld mPhysicsWorld;
	private Texture mTexture;
	private TextureRegion mFaceTextureRegion;
	private TiledTextureRegion gua;

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	private final Vector2 mTempVector = new Vector2();
	
	@Override
	public Engine onLoadEngine()
	{
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(false, ScreenOrientation.PORTRAIT,
				new FillResolutionPolicy(), this.mCamera));
	}

	@Override
	public void onLoadResources()
	{
		this.mTexture = new Texture(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gua = TextureRegionFactory.createTiledFromResource(this.mTexture, this, R.drawable.qian2, 0, 0, 2, 1); // 64x32

		mFaceTextureRegion = TextureRegionFactory.createFromResource(mTexture,
				this, R.drawable.qian3, 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
		
		this.enableAccelerometerSensor(this);
	}

	@Override
	public Scene onLoadScene()
	{
		final Scene sceneA = new Scene(1);
		sceneA.setBackground(new ColorBackground(0.2f, 0.2f, 0.2f, 1f));
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		/*
		 * Calculate the coordinates for the face, so its centered on the
		 * camera.
		 */
		final int centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2 - 100;
		final int centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion
				.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		final Sprite face = new Sprite(centerX, centerY,
				this.mFaceTextureRegion);
		sceneA.getTopLayer().addEntity(face);
		return sceneA;
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
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{
		return false;
	}

	@Override
	public void onAccelerometerChanged(AccelerometerData pAccelerometerData)
	{
		this.mTempVector.set(pAccelerometerData.getY(), pAccelerometerData.getX());
		this.mPhysicsWorld.setGravity(this.mTempVector);
	}

}
