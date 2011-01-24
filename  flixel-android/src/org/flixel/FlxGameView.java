package org.flixel;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FlxGameView extends SurfaceView implements SurfaceHolder.Callback
{
	private FlxThread _thread;
	private FlxGame _game;
	private Activity _activity;
	private SurfaceHolder _surfaceHolder;

	public FlxGameView(FlxGame game, Context context)
	{
		super(context);
		getHolder().addCallback(this);
		_activity = (Activity) context;
		_game = game;
		
		FlxG._game.stage = this;
		// Enable view key events
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}

	

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{	
		if(_thread == null)
		{
			_surfaceHolder = holder;
			_surfaceHolder.setFixedSize(FlxG.width, FlxG.height);
			_thread = new FlxThread(getHolder(), this, _game);
			_thread.setRunning(true);
			_thread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		if(_thread != null)
		{
			boolean retry = true;
			_thread.setRunning(false);
			while(retry)
			{
				try
				{
					_thread.join();
					retry = false;
				}
				catch(InterruptedException e)
				{
					
				}
			}
		}		
		_thread = null;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		
	}

	 
	@Override
	public synchronized boolean onTouchEvent(MotionEvent event)
	{
		if(MotionEvent.ACTION_DOWN == event.getAction())
			FlxG.touch.handleTouchDown(event);
		else if(MotionEvent.ACTION_MOVE == event.getAction())
			FlxG.touch.handleTouchDown(event);
		else if(MotionEvent.ACTION_UP == event.getAction())
			FlxG.touch.handleTouchRemove(event);
		return true;
	}
	
	
	@Override
	public synchronized boolean onKeyDown(int keyCode, KeyEvent event)
	{		
		synchronized(_surfaceHolder)
		{
			FlxG.keys.handleKeyDown(keyCode);			
		}
		return false;
	}
	
	@Override
	public synchronized boolean onKeyUp(int keyCode, KeyEvent event)
	{
		synchronized(_surfaceHolder)
		{
			_game.onKeyUp(event.getKeyCode());
		}		
		return false;
	}
	


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		final int specWidth = FlxG.specWidth = MeasureSpec.getSize(widthMeasureSpec);
		final int specHeight = FlxG.specHeight = MeasureSpec.getSize(heightMeasureSpec);
	
		final float desiredRatio = (float) FlxG.width / (float) FlxG.height;
		final float realRatio = (float) specWidth / specHeight;		
		int measuredWidth;
		int measuredHeight;
		
		if(realRatio < desiredRatio)
		{			
			FlxG.ratio = (float) specWidth / FlxG.width;
			measuredWidth = specWidth;
			measuredHeight = Math.round(measuredWidth / desiredRatio);
		}
		else
		{			
			FlxG.ratio = (float) specHeight / FlxG.height;
			measuredHeight = specHeight;
			measuredWidth = Math.round(measuredHeight * desiredRatio);
		}
		this.setMeasuredDimension(measuredWidth, measuredHeight);
	}



	public void shutdown()
	{
		_thread = null;
		if (_activity != null)
    		_activity.finish();
	}
	
	
	
}
