package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.GUA_BODY;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_ICON;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_TITLE;
import iching.android.R;
import iching.android.bean.Line;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.utils.IChingHelper;

import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CastIChing extends Activity implements OnClickListener
{
	private Handler handler;
	private int threadCount;
	private int threadFinishedCount;
	private int tossTimes;
	private Line[] originalHexagramLines = new Line[6];
	private Map<String, String> originalHexagram;
	private Map<String, String> relatingHexagram;
	private IChingSQLiteDBHelper iChingSQLiteDBHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cast_iching);
		Button button = (Button) findViewById(R.id.tossCoin);
		TextView guaTitle = (TextView) findViewById(R.id.gua_title);
		TextView guaTitle2 = (TextView) findViewById(R.id.gua_title2);
		button.setOnClickListener(this);
		guaTitle.setOnClickListener(this);
		guaTitle2.setOnClickListener(this);
		handler = new Handler();
		iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this, Boolean.TRUE);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.gua_title:
				showHexagram(Boolean.TRUE);
				break;
			case R.id.gua_title2:
				showHexagram(Boolean.FALSE);
				break;
			case R.id.tossCoin:
				tossCoin();
				break;
			default:
				break;
		}
	}

	private void showHexagram(boolean original)
	{
		Map<String, String> hexagram = relatingHexagram;
		if(original)
		{
			hexagram = originalHexagram;
		}
		Intent intent = new Intent(getApplicationContext(), Gua.class);
		intent.putExtra(GUA_BODY, hexagram.get(GUA_BODY));
		intent.putExtra(GUA_TITLE, hexagram.get(GUA_TITLE));
		intent.putExtra(GUA_ICON, hexagram.get(GUA_ICON));
		startActivity(intent);
	}
	
	private void tossCoin()
	{
		final Button button = (Button) findViewById(R.id.tossCoin);
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
								Thread setYaoThread = new Thread(new Runnable(){
									@Override
									public void run()
									{
										setYao(coins);
									}
								});
								handler.post(setYaoThread);
								if(tossTimes != 6)
								{
									button.setClickable(Boolean.TRUE);
								}
								else
								{
									Thread showRelatingHexgram = new Thread(new Runnable()
									{
										
										@Override
										public void run()
										{
											Locale locale = Locale.getDefault();
											String originalHexgramCode = getOriginalCodes(originalHexagramLines);
											originalHexagram = iChingSQLiteDBHelper.selectOneGuaByField("code", "'" + originalHexgramCode + "'", locale);
											TextView originalTitle = (TextView) findViewById(R.id.gua_title);
											originalTitle.setText(originalHexagram.get(GUA_TITLE));
											String relatingHexgramCode = getRelatingCodes(originalHexagramLines);
											if(relatingHexagramExists(originalHexagramLines))
											{
												displayRelatingHexagram(originalHexagramLines);
												relatingHexagram = iChingSQLiteDBHelper.selectOneGuaByField("code", "'" + relatingHexgramCode + "'", locale);
												TextView relatingTitle = (TextView) findViewById(R.id.gua_title2);
												relatingTitle.setText(relatingHexagram.get(GUA_TITLE));
											}
										}
									});
									handler.post(showRelatingHexgram);
//									String question = ((EditText)findViewById(R.id.question)).getText().toString();
									try
									{
										Thread.sleep(2000);
									}
									catch (InterruptedException e)
									{
										e.printStackTrace();
									}
								}
							}
						}
					}

					private boolean relatingHexagramExists(Line[] hexagram)
					{
						boolean result = false;
						for(Line line : hexagram)
						{
							if(line.isChanging())
							{
								result = true;
							}
						}
						return result;
					}
					
					private void displayRelatingHexagram(Line[] hexagrams)
					{
						int index = 0;
						for(Line line : hexagrams)
						{
							String sourcePrefix = "yao_";
							if(index != 0)
							{
								sourcePrefix += index;
							}
							ImageView relatingYao = (ImageView)findViewById(IChingHelper.getId(sourcePrefix, R.id.class));
							relatingYao.setVisibility(View.VISIBLE);
							if(line.isChanging())
							{
								if(line.isYang())
								{
									relatingYao.setImageResource(IChingHelper.getId("yin_relating", R.drawable.class));
								}
								else
								{
									relatingYao.setImageResource(IChingHelper.getId("yang_relating", R.drawable.class));
								}
							}
							else
							{
								if(!line.isYang())
								{
									relatingYao.setImageResource(IChingHelper.getId("yin", R.drawable.class));
								}
								else
								{
									relatingYao.setImageResource(IChingHelper.getId("yang", R.drawable.class));
								}
							}
							index++;
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
						int yaoSource = getYaoSource(coins, originalHexagramLines, tossTimes - 1);
						yao.setImageResource(yaoSource);
						yao.setVisibility(View.VISIBLE);
					}
					
					private int getYaoSource(int[] coins, Line[] originalHexagram, int index)
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
						Line line = new Line();
						if(numOfYangs == 3)
						{
							result = "yang_changing";
							line.setChanging(Boolean.TRUE);
							line.setYang(Boolean.TRUE);
						}
						else if(numOfYins == 3)
						{
							result = "yin_changing";
							line.setChanging(Boolean.TRUE);
							line.setYang(Boolean.FALSE);
						}
						else if(numOfYangs == 2)
						{
							result = "yin";
							line.setChanging(Boolean.FALSE);
							line.setYang(Boolean.FALSE);
						}
						else
						{
							result = "yang";
							line.setChanging(Boolean.FALSE);
							line.setYang(Boolean.TRUE);
						}
						originalHexagram[index] = line;
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
			threadFinishedCount++;
		}
		
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		iChingSQLiteDBHelper.close();
	}
	
	private String getOriginalCodes(Line[] lines)
	{
		StringBuilder code = new StringBuilder();
		for(Line line : lines)
		{
			if(line.isYang())
			{
				code.append('1');
			}
			else
			{
				code.append('0');
			}
		}
		return code.toString();
	}
	
	private String getRelatingCodes(Line[] lines)
	{
		StringBuilder code = new StringBuilder();
		for(Line line : lines)
		{
			char digit = '0';
			if(line.isYang() && !line.isChanging())
			{
				digit = '1';
			}
			if(!line.isYang() && line.isChanging())
			{
				digit = '1';
			}
			code.append(digit);
		}
		return code.toString();
	}
	
	@SuppressWarnings("unused")
	private String getChangingLinePositions(Line[] lines)
	{
		StringBuilder positions = new StringBuilder();
		int index = 1;
		for(Line line : lines)
		{
			if(line.isChanging())
			{
				positions.append(index);
			}
			index++;
		}
		return positions.toString();
	}
}
