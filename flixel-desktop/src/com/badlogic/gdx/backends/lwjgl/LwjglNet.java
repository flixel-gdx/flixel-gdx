package com.badlogic.gdx.backends.lwjgl;

import java.awt.Desktop;
import java.net.URI;

import com.badlogic.gdx.Net;

public class LwjglNet implements Net {
	
	@Override
	public void openURI(String uri) {
		
		if (!Desktop.isDesktopSupported()) 
			return;
		
		Desktop desktop = Desktop.getDesktop();
		
		if (!desktop.isSupported(Desktop.Action.BROWSE))
			return;
		
		try {
			desktop.browse(new URI(uri));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}