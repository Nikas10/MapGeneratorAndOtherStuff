package com.nikas.trial.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nikas.trial.GameLauncher;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "MapGeneration for PC";
		config.height = 900;
		config.width = 1600;
		new LwjglApplication(new GameLauncher(), config);
	}
}
