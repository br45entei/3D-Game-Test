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

import com.gmail.br45entei.game.Game.GameAdapter;
import com.gmail.br45entei.game.graphics.GLThread.InitializationProgress;
import com.gmail.br45entei.game.ui.Window;
import com.gmail.br45entei.util.StringUtil;

import java.util.Objects;

import org.lwjgl.opengl.swt.GLData;

/** @author Brian_Entei &ltbr45entei&#064;gmail.com&gt; */
public class InterfaceTest implements GameAdapter {
	
	/** @param args Program command line arguments */
	public static final void main(String[] args) {
		GLData data = Window.createDefaultGLData();
		data.majorVersion = 2;
		data.minorVersion = 1;
		data.forwardCompatible = false;
		Window window = new Window(null, 800, 600, Window.getDefaultRefreshRate(), data, new InterfaceTest()).setIconImages(new String[] {null});
		while(true) {
			window.open();// Mwuahahahaha!
		}
	}
	
	private volatile boolean initialized = false, inputInitialized = false;
	
	/** Creates a new InterfaceTest object. */
	public InterfaceTest() {
	}
	
	@Override
	public String getName() {
		return "Interface Test";
	}
	
	@Override
	public boolean isInitialized() {
		return this.initialized;
	}
	
	@Override
	public void initialize(InitializationProgress progress) {
		// ...
		
		progress.setProgress(1.0f);
		this.initialized = true;
	}
	
	@Override
	public boolean isInputInitialized() {
		return this.inputInitialized;
	}
	
	@Override
	public void inputInit() {
		// ...
		
		this.inputInitialized = true;
	}
	
	@Override
	public void inputCleanup() {
		// ...
		
		this.inputInitialized = false;
	}
	
	@Override
	public void onKeyDown(int key) {
		
	}
	
	@Override
	public void onCleanup() {
		
		this.initialized = false;
	}
	
	@Override
	public boolean handleException(Throwable ex, String method, Object... params) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < params.length; i++) {
			Object param = params[i];
			String toString;
			if(param == null || param.getClass().isPrimitive()) {
				toString = Objects.toString(param);
			} else {
				toString = param.toString();
				String className = param.getClass().getName();
				if(toString.startsWith(className.concat("@"))) {
					toString = className;
				}
			}
			
			sb.append(toString).append(i + 1 == params.length ? "" : ", ");
		}
		String parameters = sb.toString();
		System.err.println(String.format("The game \"%s\" threw an exception while executing method %s(%s):", this.getName(), method, parameters));
		System.err.println(StringUtil.throwableToStr(ex));
		System.err.flush();
		return false;
	}
	
}
