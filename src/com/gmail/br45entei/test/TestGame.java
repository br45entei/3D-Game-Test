package com.gmail.br45entei.test;

import com.gmail.br45entei.game.Game;
import com.gmail.br45entei.game.graphics.GLUtil;
import com.gmail.br45entei.game.graphics.Renderer;
import com.gmail.br45entei.game.input.Mouse;
import com.gmail.br45entei.game.ui.MenuProvider;
import com.gmail.br45entei.game.ui.Window;

import java.util.Objects;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.swt.GLData;

/** @author Brian_Entei */
public class TestGame implements Game, MenuProvider {
	
	private volatile boolean initialized = false;
	private volatile int mx, my;
	private volatile int mWidth = 16, mHeight = 16;
	
	/** Default constructor */
	private TestGame() {
	}
	
	/** Opens a new {@link Window} and tests this {@link TestGame}.
	 * 
	 * @param args Program command line arguments */
	public static void main(String[] args) {
		Game game = new TestGame();
		
		GLData data = new GLData();
		data.doubleBuffer = true;
		data.swapInterval = Integer.valueOf(1);
		data.majorVersion = 2;
		data.minorVersion = 1;
		data.forwardCompatible = false;
		
		Window window = new Window("Window Title", 800, 600, 60.0D, data); // new Window(title, width, height, framerate);
		window.setActiveRenderer(game);
		window.registerRenderer(Renderer.colorDemo);
		window.registerGame(new InterfaceTest());
		window.open();
	}
	
	@Override
	public String getName() {
		return "Test Game";
	}
	
	@Override
	public boolean isInitialized() {
		return this.initialized;
	}
	
	@Override
	public void initialize() {
		this.mx = Window.getWindow().getWidth() / 2;
		this.my = Window.getWindow().getHeight() / 2;
		this.initialized = true;
	}
	
	@Override
	public void onSelected() {
		
	}
	
	private volatile int lastWidth, lastHeight;
	
	@Override
	public void onViewportChanged(Rectangle oldViewport, Rectangle newViewport) {
		GL11.glViewport(newViewport.x, newViewport.y, newViewport.width, newViewport.height);// Set the GL viewport
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadMatrixf(GLUtil.getOrthographicMatrixf(newViewport.x, newViewport.y, newViewport.width, newViewport.height, 0.01f, 1000.0f));
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	@Override
	public void render(double deltaTime) {
		int width = Window.getWindow().getWidth();
		int height = Window.getWindow().getHeight();
		if(width != this.lastWidth || height != this.lastHeight) {
			GL11.glViewport(0, 0, width, height);// Set the GL viewport
		}
		GL11.glClearColor(0, 0, 0, 1);// Set the clear color
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);// Clear the viewport using the clear color
		
		if(width != this.lastWidth || height != this.lastHeight) {
			this.lastWidth = width;
			this.lastHeight = height;
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadMatrixf(GLUtil.getOrthographicMatrixf(0, 0, width, height, 0.01f, 1000.0f));
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
		}
		
		GL11.glColor3f(0.85f, 0.12f, 0.27f);
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f(width * 0.25f, height * 0.25f);
		GL11.glVertex2f(width * 0.75f, height * 0.25f);
		GL11.glVertex2f(width * 0.25f, height * 0.75f);
		GL11.glVertex2f(width * 0.75f, height * 0.75f);
		GL11.glEnd();
		
		int mx = this.mx - (this.mWidth / 2);
		int my = this.my - (this.mHeight / 2);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f(mx, my);
		GL11.glVertex2f(mx + this.mWidth, my);
		GL11.glVertex2f(mx, my + this.mHeight);
		GL11.glVertex2f(mx + this.mWidth, my + this.mHeight);
		GL11.glEnd();
		
	}
	
	@Override
	public void onDeselected() {
		
	}
	
	//=============================================================================================
	
	@Override
	public boolean isModal() {
		return false;
	}
	
	@Override
	public void input(double deltaTime) {
		
	}
	
	@Override
	public void update(double deltaTime) {
		
	}
	
	@Override
	public void onMouseMoved(int deltaX, int deltaY, int oldX, int oldY, int newX, int newY) {
		if(Mouse.isCaptured()) {
			this.mx = Math.min(Window.getWindow().getWidth(), Math.max(0, this.mx + deltaX));
			this.my = Math.min(Window.getWindow().getHeight(), Math.max(0, this.my - deltaY));
		} else {
			this.mx = newX;
			this.my = Window.getWindow().getHeight() - newY;
		}
	}
	
	@Override
	public void onMouseScroll(boolean vertical, int count) {
		
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
	public void onKeyDown(int key) {
		
	}
	
	@Override
	public void onKeyHeld(int key) {
		
	}
	
	@Override
	public void onKeyUp(int key) {
		
	}
	
	//=============================================================================================
	
	@Override
	public String getMenuName() {
		return "Test Game Options";
	}
	
	@Override
	public void onMenuBarCreation(Menu menu) {
		
	}
	
	@Override
	public void onMenuBarDeletion(Menu menu) {
		
	}
	
	@Override
	public void onPopupMenuCreation(Menu menu) {
		
	}
	
	@Override
	public void onPopupMenuDeletion(Menu menu) {
		
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
				if(toString.startsWith(param.getClass().getName().concat("@"))) {
					toString = param.getClass().getName();
				}
			}
			
			sb.append(toString).append(i + 1 == params.length ? "" : ", ");
		}
		String parameters = sb.toString();
		System.err.println(String.format("The renderer \"%s\" threw an exception while executing method %s(%s)!", this.getName(), method, parameters));
		System.err.flush();
		return false;
	}
	
}
