package iching.android.activities;

import iching.android.R;
import iching.android.utils.IChingHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnectorManager;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.LayoutGameActivity;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Randomizer extends LayoutGameActivity implements IAccelerometerListener, IOnSceneTouchListener, IOnMenuItemClickListener, IOnAreaTouchListener
{
	private Camera camera;
	private PhysicsWorld physicsWorld;
	private Texture texture;
	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
	
	protected static final int MENU_RESET = 0;
	protected static final int MENU_QUIT = MENU_RESET + 1;

	private final Vector2 vector2 = new Vector2();
	private int count;
	private Map<String, String[]> icons;
	private MenuScene mMenuScene;
	private Scene scene;
	private Font font;
	private String[] guas;
	private List<Sprite> guasOnScreen = new ArrayList<Sprite>();
	
	private static final int QIAN_GONG = 1;
	private static final int ZHEN_GONG = 2;
	private static final int KAN_GONG = 3;
	private static final int GEN_GONG = 4;
	private static final int KUN_GONG = 5;
	private static final int XUN_GONG = 6;
	private static final int LI_GONG = 7;
	private static final int DUI_GONG = 8;
	
	@Override
	public Engine onLoadEngine()
	{
		icons = getIcons();
		guas = icons.get(Integer.toString(QIAN_GONG));
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
		
		Texture mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		FontFactory.setAssetBasePath("font/");
		font = FontFactory.createFromAsset(mFontTexture, this, "Plok.ttf", 36, true, Color.WHITE);
		this.mEngine.getTextureManager().loadTexture(mFontTexture);
		this.mEngine.getFontManager().loadFont(font);
		enableAccelerometerSensor(this);
	}

	@Override
	public Scene onLoadScene()
	{
		mEngine.registerUpdateHandler(new FPSLogger());
		scene = new Scene(2);
		mMenuScene = createMenuScene();
		scene.setBackground(new ColorBackground(1, 1, 1));
		scene.setOnSceneTouchListener(this);
		physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_SATURN), true);
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
				addIcon(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
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

	private void addIcon(float pX, float pY)
	{
		if(count < 8)
		{
			texture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

			TextureRegion guaTextureRegion = TextureRegionFactory.createFromResource(texture, this, IChingHelper.getId(guas[count], R.drawable.class), 0, 0);
			Sprite gua = new Sprite(pX, pY, guaTextureRegion);
			guasOnScreen.add(gua);
			gua.setUpdatePhysics(false);
			scene.registerTouchArea(gua);
			Body body = PhysicsFactory.createBoxBody(physicsWorld, gua, BodyType.DynamicBody, FIXTURE_DEF);
			scene.getTopLayer().addEntity(gua);
			mEngine.getTextureManager().loadTexture(texture);
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(gua, body, true, true, true, true));
		}
		count++;
	}
	
	private void removeIcons(List<Sprite> icons)
	{
		PhysicsConnectorManager physicsConnectorManager = physicsWorld.getPhysicsConnectorManager();
		for(Sprite icon : icons)
		{
			PhysicsConnector guaPhysicsConnector = physicsConnectorManager.findPhysicsConnectorByShape(icon);
			
			physicsWorld.unregisterPhysicsConnector(guaPhysicsConnector);
			physicsWorld.destroyBody(guaPhysicsConnector.getBody());
			scene.unregisterTouchArea(icon);
			scene.getTopLayer().removeEntity(icon);
		}
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
	
	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY)
	{
		switch(pMenuItem.getID())
		{
			case QIAN_GONG:
				setGuas(QIAN_GONG, icons, scene);
				return true;
			case ZHEN_GONG:
				setGuas(ZHEN_GONG, icons, scene);
				return true;
			case KAN_GONG:
				setGuas(KAN_GONG, icons, scene);
				return true;
			case GEN_GONG:
				setGuas(GEN_GONG, icons, scene);
				return true;
			case KUN_GONG:
				setGuas(KUN_GONG, icons, scene);
				return true;
			case XUN_GONG:
				setGuas(XUN_GONG, icons, scene);
				return true;
			case LI_GONG:
				setGuas(LI_GONG, icons, scene);
				return true;
			case DUI_GONG:
				setGuas(DUI_GONG, icons, scene);
				return true;
			default:
				return false;
		}
	}

	public void setGuas(int gongId, Map<String, String[]> icons, Scene scene)
	{
		removeIcons(guasOnScreen);
		guas = icons.get(Integer.toString(gongId));
		count = 0;
		guasOnScreen = new ArrayList<Sprite>();
		scene.back();
	}
	
	protected MenuScene createMenuScene()
	{
		final MenuScene menuScene = new MenuScene(camera);

		final IMenuItem qianMenuItem = new ColorMenuItemDecorator(new TextMenuItem(QIAN_GONG, font, getString(R.string.qian_gong)), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		qianMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(qianMenuItem);
		
		final IMenuItem zhenMenuItem = new ColorMenuItemDecorator(new TextMenuItem(ZHEN_GONG, font, getString(R.string.zhen_gong)), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		zhenMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(zhenMenuItem);

		final IMenuItem kanMenuItem = new ColorMenuItemDecorator(new TextMenuItem(KAN_GONG, font, getString(R.string.kan_gong)), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		kanMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(kanMenuItem);

		final IMenuItem genMenuItem = new ColorMenuItemDecorator(new TextMenuItem(GEN_GONG, font, getString(R.string.gen_gong)), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		genMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(genMenuItem);

		final IMenuItem kunMenuItem = new ColorMenuItemDecorator(new TextMenuItem(KUN_GONG, font, getString(R.string.kun_gong)), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		kunMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(kunMenuItem);

		final IMenuItem xunMenuItem = new ColorMenuItemDecorator(new TextMenuItem(XUN_GONG, font, getString(R.string.xun_gong)), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		xunMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(xunMenuItem);

		final IMenuItem liMenuItem = new ColorMenuItemDecorator(new TextMenuItem(LI_GONG, font, getString(R.string.li_gong)), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		liMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(liMenuItem);

		final IMenuItem duiMenuItem = new ColorMenuItemDecorator(new TextMenuItem(DUI_GONG, font, getString(R.string.dui_gong)), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		duiMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(duiMenuItem);

		menuScene.buildAnimations();
		
		menuScene.setBackgroundEnabled(false);

		menuScene.setOnMenuItemClickListener(this);
		return menuScene;
	}
	
	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent)
	{
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN)
		{
			if(scene.hasChildScene())
			{
				mMenuScene.back();
			}
			else
			{
				scene.setChildScene(mMenuScene, false, true, true);
			}
			return true;
		}
		else
		{
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY)
	{
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
		{
			Toast.makeText(this, "s", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}
}
