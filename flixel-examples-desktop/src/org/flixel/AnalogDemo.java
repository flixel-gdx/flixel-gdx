package org.flixel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class AnalogDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.analog.AnalogDemo(), "", 480, 320, false);
		/*SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				// -------------------------------------------------------------
				// Display mode selection
				// -------------------------------------------------------------

				String[] modes =
				{ "portrait", "landscape" };
				String modeResult = (String) JOptionPane.showInputDialog(null, "Select the display mode",
						"Initialization", JOptionPane.PLAIN_MESSAGE, null, modes, "portrait");

				// -------------------------------------------------------------
				// Resolution selection
				// -------------------------------------------------------------

				String[] resolutions =
				{ "HVGA (320x480)", "WVGA800 (480x800)", "WVGA854 (480x854)", "Galaxy Tab (600x1024)",
						"Motorola Xoom (800x1280)" };
				String resolutionResult = (String) JOptionPane.showInputDialog(null, "Select your display",
						"Initialization", JOptionPane.PLAIN_MESSAGE, null, resolutions, "HVGA (320x480)");

				// -------------------------------------------------------------
				// App launch
				// -------------------------------------------------------------

				boolean isPortrait = modeResult.equals("portrait");

				if(resolutionResult != null && resolutionResult.length() > 0)
				{
					Matcher m = Pattern.compile("(\\d+)x(\\d+)").matcher(resolutionResult);
					m.find();
					int w = Integer.parseInt(m.group(isPortrait ? 1 : 2));
					int h = Integer.parseInt(m.group(isPortrait ? 2 : 1));
					launch(w, h);
				}
			}
		});*/
	}
	
	
	public static void launch(int width, int height)
	{
		new LwjglApplication(new org.flixel.examples.analog.AnalogDemo(), "", width, height, false);
	}
}
