package com.aesophor.vigilante.desktop;

import com.aesophor.vigilante.Vigilante;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Vigilante";
		config.width = 1280;
		config.height = 700;
		//config.fullscreen = true;
		new LwjglApplication(new Vigilante(), config);
	}
	
}