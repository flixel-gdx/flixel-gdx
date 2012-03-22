package org.flixel;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExampleStarter extends ListActivity
{
	String examples[] = { "Animation", "Collision", "Particles", "ThousandParticles", "Blur", 
						  "Bitmapfont", "MultiTouch", "DragAndDrop", "CameraEffects", 
						  "Audio", "Tile", "TiledMap", "TiledMap2", "PathFinding", "FlxCollisions"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examples));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		String exampleName = examples[position];
		try
		{
			Class<?> c = Class.forName("org.flixel." + exampleName + "Demo");
			Intent intent = new Intent(this, c);
			startActivity(intent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
