package iching.android.activities;

import iching.android.R;
import iching.android.utils.IChingHelper;
import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
	private Integer tossTimes = 0;
	private int[] originalHexagram = new int[6];

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
	public void onClick(View view)
	{
		final Button button = (Button) findViewById(R.id.test);
		button.setClickable(Boolean.FALSE);
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				tossTimes++;
				tossTimes = tossTimes % 7;
				long now = System.currentTimeMillis();
				long timeToTossCoin = 1000;
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
								try
								{
									Thread.sleep(2000);
								}
								catch(InterruptedException e)
								{
									e.printStackTrace();
								}
								ImageView firstCoin = (ImageView)findViewById(R.id.first_coin);
								ImageView secondCoin = (ImageView)findViewById(R.id.second_coin);
								ImageView thirdCoin = (ImageView)findViewById(R.id.third_coin);
								final int[] coins = {firstCoin.getHeight(), secondCoin.getHeight(), thirdCoin.getHeight()};
								if(tossTimes != 6)
								{
									button.setClickable(Boolean.TRUE);
								}
								Thread setYaoThread = new Thread(new Runnable(){
									@Override
									public void run()
									{
										setYao(coins);
									}
								});
								handler.post(setYaoThread);
								Log.e("firstCoine", firstCoin.getHeight() + "");
								Log.e("secondCoine", secondCoin.getHeight() + "");
								Log.e("thirdCoine", thirdCoin.getHeight() + "");
							}
						}
					}

					private void displayDerivedHexagram(int[] hexagram)
					{
						for(int i : hexagram)
						{
							
						}
					}
					
					private void setYao(int[] coins) {
						String idString = "yao";
						if(tossTimes != 1)
						{
							idString += tossTimes;
						}								
						int enableImageId = IChingHelper.getId(idString, R.id.class);								
						ImageView yao = (ImageView) findViewById(enableImageId);
						int yaoSource = getYaoSource(coins, originalHexagram, tossTimes - 1);
						yao.setImageResource(yaoSource);
						yao.setVisibility(View.VISIBLE);
					}
					
					private int getYaoSource(int[] coins, int[] originalHexagram, int index)
					{
						String result = null;
						int numOfYins = 0;
						int numOfYangs = 0;
						for(int i : coins)
						{
							if(i == 35)
							{
								numOfYangs++;
							}
							else
							{
								numOfYins++;
							}
						}
						if(numOfYangs == 3)
						{
							result = "yang_changing";
							originalHexagram[index] = 0;
						}
						else if(numOfYins == 3)
						{
							result = "yin_changing";
							originalHexagram[index] = 1;
						}
						else if(numOfYangs == 2)
						{
							result = "yin";
							originalHexagram[index] = 0;
						}
						else
						{
							result = "yang";
							originalHexagram[index] = 1;
						}
						return IChingHelper.getId(result, R.drawable.class);
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
			if(source == R.drawable.manwen)
			{
				Matrix markerMatrix = new Matrix();
				coin.setImageMatrix(markerMatrix);
			}
			coin.setImageResource(source);
			synchronized(threadFinishedCount)
			{
				threadFinishedCount++;
			}
		}
		
	}
}
