package com.gmail.br45entei.test;

import com.gmail.br45entei.game.graphics.Renderer;
import com.gmail.br45entei.game.input.InputCallback.InputLogger;
import com.gmail.br45entei.game.ui.Window;

import org.lwjgl.opengl.swt.GLData;

/** @author Brian_Entei */
public class MultiWindowedTest {
	
	public static final void main(String[] args) {
		Thread.currentThread().setName("FirstWindowThread");
		
		final GLData data = Window.createDefaultGLData();
		data.forwardCompatible = false;
		data.majorVersion = 0;
		data.minorVersion = 0;
		
		Window window = new Window("SWT-LWJGL3 Framework #1", 800, 600, data);
		window.registerInputCallback(new InputLogger(System.out));
		window.setActiveRenderer(new Renderer.ColorDemo() {
			@Override
			public void initialize() {
				super.initialize();
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						Window window = new Window("SWT-LWJGL3 Framework #2", 800, 600, data);
						window.registerInputCallback(new InputLogger(System.err));
						window.setActiveRenderer(new Renderer.ColorDemo());
						window.open();
					}
				}, "SecondWindowThread");
				thread.setDaemon(false);
				thread.start();
			}
		});
		
		window.open();
	}
	
}
