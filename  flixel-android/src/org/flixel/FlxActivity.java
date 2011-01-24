package org.flixel;

import android.app.Activity;
import android.util.Log;

public class FlxActivity extends Activity
{	
	
	@Override
	protected void onStart()
	{		
		Log.i("onStart", "start");
		super.onStart();
	}
	
	@Override
	protected void onResume()
	{
		Log.i("onResume", "resume");
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{		
		Log.i("onPause", "pause");
		super.onPause();	
		
	}
	
	@Override
	protected void onStop()
	{	
		Log.i("onStop", "stop");
		super.onStop();
		if(!FlxG.getPause())
			FlxG.setPause(true);
		
	}
	
	@Override
	protected void onRestart()
	{		
		Log.i("onRestart", "restart");
		super.onRestart();
		FlxG.resume();
	}
	
	@Override
	protected void onDestroy()
	{
		Log.i("onDestroy", "destroy");
		FlxG.shutdown(true);
		super.onDestroy();
	}
}
