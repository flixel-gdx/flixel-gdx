package com.badlogic.gdx.backends.android;

import android.content.Intent;

import com.badlogic.gdx.Net;

public class AndroidNet implements Net {

	final AndroidApplication app; 
	
	public AndroidNet(AndroidApplication activity) {
		this.app = activity;
	}
	
	@Override
	public void openURI(String URI) {
	
		app.startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(URI)));
	}

}
