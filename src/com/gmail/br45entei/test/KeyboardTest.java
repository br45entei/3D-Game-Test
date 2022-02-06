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

import com.gmail.br45entei.game.input.Keyboard.Keys;
import com.gmail.br45entei.util.CodeUtil;
import com.gmail.br45entei.util.Platform;
import com.sun.jna.platform.KeyboardUtils;
import com.sun.jna.platform.win32.User32;

/** @author Brian_Entei &ltbr45entei&#064;gmail.com&gt; */
public class KeyboardTest {
	
	private static final boolean[] downStates = new boolean[256];
	private static final boolean[] lastDownStates = new boolean[downStates.length];
	
	private static final boolean checkKey(int key) {
		switch(Platform.get()) {
		case WINDOWS:
			return User32.INSTANCE.GetAsyncKeyState(key) < 0;
		case LINUX:
			return KeyboardUtils.isPressed(key);
		case MACOSX:
			//
		default:
		case UNKNOWN:
			return false;
		}
	}
	
	/** Polls the system keyboard. */
	public static final void poll() {
		System.arraycopy(downStates, 0, lastDownStates, 0, lastDownStates.length);
		for(int i = 0; i < downStates.length; i++) {
			downStates[i] = checkKey(i);
			if(downStates[i]) {
				System.out.println("Pressed: ".concat(Keys.getNameForKey(i)));
			}
		}
	}
	
	/** @param key The keyboard key's scan-code
	 * @return Whether or not the specified key is currently being pressed/held
	 *         down */
	public static final boolean isKeyDown(int key) {
		return key >= 0 && key < downStates.length && downStates[key];
	}
	
	/** @param key The keyboard key's scan-code
	 * @return Whether or not the specified key was just pressed */
	public static final boolean getKeyDown(int key) {
		return key >= 0 && key < downStates.length && downStates[key] && !lastDownStates[key];
	}
	
	/** @param args Program command line arguments */
	public static final void main(String[] args) {
		while(true) {
			poll();
			if(isKeyDown(97)) {
				System.out.println("a?"); // This one does not print
			}
			if(isKeyDown(65)) {
				System.out.println("A?"); // This one prints
			}
			CodeUtil.sleep(10L);
		}
	}
	
}
