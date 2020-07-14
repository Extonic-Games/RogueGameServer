package me.extain.server.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.backends.headless.HeadlessNet;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

import me.extain.server.RogueGameServer;

import static org.mockito.Mockito.mock;

public class DesktopLauncher {
	public static void main (String[] arg) {
		HeadlessNativesLoader.load();
		MockGraphics mockGraphics = new MockGraphics();
		Gdx.graphics = mockGraphics;
		HeadlessFiles headlessFiles = new HeadlessFiles();
		Gdx.files = headlessFiles;
		Gdx.gl = mock(GL20.class);
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new RogueGameServer(), config);
	}
}
