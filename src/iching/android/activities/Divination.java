package iching.android.activities;

import iching.android.R;
import iching.android.persistence.IChingSQLiteDBHelper;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Divination extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.divination);
		Bundle extras = getIntent().getExtras();
		TextView yourQuestion = (TextView)findViewById(R.id.your_question);
		yourQuestion.setText(extras.getString(IChingSQLiteDBHelper.QUESTION));
		ArrayList<ImageView> originalImageViews = new ArrayList<ImageView>();
		originalImageViews.add((ImageView)findViewById(R.id.yao));
		originalImageViews.add((ImageView)findViewById(R.id.yao2));
		originalImageViews.add((ImageView)findViewById(R.id.yao3));
		originalImageViews.add((ImageView)findViewById(R.id.yao4));
		originalImageViews.add((ImageView)findViewById(R.id.yao5));
		originalImageViews.add((ImageView)findViewById(R.id.yao6));
		
		String lines = extras.getString(IChingSQLiteDBHelper.ORIGINAL_LINES);
		String changingLines = extras.getString(IChingSQLiteDBHelper.CHANGING_LINES);
		
		ArrayList<ImageView> relatingImageViews = new ArrayList<ImageView>();
		relatingImageViews.add((ImageView)findViewById(R.id.yao_));
		relatingImageViews.add((ImageView)findViewById(R.id.yao_1));
		relatingImageViews.add((ImageView)findViewById(R.id.yao_2));
		relatingImageViews.add((ImageView)findViewById(R.id.yao_3));
		relatingImageViews.add((ImageView)findViewById(R.id.yao_4));
		relatingImageViews.add((ImageView)findViewById(R.id.yao_5));
		displayHexagramByLines(lines, changingLines, Boolean.TRUE, originalImageViews);
		if(changingLines.trim().length() != 0)
		{
			displayHexagramByLines(lines, changingLines, Boolean.FALSE, relatingImageViews);
		}
	}
	
	private void displayHexagramByLines(String lines, String changingLines, Boolean isOriginal, ArrayList<ImageView> lineImages)
	{
		char[] digits = lines.toCharArray();
		int index = 0;
		for(char digit : digits)
		{
			boolean changingLine = changingLines.indexOf(String.valueOf(index+1)) != -1;
			ImageView imageView = lineImages.get(index);
			if(digit == '1' && changingLine && isOriginal)
			{
				imageView.setImageResource(R.drawable.yang_changing);
			}
			else if(digit == '1' && !changingLine && isOriginal)
			{
				imageView.setImageResource(R.drawable.yang);
			}
			else if(digit == '1' && changingLine && !isOriginal)
			{
				imageView.setImageResource(R.drawable.yin_relating);
			}
			else if(digit == '1' && !changingLine && !isOriginal)
			{
				imageView.setImageResource(R.drawable.yang);
			}
			else if(digit == '0' && changingLine && isOriginal)
			{
				imageView.setImageResource(R.drawable.yin_changing);
			}
			else if(digit == '0' && !changingLine && isOriginal)
			{
				imageView.setImageResource(R.drawable.yin);
			}
			else if(digit == '0' && changingLine && !isOriginal)
			{
				imageView.setImageResource(R.drawable.yang_relating);
			}
			else if(digit == '0' && !changingLine && !isOriginal)
			{
				imageView.setImageResource(R.drawable.yin);
			}
			imageView.setVisibility(View.VISIBLE);
			index++;
		}
	}
}

