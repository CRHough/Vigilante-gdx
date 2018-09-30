package com.aesophor.medievania.desktop;

import com.aesophor.medievania.Medievania;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Medievania";
		config.width = 1280;
		config.height = 700;
		//config.fullscreen = true;
		new LwjglApplication(new Medievania(), config);
	}
	
}