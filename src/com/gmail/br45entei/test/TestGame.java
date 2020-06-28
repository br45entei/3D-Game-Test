package com.gmail.br45entei.test;

import com.badlogic.gdx.controllers.Controller;
import com.gmail.br45entei.game.Game;
import com.gmail.br45entei.game.graphics.GLUtil;
import com.gmail.br45entei.game.graphics.MatrixStack;
import com.gmail.br45entei.game.graphics.Renderer;
import com.gmail.br45entei.game.input.Keyboard;
import com.gmail.br45entei.game.input.Keyboard.Keys;
import com.gmail.br45entei.game.input.Mouse;
import com.gmail.br45entei.game.ui.MenuProvider;
import com.gmail.br45entei.game.ui.Window;
import com.gmail.br45entei.util.BufferUtil;
import com.gmail.br45entei.util.CodeUtil;
import com.gmail.br45entei.util.SWTUtil;

import java.awt.Point;
import java.nio.FloatBuffer;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.swt.GLData;

/** @author Brian_Entei */
public class TestGame implements Game, MenuProvider {
	
	/** Creates the vertices of a cube with the specified dimensions.<br>
	 * Use the following code to load the cube into a VBO and then draw it.<br>
	 * 
	 * <pre>
	 * float[] vertices = createCubeVertices(width, height, length);
	 * 
	 * //...
	 * 
	 * //Creating a VBO to load the vertices into OpenGL:
	 * int[] vbo = new int[1];
	 * //create a new direct native-ordered FloatBuffer with a capacity of the length of the cube's vertices array:
	 * FloatBuffer cubeVertexBuf = ByteBuffer.allocateDirect((vertices.length * Float.SIZE) / Byte.SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer().rewind();
	 * for(int i = 0; i < vertices.length; i++) {
	 * 	cubeVertexBuf.put(i, vertices[i]);
	 * }
	 * cubeVertexBuf.rewind();
	 * GL15.glGenBuffers(vbo);
	 * GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo[0]);
	 * GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cubeVertexBuf, GL15.GL_STATIC_DRAW);
	 * 
	 * //Drawing the cube:
	 * GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo[0]);
	 * GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
	 * GL20.glEnableVertexAttribArray(0);
	 * GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length / 3);
	 * 
	 * //Deleting the VBO when no longer needed (e.g. after all rendering is finished):
	 * GL15.glDeleteBuffers(vbo[0]);
	 * </pre>
	 * 
	 * @param width The width of the cube (X axis)
	 * @param height The height of the cube (Y axis)
	 * @param length The length of the cube (Z axis)
	 * @return The cube's vertices */
	public static final float[] createCubeVertices(float width, float height, float length) {//@formatter:off
		return new float[] {-width, height, -length, -width, -height, -length, width, -height, -length, width, -height, -length, width, height, -length, -width, height, -length,
				width, -height, -length, width, -height, length, width, height, -length, width, -height, length, width, height, length, width, height, -length,
				width, -height, length, -width, -height, length, width, height, length, -width, -height, length, -width, height, length, width, height, length,
				-width, -height, length, -width, -height, -length, -width, height, length, -width, -height, -length, -width, height, -length, -width, height, length,
				-width, -height, length, width, -height, length, width, -height, -length, width, -height, -length, -width, -height, -length, -width, -height, length,
				-width, height, -length, width, height, -length, width, height, length, width, height, length, -width, height, length, -width, height, -length
		};//@formatter:on
	}
	
	private volatile boolean initialized = false;
	private volatile int mx, my;
	private volatile int mWidth = 16, mHeight = 16;
	
	protected volatile boolean _3D = false;
	private volatile boolean last3D = this._3D;
	private volatile float x, y, z;
	private volatile float lastX, lastY, lastZ;
	private volatile float yaw, pitch, roll;
	private volatile float lastYaw, lastPitch, lastRoll;
	private final MatrixStack stack = new MatrixStack();
	private volatile float[] modelView = this.stack.modelView(this.x, this.y, this.z, this.yaw, this.pitch, this.roll).peekf();
	
	private volatile boolean freeLook = false;
	private volatile float mouseSensitivity = 0.15f;
	
	private volatile int[] vbo = new int[1];
	private volatile float[] cubeVertices = null;
	private volatile FloatBuffer cubeVertexBuf = null;
	
	//============================================================
	
	protected volatile MenuItem mntmToggle2D3D = null;
	
	//============================================================
	
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
		window.registerInputCallback(new InputLogger(System.out) {
			@Override
			public void onKeyDown(int key) {
				//super.onKeyDown(key);
			}
			
			@Override
			public void onKeyHeld(int key) {
				//super.onKeyHeld(key);
			}
			
			@Override
			public void onKeyUp(int key) {
				//super.onKeyUp(key);
			}
			
			@Override
			public void onMouseButtonDown(int button) {
				//super.onMouseButtonDown(button);
			}
			
			@Override
			public void onMouseButtonHeld(int button) {
				//super.onMouseButtonHeld(button);
			}
			
			@Override
			public void onMouseButtonUp(int button) {
				//super.onMouseButtonUp(button);
			}
			
			@Override
			public void onMouseMoved(int deltaX, int deltaY, int oldX, int oldY, int newX, int newY) {
				//super.onMouseMoved(deltaX, deltaY, oldX, oldY, newX, newY);
			}
		});
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
		//final int width = Window.getWindow().getWidth();
		final int height = Window.getWindow().getHeight();
		
		//this.mx = width / 2;
		//this.my = height / 2;
		
		java.awt.Point mLoc = Mouse.getLocation();
		java.awt.Point topLeft = Mouse.getCursorCanvasLocation();
		this.mx = mLoc.x - topLeft.x;
		this.my = height - (mLoc.y - topLeft.y);
		
		this.glSetCubeSize(0.5f, 0.5f, 0.5f);
		
		this.initialized = true;
	}
	
	public void glSetCubeSize(float width, float height, float length) {
		//Create the cube and load it into a VBO:
		this.cubeVertices = createCubeVertices(width, height, length);
		this.cubeVertexBuf = BufferUtil.createDirectFloatBuffer(this.cubeVertices.length);//ByteBuffer.allocateDirect((this.cubeVertices.length * Float.SIZE / Byte.SIZE)).order(ByteOrder.nativeOrder()).asFloatBuffer().rewind();
		for(int i = 0; i < this.cubeVertices.length; i++) {
			// (Debug stuff) System.out.println(i + " / " + this.cubeVertices.length + "(limit: " + this.cubeVertexBuf.limit() + ")");
			this.cubeVertexBuf.put(i, this.cubeVertices[i]);
		}
		this.cubeVertexBuf.rewind();
		if(this.vbo[0] == 0) {
			GL15.glGenBuffers(this.vbo);
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo[0]);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.cubeVertexBuf, GL15.GL_STATIC_DRAW);
	}
	
	public void glDrawCube(float x, float y, float z, float yaw, float pitch, float roll) {
		GL11.glLoadMatrixf(this.stack.push().loadIdentity().translate(x, y, z).rotate(yaw, pitch, roll).multMatrix4x4(this.modelView).popf());
		
		//Bind the cube's VBO:
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo[0]);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(0);
		
		//Adjust OpenGL settings and draw the cube:
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.cubeVertices.length / 3);
	}
	
	@Override
	public void onSelected() {
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
	}
	
	private void updateMousePosition(int deltaX, int deltaY) {
		Point mLoc = Mouse.getLocation();
		Point topLeft = Mouse.getCursorCanvasLocation();
		
		Point oldLoc = new Point(mLoc);
		oldLoc.x -= (topLeft.x + deltaX);
		oldLoc.y -= (topLeft.y + deltaY);
		Point newLoc = new Point(mLoc);
		newLoc.x -= topLeft.x;
		newLoc.y -= topLeft.y;
		
		this.onMouseMoved(deltaX, deltaY, oldLoc.x, oldLoc.y, newLoc.x, newLoc.y);
	}
	
	private volatile int lastWidth, lastHeight;
	
	@Override
	public void onViewportChanged(Rectangle oldViewport, Rectangle newViewport) {
		this.my = oldViewport.height - this.my;
		this.my = newViewport.height - this.my;
		if(Mouse.isCaptured()) {
			this.mx = Math.min(Window.getWindow().getWidth(), Math.max(0, this.mx));
			this.my = Math.min(Window.getWindow().getHeight(), Math.max(0, this.my));
		} else {
			this.updateMousePosition(0, 0);
		}
		
		GL11.glViewport(newViewport.x, newViewport.y, newViewport.width, newViewport.height);// Set the GL viewport
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadMatrixf(this._3D ? GLUtil.getPerspectiveMatrixf(70.0f, (newViewport.width + 0.0f) / (newViewport.height + 0.0f), 0.01f, 1000.0f, true) : GLUtil.getOrthographicMatrixf(newViewport.x, newViewport.y, newViewport.width, newViewport.height, 0.01f, 1000.0f));
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
		
		if(width != this.lastWidth || height != this.lastHeight || this._3D != this.last3D) {// Update the perspective matrix and misc. OpenGL settings if necessary
			this.lastWidth = width;
			this.lastHeight = height;
			this.last3D = this._3D;
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadMatrixf(this._3D ? GLUtil.getPerspectiveMatrixf(70.0f, (width + 0.0f) / (height + 0.0f), 0.01f, 1000.0f, true) : GLUtil.getOrthographicMatrixf(0, 0, width, height, 0.01f, 1000.0f));
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			
			if(!this._3D) {// Disable 3D settings when in 2D mode and restore default facing and culling behaviour
				this.set2DMode();
			}
		}
		
		if(!this._3D) {
			// Render a red rectangle that scales with the size of the canvas:
			GL11.glColor3f(0.85f, 0.12f, 0.27f);
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			GL11.glVertex2f(width * 0.25f, height * 0.25f);
			GL11.glVertex2f(width * 0.75f, height * 0.25f);
			GL11.glVertex2f(width * 0.25f, height * 0.75f);
			GL11.glVertex2f(width * 0.75f, height * 0.75f);
			GL11.glEnd();
			
			// Render a square with a fixed size that follows the mouse cursor:
			int mx = this.mx - (this.mWidth / 2);
			int my = this.my - (this.mHeight / 2);
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			GL11.glVertex2f(mx, my);
			GL11.glVertex2f(mx + this.mWidth, my);
			GL11.glVertex2f(mx, my + this.mHeight);
			GL11.glVertex2f(mx + this.mWidth, my + this.mHeight);
			GL11.glEnd();
		} else {
			this.set2DMode();
			// TODO Render font here! :D
			this.set3DMode();
			
			// Set the color of the cube:
			GL11.glColor3f(0.85f, 0.12f, 0.27f);
			// Draw the cube:
			this.glDrawCube(0, -1, -4, 0, 0, 0);
		}
		
	}
	
	private void set2DMode() {
		GL11.glLoadIdentity();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glFrontFace(GL11.GL_CCW);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	private void set3DMode() {
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glFrontFace(GL11.GL_CCW);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		// Update the modelView using our position and rotation:
		this.modelView = this.stack.modelView(this.x, this.y, this.z, this.yaw, this.pitch, this.roll).peekf();
		
	}
	
	@Override
	public void onDeselected() {
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_DONT_CARE);
	}
	
	@Override
	public void onCleanup() {
		GL15.glDeleteBuffers(this.vbo[0]);
		this.cubeVertexBuf = null;
		
		this.initialized = false;
	}
	
	//=============================================================================================
	
	@Override
	public boolean isModal() {
		return false;
	}
	
	private volatile long lastSecond = 0L;
	
	@Override
	public void input(double deltaTime) {
		if(Keyboard.getKeyDown(Keys.VK_M)) {
			this._3D = !this._3D;
		}
		if(this._3D) {
			float x = this.x, y = this.y, z = this.z;
			float yaw = this.yaw, pitch = this.pitch, roll = this.roll;
			if(Mouse.isCaptured()) {
				if(Keyboard.isKeyDown(Keys.VK_W)) {
					x -= this.modelView[2] * deltaTime;
					y -= this.modelView[6] * deltaTime;
					z -= this.modelView[10] * deltaTime;
				}
				if(Keyboard.isKeyDown(Keys.VK_S)) {
					x += this.modelView[2] * deltaTime;
					y += this.modelView[6] * deltaTime;
					z += this.modelView[10] * deltaTime;
				}
				if(Keyboard.isKeyDown(Keys.VK_A)) {
					x -= this.modelView[0] * deltaTime;
					y -= this.modelView[4] * deltaTime;
					z -= this.modelView[8] * deltaTime;
				}
				if(Keyboard.isKeyDown(Keys.VK_D)) {
					x += this.modelView[0] * deltaTime;
					y += this.modelView[4] * deltaTime;
					z += this.modelView[8] * deltaTime;
				}
				if(Keyboard.isKeyDown(Keys.VK_SPACE)) {
					x += this.modelView[1] * deltaTime;
					y += this.modelView[5] * deltaTime;
					z += this.modelView[9] * deltaTime;
				}
				if(Keyboard.isKeyDown(Keys.VK_LSHIFT)) {
					x -= this.modelView[1] * deltaTime;
					y -= this.modelView[5] * deltaTime;
					z -= this.modelView[9] * deltaTime;
				}
				
				java.awt.Point dxy = Mouse.getDXY(false);
				yaw += dxy.x * this.mouseSensitivity;
				pitch += dxy.y * this.mouseSensitivity;
				if(Keyboard.isKeyDown(Keys.VK_OPEN_BRACKET)) {
					roll -= deltaTime * this.mouseSensitivity * 100.0f;
				}
				if(Keyboard.isKeyDown(Keys.VK_CLOSE_BRACKET)) {
					roll += deltaTime * this.mouseSensitivity * 100.0f;
				}
				
				if(Keyboard.isKeyDown(Keys.VK_R)) {
					x = y = z = yaw = pitch = roll = 0;
				}
			}
			
			yaw = (360.0f + yaw) % 360.0f;
			pitch = (360.0f + pitch) % 360.0f;
			roll = (360.0f + roll) % 360.0f;
			if(!this.freeLook) {
				pitch = pitch > 90.0f && pitch <= 180.0f ? 90.0f : (pitch < 270.0f && pitch > 180.0f ? 270.0f : pitch);
			}
			
			//Using the stack from the display thread and the GLThread at the same time causes the rendered frame to sometimes flicker about, so we won't do that.
			//this.modelView = this.stack.modelView(this.x, this.y, this.z, this.yaw, this.pitch, this.roll).peekf();
			
			long now = System.currentTimeMillis();
			if(now - this.lastSecond >= 1000L || (x != this.lastX || y != this.lastY || z != this.lastZ) || (yaw != this.lastYaw || pitch != this.lastPitch || roll != this.lastRoll)) {
				this.lastSecond = now;
				this.lastX = this.x = x;
				this.lastY = this.y = y;
				this.lastZ = this.z = z;
				this.lastYaw = this.yaw = yaw;
				this.lastPitch = this.pitch = pitch;
				this.lastRoll = this.roll = roll;
				System.out.println("===================================");
				System.out.println(String.format("X: %s; Y: %s; Z: %s;", CodeUtil.limitDecimalNoRounding(-this.x, 4, true), CodeUtil.limitDecimalNoRounding(-this.y, 4, true), CodeUtil.limitDecimalNoRounding(-this.z, 4, true)));
				System.out.println("===================================");
				System.out.println(String.format("Yaw: %s; Pitch: %s; Roll: %s;", CodeUtil.limitDecimalNoRounding(this.yaw, 4, true), CodeUtil.limitDecimalNoRounding(this.pitch, 4, true), CodeUtil.limitDecimalNoRounding(this.roll, 4, true)));
				System.out.println(GLUtil.matrix4x4ToStringf(this.modelView, 4, true));
			}
		}
	}
	
	@Override
	public void update(double deltaTime) {
		
	}
	
	@Override
	public void onMouseMoved(int deltaX, int deltaY, int oldX, int oldY, int newX, int newY) {
		if(!this._3D) {
			if(Mouse.isCaptured()) {
				this.mx = Math.min(Window.getWindow().getWidth(), Math.max(0, this.mx + deltaX));
				this.my = Math.min(Window.getWindow().getHeight(), Math.max(0, this.my - deltaY));
			} else {
				this.mx = newX;
				this.my = Window.getWindow().getHeight() - newY;
			}
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
	
	@Override
	public void onControllerConnected(Controller controller) {
		
	}
	
	@Override
	public void onControllerDisconnected(Controller controller) {
		
	}
	
	//=============================================================================================
	
	@Override
	public String getMenuName() {
		return "Test Game Options";
	}
	
	@Override
	public void onMenuBarCreation(Menu menu) {
		this.mntmToggle2D3D = new MenuItem(menu, SWT.CHECK);
		this.mntmToggle2D3D.setText("Enable 3D Mode\tM");
		this.mntmToggle2D3D.setToolTipText("Toggles between 2D mode and 3D mode");
		this.mntmToggle2D3D.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TestGame.this._3D = !TestGame.this._3D;
				TestGame.this.mntmToggle2D3D.setSelection(TestGame.this._3D);
			}
		});
		
		final MenuItem mntmCameraOptions = new MenuItem(menu, SWT.NONE);
		mntmCameraOptions.setText("Camera Options");
		mntmCameraOptions.setToolTipText("Allows you to change settings such as mouse sensitivity, free-look mode, etc.");
		mntmCameraOptions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mntmCameraOptions.setEnabled(false);
				
				Window window = Window.getWindow();
				Shell parent = window.getShell();
				Shell shell = new Shell(parent, SWT.DIALOG_TRIM);
				try {
					shell.setText(TestGame.this.getName().concat(" 3D Camera Options"));
					Image[] images = parent.getImages();
					if(images.length > 0) {
						shell.setImages(images);
					} else {
						shell.setImage(parent.getImage());
					}
					shell.setSize(450, 320);
					
					shell.open();
					shell.layout();
					
					while(window.getActiveRenderer() == TestGame.this && !window.isFullscreen() && window.swtLoop() && !shell.isDisposed()) {
						Mouse.setCaptured(false);
					}
				} finally {
					shell.dispose();
					if(!mntmCameraOptions.isDisposed()) {
						mntmCameraOptions.setEnabled(true);
					}
				}
			}
		});
	}
	
	@Override
	public void onMenuBarDeletion(Menu menu) {
		//if(this.mntmToggle2D3D != null) {
		this.mntmToggle2D3D.dispose();
		this.mntmToggle2D3D = null;
		//}
		
	}
	
	@Override
	public void onPopupMenuCreation(Menu menu) {
		
	}
	
	@Override
	public void onPopupMenuDeletion(Menu menu) {
		
	}
	
	@Override
	public void updateMenuItems() {
		if(this.mntmToggle2D3D != null && !this.mntmToggle2D3D.isDisposed()) {
			SWTUtil.setSelection(this.mntmToggle2D3D, this._3D);
		}
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
		System.err.println(String.format("The renderer \"%s\" threw an exception while executing method %s(%s)!", this.getName(), method, parameters));
		System.err.flush();
		return false;
	}
	
}
