/*******************************************************************************
 * 
 * Copyright © 2022 Brian_Entei (br45entei@gmail.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 *******************************************************************************/
package com.gmail.br45entei.test;

import com.gmail.br45entei.game.graphics.GLThread.InitializationProgress;
import com.gmail.br45entei.game.graphics.Renderer;
import com.gmail.br45entei.game.input.InputCallback.InputLogger;
import com.gmail.br45entei.game.ui.Window;

import org.lwjgl.opengl.swt.GLData;

/** @author Brian_Entei &ltbr45entei&#064;gmail.com&gt; */
public class MultiWindowedTest {
	
	/** @param args Program command line arguments */
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
			public void initialize(InitializationProgress progress) {
				super.initialize(progress);
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
