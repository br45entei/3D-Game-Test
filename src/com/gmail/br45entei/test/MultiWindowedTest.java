package com.gmail.br45entei.test;

import com.gmail.br45entei.game.graphics.Renderer;
import com.gmail.br45entei.game.input.InputCallback.InputLogger;
import com.gmail.br45entei.game.ui.Window;

/** @author Brian_Entei */
public class MultiWindowedTest {
	
	public static final void main(String[] args) {
		Thread.currentThread().setName("FirstWindowThread");
		
		Window window = new Window("SWT-LWJGL3 Framework #1", 800, 600);
		window.registerInputCallback(new InputLogger(System.out));
		window.setActiveRenderer(new Renderer.ColorDemo() {
			@Override
			public void initialize() {
				super.initialize();
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						Window window = new Window("SWT-LWJGL3 Framework #2", 800, 600);
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
