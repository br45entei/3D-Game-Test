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

import com.badlogic.gdx.controllers.Controller;
import com.gmail.br45entei.game.Game;
import com.gmail.br45entei.game.graphics.GLThread.InitializationProgress;
import com.gmail.br45entei.game.graphics.MatrixStack;
import com.gmail.br45entei.game.input.ControllerManager;
import com.gmail.br45entei.game.input.Mouse;
import com.gmail.br45entei.game.ui.MenuProvider;
import com.gmail.br45entei.game.ui.Window;
import com.gmail.br45entei.util.StringUtil;

import java.util.Objects;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.libsdl.SDL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.swt.GLData;

/** @author Brian_Entei &ltbr45entei&#064;gmail.com&gt; */
public class CameraTest implements Game, MenuProvider {
	
	private volatile boolean initialized = false;
	
	protected final MatrixStack stack = new MatrixStack();
	protected final MatrixStack projStack = new MatrixStack(2);
	
	/** Default constructor. */
	public CameraTest() {
		this.stack.setModelView(0, 0, 0, 0, 0, 0);
	}
	
	/** @param args Program command line arguments */
	public static void main(String[] args) {
		Game game = new CameraTest();
		
		GLData data = new GLData();
		data.doubleBuffer = true;
		data.swapInterval = Integer.valueOf(1);
		data.majorVersion = 2;
		data.minorVersion = 1;
		data.forwardCompatible = false;
		
		Window window = new Window("Camera Test", 800, 600, 60.0D, data); // new Window(title, width, height, framerate);
		//window.setAlwaysOnTop(true, Boolean.TRUE);
		boolean deadZoneSuccess = false;
		if(window.pollControllers()) {
			ControllerManager manager = window.getControllerManager();
			if(manager != null) {
				Controller controller = manager.getController1();
				if(controller != null) {
					for(int axis = SDL.SDL_CONTROLLER_AXIS_LEFTX; axis < SDL.SDL_CONTROLLER_AXIS_MAX; axis++) {
						manager.setAxisDeadzone(axis, ControllerManager.COMMON_AXIS_DEADZONE, controller);
					}
					manager.setAxisDeadzone(SDL.SDL_CONTROLLER_AXIS_RIGHTY, ControllerManager.DEFAULT_AXIS_DEADZONE, controller);
					deadZoneSuccess = true;
				}
			}
		}
		if(deadZoneSuccess) {
			System.out.println("DeadZone success!");
		} else {
			System.err.println("DeadZone failure!");
		}
		
		window.setActiveRenderer(game);
		window.registerInputCallback(new InputLogger(System.out).setPrintKeyboardButtons(false).setPrintKeyboardButtonHelds(false).setPrintMouseButtons(false).setPrintMouseButtonHelds(false).setPrintControllerAxisChanges(false));
		window.open();
	}
	
	@Override
	public String getName() {
		return "CameraTest";
	}
	
	@Override
	public boolean isInitialized() {
		return this.initialized;
	}
	
	@Override
	public void initialize(InitializationProgress progress) {
		this.initialized = true;
	}
	
	@Override
	public void onSelected() {
		
	}
	
	@Override
	public void onViewportChanged(Rectangle oldViewport, Rectangle newViewport) {
		int width = Window.getWindow().getWidth();
		int height = Window.getWindow().getHeight();
		GL11.glViewport(0, 0, width, height);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadMatrixd(this.projStack.setPerspectiveProjection(45.0, width, height, 0.01, 1000.0).peek());
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadMatrixd(this.stack.peek());
		
	}
	
	@Override
	public void render(double deltaTime, int width, int height) {
		GL11.glClearColor(0.5f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
	}
	
	@Override
	public void onDeselected() {
		
	}
	
	@Override
	public void onCleanup() {
		
	}
	
	@Override
	public boolean isInputInitialized() {
		return false;
	}
	
	@Override
	public void inputInit() {
		
	}
	
	@Override
	public void inputCleanup() {
		
	}
	
	@Override
	public void input(double deltaTime) {
		if(Mouse.isCaptured() && Mouse.shouldIListenToClickEvents()) {
			
		}
	}
	
	@Override
	public void update(double deltaTime) {
		
	}
	
	@Override
	public void onMouseMoved(int deltaX, int deltaY, int oldX, int oldY, int newX, int newY) {
		
	}
	
	@Override
	public void onMouseButtonDown(int button) {
		
	}
	
	@Override
	public void onMouseButtonHeld(int button) {
		
	}
	
	@Override
	public void onMouseButtonUp(int button) {
		
	}
	
	@Override
	public void onMouseDoubleClick(int button) {
		
	}
	
	@Override
	public void onMouseScroll(boolean vertical, int count) {
		
	}
	
	@Override
	public void onKeyDown(int key) {
		
	}
	
	@Override
	public void onKeyHeld(int key) {
		
	}
	
	@Override
	public void onKeyUp(int key) {
		
	}
	
	@Override
	public void onControllerConnected(Controller controller) {
		
	}
	
	@Override
	public void onControllerDisconnected(Controller controller) {
		
	}
	
	@Override
	public void onControllerButtonDown(Controller controller, int button) {
		
	}
	
	@Override
	public void onControllerButtonRepeat(Controller controller, int button) {
		
	}
	
	@Override
	public void onControllerButtonUp(Controller controller, int button) {
		
	}
	
	@Override
	public void onControllerButtonDoubleTapped(Controller controller, int button) {
		
	}
	
	@Override
	public void onControllerAxisChanged(Controller controller, int axis, float oldValue, float newValue) {
		
	}
	
	@Override
	public String getMenuName() {
		return "CameraTest Options";
	}
	
	@Override
	public void onMenuBarCreation(Menu menu) {
		
	}
	
	@Override
	public void onMenuBarDeletion(Menu menu) {
		
	}
	
	@Override
	public boolean providesPopupMenu() {
		return false;
	}
	
	@Override
	public void onPopupMenuCreation(Menu menu) {
		
	}
	
	@Override
	public void onPopupMenuDeletion(Menu menu) {
		
	}
	
	@Override
	public void updateMenuItems() {
		
	}
	
	//=============================================================================================
	
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
		System.err.println(String.format("The renderer \"%s\" threw an exception while executing method %s(%s):", this.getName(), method, parameters));
		System.err.println(StringUtil.throwableToStr(ex));
		System.err.flush();
		return false;
	}
	
}
