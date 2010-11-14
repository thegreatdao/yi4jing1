package iching.android.activities;

import iching.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class CastIChing extends Activity implements OnClickListener
{
	private Handler handler;
	private int threadCount;
	private Integer threadFinishedCount = 0;
	private boolean isHeadFirstCoin;
	private boolean isHeadSecondCoin;
	private boolean isHeadThirdCoin;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cast_iching);
		Button button = (Button) findViewById(R.id.test);
		button.setOnClickListener(this);
		handler = new Handler();
	}

	@Override
	public void onClick(View v)
	{
		final Button button = (Button) findViewById(R.id.test);
		button.setClickable(false);
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				long now = System.currentTimeMillis();
				long timeToTossCoin = 2000;
				while (System.currentTimeMillis() - now < timeToTossCoin)
				{
					tossCoins();
					threadCount++;
				}
				Thread ichingChecker = new Thread(new Runnable()
				{
					
					@Override
					public void run()
					{
						boolean run = true;
						while(run)
						{
							if(threadCount * 3 == threadFinishedCount)
							{
								run = false;
								threadCount = 0;
								threadFinishedCount = 0;
								button.setClickable(true);
							}
						}
					}
				});
				ichingChecker.start();
			}
		});
		thread.start();
	}
	
	private void tossCoins()
	{
		final ImageView firstCoin = (ImageView)findViewById(R.id.first_coin);
		final ImageView secondCoin = (ImageView)findViewById(R.id.second_coin);
		final ImageView thirdCoin = (ImageView)findViewById(R.id.third_coin);
		
		handler.post(new TossCoinThread(firstCoin, headOrTail()));
		handler.post(new TossCoinThread(secondCoin, headOrTail()));
		handler.post(new TossCoinThread(thirdCoin, headOrTail()));
		
	}

	private int headOrTail()
	{
		double mid = 0.5;
		double random = Math.random();
		if(random > mid)
		{
			return R.drawable.hanwen;
		}
		else
		{
			return R.drawable.manwen;
		}
	}
	
	private class TossCoinThread extends Thread
	{
		private ImageView coin;
		private int source;
		
		TossCoinThread(ImageView coin, int source)
		{
			this.coin = coin;
			this.source = source;
		}
		
		@Override
		public void run()
		{
			coin.setImageResource(source);
			synchronized(threadFinishedCount)
			{
				threadFinishedCount++;
			}
		}
		
	}
}
