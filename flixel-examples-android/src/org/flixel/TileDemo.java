package org.flixel;

import com.badlogic.gdx.backends.android.AndroidApplication;

import android.os.Bundle;
import android.view.WindowManager;

public class TileDemo extends AndroidApplication
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initialize(new org.examples.tile.TileDemo(), false);
	}
}
