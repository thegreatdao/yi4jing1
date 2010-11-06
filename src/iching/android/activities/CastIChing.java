package iching.android.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class CastIChing extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new Panel(this));
	}

	class Panel extends View
	{
		public Panel(Context context) {
			super(context);
		}

		@Override
		public void onDraw(Canvas canvas)
		{
			ShapeDrawable mDrawable;

			int x = 10;
			int y = 10;
			int width = 100;
			int height = 10;

			mDrawable = new ShapeDrawable(new RectShape());
			mDrawable.getPaint().setColor(Color.BLACK);
			mDrawable.setBounds(x, y, x + width, y + height);
			mDrawable.draw(canvas);
			y = 25;
			mDrawable.setBounds(x, y, x + width, y + height);
			mDrawable.draw(canvas);
			y = 40;
			mDrawable.setBounds(x, y, x + width, y + height);
			mDrawable.draw(canvas);
			y = 55;
			mDrawable.setBounds(x, y, x + width, y + height);
			mDrawable.draw(canvas);
			y = 70;
			mDrawable.setBounds(x, y, x + width, y + height);
			mDrawable.draw(canvas);
			y = 85;
			mDrawable.setBounds(x, y, x + width, y + height);
			mDrawable.draw(canvas);
			TextView textView = new TextView(getBaseContext());
			textView.setText("This is it!");
			textView.setHeight(200);
			textView.setWidth(200);
		}
	}
}
