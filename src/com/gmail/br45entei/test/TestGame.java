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
import com.gmail.br45entei.game.graphics.AnimatedTexture;
import com.gmail.br45entei.game.graphics.FontRender;
import com.gmail.br45entei.game.graphics.FontRender.GLFont;
import com.gmail.br45entei.game.graphics.GLThread.InitializationProgress;
import com.gmail.br45entei.game.graphics.GLUtil;
import com.gmail.br45entei.game.graphics.MatrixStack;
import com.gmail.br45entei.game.graphics.MatrixStack.MultiplicationOrder;
import com.gmail.br45entei.game.graphics.MatrixStack.RotationOrder;
import com.gmail.br45entei.game.graphics.RandomColorGenerator;
import com.gmail.br45entei.game.graphics.RandomRotationGenerator;
import com.gmail.br45entei.game.graphics.Renderer;
import com.gmail.br45entei.game.graphics.Texture;
import com.gmail.br45entei.game.graphics.TextureLoader;
import com.gmail.br45entei.game.input.ControllerManager;
import com.gmail.br45entei.game.input.Keyboard;
import com.gmail.br45entei.game.input.Keyboard.Keys;
import com.gmail.br45entei.game.input.Mouse;
import com.gmail.br45entei.game.math.MathUtil;
import com.gmail.br45entei.game.ui.MenuProvider;
import com.gmail.br45entei.game.ui.Window;
import com.gmail.br45entei.util.SWTUtil;
import com.gmail.br45entei.util.StringUtil;

import java.nio.FloatBuffer;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.libsdl.SDL;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.swt.GLData;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

/** @author Brian_Entei &ltbr45entei&#064;gmail.com&gt; */
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
	public static final float[] createCubeVertices(float width, float height, float length) {
		width *= 0.5f;
		height *= 0.5f;
		length *= 0.5f;
		return new float[] {//@formatter:off
				//Front (facing towards -Z direction, away from camera)
				-width, height, -length, -width, -height, -length, width, -height, -length, width, -height, -length, width, height, -length, -width, height, -length,
				//Right (facing towards +X direction)
				width, -height, -length, width, -height, length, width, height, -length, width, -height, length, width, height, length, width, height, -length,
				//Back (facing towards +Z direction, towards camera)
				width, -height, length, -width, -height, length, width, height, length, -width, -height, length, -width, height, length, width, height, length,
				//Left (facing towards -X direction)
				-width, -height, length, -width, -height, -length, -width, height, length, -width, -height, -length, -width, height, -length, -width, height, length,
				//Bottom (facing towards -Y direction)
				-width, -height, length, width, -height, length, width, -height, -length, width, -height, -length, -width, -height, -length, -width, -height, length,
				//Top (facing towards +Y direction)
				-width, height, -length, width, height, -length, width, height, length, width, height, length, -width, height, length, -width, height, -length
		};//@formatter:on
	}
	
	private volatile boolean initialized = false, inputInitialized = false;
	private volatile int mx, my;
	private volatile int mWidth = 16, mHeight = 16;
	
	protected volatile boolean _3D = false;
	private volatile float x, y, z, zDist;
	private volatile float lastX, lastY, lastZ, lastZDist;
	private volatile float yaw, pitch, roll;
	private volatile float lastYaw, lastPitch, lastRoll;
	private final MatrixStack stack = new MatrixStack();
	private volatile float fovy = 70.0f, zNear = 0.01f, zFar = 1000.0f;
	private volatile float targetFovy = this.fovy;
	private volatile float zoomFovy = 20.0f;
	private volatile float lastZNear = this.zNear;
	private volatile float lastZFar = this.zFar;
	private volatile float[] orthographicProjection = this.stack.setOrthographicProjection(0, 0, 800, 600, this.zNear, this.zFar).peekf();
	private volatile float[] perspectiveProjection = this.stack.setPerspectiveProjection(this.fovy, 800, 600, this.zNear, this.zFar).peekf();
	private volatile float[] modelView = this.stack.setModelView(this.x, this.y, this.z, this.yaw, this.pitch, this.roll).peekf();
	
	private volatile boolean printCameraInfo = false;
	
	private volatile boolean freeLook = false;
	private volatile boolean invertYawWhenUpsideDown = false;
	private volatile boolean invertPitchWhenUpsideDown = false;
	private volatile float mouseSensitivity = 0.15f;
	private volatile boolean freeMove = false;
	private volatile boolean invertForwardMovementWhenUpsideDown = false;
	private volatile boolean invertVerticalMovementWhenUpsideDown = false;
	private volatile float movementSpeed = 1.2f;
	
	//============================================================
	
	private volatile int[] vbo = new int[1];
	private volatile float[] cubeVertices = null;
	private volatile FloatBuffer cubeVertexBuf = null;
	private final RandomColorGenerator cube1ColorGenerator = new RandomColorGenerator();
	private final RandomColorGenerator cube2ColorGenerator = new RandomColorGenerator();
	private final RandomColorGenerator cube3ColorGenerator = new RandomColorGenerator();
	private final RandomRotationGenerator cube3RotationGenerator = new RandomRotationGenerator();
	private final RandomColorGenerator cube4ColorGenerator = new RandomColorGenerator();
	
	//============================================================
	
	private volatile GLFont font = null;
	
	//============================================================
	
	protected volatile MenuItem mntmToggle2D3D = null;
	protected volatile MenuItem mntmCameraOptions = null;
	
	//============================================================
	
	/** Default constructor */
	public TestGame() {
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
		window.registerRenderer(Renderer.colorDemo);
		window.registerGame(new GameAdapter() {
			
			volatile AnimatedTexture[] animatedTextures = null;
			volatile int animatedTextureIndex = 0;
			
			volatile boolean initialized = false, inputInitialized = false;
			volatile int width, height;
			
			@Override
			public String getName() {
				return "Texture Test";
			}
			
			@Override
			public boolean isInitialized() {
				return this.initialized;
			}
			
			@Override
			public void initialize(InitializationProgress progress) {
				progress.set(0.0f, "Loading animation textures...", "blocks/lava_fall/0.png");
				if(this.animatedTextures == null) {
					this.animatedTextures = new AnimatedTexture[3];
					int j = 0;
					
					Texture[] textures = new Texture[16];
					for(int i = 0; i < textures.length; i++) {
						String path = String.format("blocks/lava_fall/%s.png", Integer.toString(i));
						progress.set(i / 256.0f, String.format("Loading animation textures (%s/256)\r\n(\"%s\")", Integer.toString(i + 1), path));
						textures[i] = TextureLoader.getTexture(path);
					}
					this.animatedTextures[j++] = new AnimatedTexture(160L, textures);
					
					String path = "textures/textures.Pixel-Furnace.com/Animated_Water_Normal_Map/%s.png";
					textures = new Texture[120];
					for(int i = 0; i < textures.length; i++) {
						String num = Integer.toString(i + 1);
						String curPath = String.format(path, StringUtil.lineOf('0', 4 - num.length()).concat(num));
						textures[i] = TextureLoader.getTexture(curPath);
						progress.set((i + 16.0f) / 256.0f, String.format("Loading animation textures (%s/256)\r\n(\"%s\")", Integer.toString(i + 17), curPath));
					}
					this.animatedTextures[j++] = new AnimatedTexture(3000.0 / Window.getDefaultRefreshRate(), textures);
					
					path = "textures/textures.Pixel-Furnace.com/Animated_Water_Normal_Map_2/%s.png";
					textures = new Texture[120];
					for(int i = 0; i < textures.length; i++) {
						String num = Integer.toString(i + 1);
						String curPath = String.format(path, StringUtil.lineOf('0', 4 - num.length()).concat(num));
						textures[i] = TextureLoader.getTexture(curPath);
						progress.set((i + 136.0f) / 256.0f, String.format("Loading animation textures (%s/256)\r\n(\"%s\")", Integer.toString(i + 137), curPath));
					}
					this.animatedTextures[j++] = new AnimatedTexture(textures);
				}
				
				progress.set(1.0f, "Whew, that was a lot of animation textures!");
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
			public void onSelected() {
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			
			@Override
			public void onViewportChanged(Rectangle oldViewport, Rectangle newViewport) {
				GL11.glViewport(0, 0, this.width = newViewport.width, this.height = newViewport.height);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadMatrixd(GLUtil.getOrthographicMatrixd(0, 0, this.width, this.height, 0.01, 1000.0));
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
			}
			
			@Override
			public void render(double deltaTime, int width, int height) {
				GL11.glClearColor(0, 0, 0, 1);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				this.animatedTextures[this.animatedTextureIndex].bind(deltaTime);
				GLUtil.glRenderQuad(null, new Vector2f(this.width, this.height), new Vector4f(1, 1, 1, 0));
			}
			
			@Override
			public void onDeselected() {
				Texture.unbindAllTextures();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
			}
			
			@Override
			public void onCleanup() {
				
				this.initialized = false;
			}
			
			@Override
			public void onKeyDown(int key) {
				Window window = Window.getCurrent();
				if(window != null && window.getActiveRenderer() == this) {
					if(key == Keys.VK_LEFT_ARROW || key == Keys.VK_RIGHT_ARROW) {
						int index = this.animatedTextureIndex + (key == Keys.VK_LEFT_ARROW ? -1 : 1);
						index = index < 0 ? this.animatedTextures.length - 1 : index;
						index = index >= this.animatedTextures.length ? 0 : index;
						this.animatedTextureIndex = index;
					}
				}
			}
			
			@Override
			public boolean handleException(Throwable ex, String method, Object... params) {
				return false;
			}
			
		});
		window.registerGame(new InterfaceTest());
		window.registerInputCallback(new InputLogger(System.out).setPrintKeyboardButtons(false).setPrintKeyboardButtonHelds(false).setPrintMouseButtons(false).setPrintMouseButtonHelds(false).setPrintControllerAxisChanges(false));
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
	public void initialize(InitializationProgress progress) {
		progress.set(0.25f, "Creating the goodness... :D", "blocks/demo/cogs.png");
		//final int width = Window.getWindow().getWidth();
		final int height = Window.getWindow().getHeight();
		
		//this.mx = width / 2;
		//this.my = height / 2;
		
		java.awt.Point mLoc = Mouse.getLocation();
		java.awt.Point topLeft = Mouse.getCursorCanvasLocation();
		this.mx = mLoc.x - topLeft.x;
		this.my = height - (mLoc.y - topLeft.y);
		
		this.glSetCubeSize(1f, 1f, 1f);
		
		if(this.font == null) {
			this.font = FontRender.createFont("Consolas", 12, false, false, true, true);
		}
		
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
	
	public void glSetCubeSize(float width, float height, float length) {
		//Create the cube and load it into a VBO:
		this.cubeVertices = createCubeVertices(width, height, length);
		this.cubeVertexBuf = BufferUtils.createFloatBuffer(this.cubeVertices.length);//BufferUtil.createDirectFloatBuffer(this.cubeVertices.length);//ByteBuffer.allocateDirect((this.cubeVertices.length * Float.SIZE) / Byte.SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer().rewind();
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
		GL11.glLoadMatrixf(this.stack.push().loadIdentity().multMatrix4x4(this.modelView).translate(x, y, z).rotate(yaw, pitch, roll).popf());
		
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
		java.awt.Point mLoc = Mouse.getLocation();
		java.awt.Point topLeft = Mouse.getCursorCanvasLocation();
		
		java.awt.Point oldLoc = new java.awt.Point(mLoc);
		oldLoc.x -= (topLeft.x + deltaX);
		oldLoc.y -= (topLeft.y + deltaY);
		java.awt.Point newLoc = new java.awt.Point(mLoc);
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
	public void render(double deltaTime, int width, int height) {
		if(width != this.lastWidth || height != this.lastHeight || this.fovy != this.targetFovy || this.zNear != this.lastZNear || this.zFar != this.lastZFar) {
			GL11.glViewport(0, 0, this.lastWidth = width, this.lastHeight = height);// Set the GL viewport
			if(!this._3D) {
				this.set2DMode(0, 0, width, height);
			}
		}
		GL11.glClearColor(0, 0, 0, 1);// Set the clear color
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);// Clear the viewport using the clear color
		
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
			// Render the 3D Environment:
			
			this.set3DMode(width, height);
			//GL11.glColor3f(0.85f, 0.12f, 0.27f);// Sets the color of the cube
			//this.glDrawCube(0, -1, -4, 0, 0, 0);// Draws the cube
			
			float[] rgb = this.cube1ColorGenerator.getColor();
			GL11.glColor3f(rgb[0], rgb[1], rgb[2]);
			this.glDrawCube(0, -1, -4, 0, 0, 0);
			rgb = this.cube2ColorGenerator.getColor();
			GL11.glColor3f(rgb[0], rgb[1], rgb[2]);
			this.glDrawCube(0, 0, 0, 0, 0, 0);
			rgb = this.cube3ColorGenerator.getColor();
			GL11.glColor3f(rgb[0], rgb[1], rgb[2]);
			float[] ypr = this.cube3RotationGenerator.getRotation();
			this.glDrawCube(7, 4, 4, ypr[0], ypr[1], ypr[2]);
			rgb = this.cube4ColorGenerator.getColor();
			GL11.glColor3f(rgb[0], rgb[1], rgb[2]);
			this.glDrawCube(4, 4, 7, 0, 0, 0);
			
			// Render the 2D UI:
			this.set2DMode(0, 0, width, height);
		}
		
		Window window = Window.getWindow();
		//System.out.println(StringUtil.getCurrentStackTraceElement());//System.out.println(StringUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String text = String.format("FPS: Current: %s; Last: %s; Vsync: %sabled;", MathUtil.limitDecimalNoRounding(window.getGLThread().getCurrentAverageFPS(), 4, true), Long.toString(window.getGLThread().getLastFPS()), (window.isVsyncEnabled() ? "en" : "dis"));
		if(this._3D) {
			text = text.concat("\r\n").concat(String.format("FreeLook: %sabled; FreeMove: %sabled; Camera Upside-down: %s;", this.freeLook ? "en" : "dis", this.freeMove ? "en" : "dis", Boolean.toString(this.isCameraUpsideDown())));
			text = text.concat("\r\n").concat(String.format("Mouse Sensitivity: %s; Movement Speed: %s;", MathUtil.limitDecimalNoRounding(this.mouseSensitivity, 4, true), MathUtil.limitDecimalNoRounding(this.movementSpeed, 4, true)));
			text = text.concat("\r\n").concat(String.format("X: %s; Y: %s; Z: %s; ~: %s;", MathUtil.limitDecimalNoRounding(this.x, 4, true), MathUtil.limitDecimalNoRounding(this.y, 4, true), MathUtil.limitDecimalNoRounding(this.z, 4, true), MathUtil.limitDecimalNoRounding(this.zDist, 4, true)));
			text = text.concat("\r\n").concat(String.format("Yaw: %s; Pitch: %s; Roll: %s;", MathUtil.limitDecimalNoRounding(this.yaw, 4, true), MathUtil.limitDecimalNoRounding(this.pitch, 4, true), MathUtil.limitDecimalNoRounding(this.roll, 4, true)));
			text = text.concat("\r\n").concat(String.format("Field of View: %s; zNear: %s; zFar: %s;", MathUtil.limitDecimalNoRounding(this.fovy, 4, true), MathUtil.limitDecimalNoRounding(this.zNear, 4, true), MathUtil.limitDecimalNoRounding(this.zFar, 4, true)));
			text = text.concat("\r\n").concat(String.format("Viewport: 0, 0, %s, %s;", Integer.toString(this.lastWidth), Integer.toString(this.lastHeight)));
			text = text.concat("\r\n").concat(GLUtil.matrix4x4ToStringf(this.orthographicProjection, 6, true).replaceFirst(Pattern.quote(StringUtil.lineOf('=', "==[2D Projection]".length())), Matcher.quoteReplacement("==[2D Projection]")));
			text = text.concat("\r\n").concat(GLUtil.matrix4x4ToStringf(this.perspectiveProjection, 8, true).replaceFirst(Pattern.quote(StringUtil.lineOf('=', "==[3D Projection]".length())), Matcher.quoteReplacement("==[3D Projection]")));
			text = text.concat("\r\n").concat(GLUtil.matrix4x4ToStringf(this.modelView, 6, true).replaceFirst(Pattern.quote(StringUtil.lineOf('=', "==[3D Model View]".length())), Matcher.quoteReplacement("==[3D Model View]")));
		}
		/*GLUtil.glPushColor();
		GLUtil.glPushBlendMode();
		GLUtil.glSetBlendEnabled(true);
		GLUtil.glColord(1, 0, 0, 0.25);
		GLUtil.glDrawRect2d(FontRender.sizeOf(this.font, text, 0, height).getBounds());
		GLUtil.glPopColor();
		GLUtil.glPopBlendMode();//*/
		FontRender.drawString(this.font, text, 0, height - (this.font.getLineHeight() * 0), 1, 1, 1);
		
		if(this._3D) {
			ControllerManager manager = Window.getWindow().getControllerManager();
			if(Mouse.isCaptured() || (manager != null && manager.getController1() != null)) {
				GLUtil.glLegacyRenderCrosshairsAt(this.lastWidth / 2.0, this.lastHeight / 2.0, 1.0, true);
			} else {
				GLUtil.glLegacyRenderCrosshairsOnMouse(true);
			}
		}
		
	}
	
	private void set2DMode(float x, float y, float width, float height) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadMatrixf(this.orthographicProjection = GLUtil.getOrthographicMatrixf(x, y, width, height, this.zNear, this.zFar));
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glFrontFace(GL11.GL_CCW);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	private void set3DMode(float width, float height) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadMatrixf(this.perspectiveProjection = GLUtil.getPerspectiveMatrixf(/*this.lastFovy = */this.fovy, (width + 0.0f) / (height + 0.0f), this.lastZNear = this.zNear, this.lastZFar = this.zFar, true));
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadMatrixf(this.modelView = this.stack.loadIdentity().translate(0, 0, -this.zDist).rotate(-this.yaw, -this.pitch, -this.roll, RotationOrder.ZXY, MultiplicationOrder.NEWxOLD).translate(-this.x, -this.y, -this.z, MultiplicationOrder.NEWxOLD).peekf());//GL11.glLoadMatrixf(this.modelView = this.stack.setModelView(this.x, this.y, this.z, this.yaw, this.pitch, this.roll).peekf());// Updates the modelView using our position and rotation
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glFrontFace(GL11.GL_CCW);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	@Override
	public void onDeselected() {
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_DONT_CARE);
	}
	
	@Override
	public void onCleanup() {
		GL15.glDeleteBuffers(this.vbo[0]);
		this.cubeVertexBuf = null;
		if(this.font != null) {
			this.font.destroy();
			this.font = null;
		}
		
		this.initialized = false;
	}
	
	//=============================================================================================
	
	@Override
	public boolean isModal() {
		return false;
	}
	
	private volatile long lastSecond = 0L;
	
	public void moveForward(double deltaTime) {
		double moveAmount = this.movementSpeed * deltaTime;
		if(this.freeMove) {
			this.x -= this.modelView[2] * moveAmount;
			this.y -= this.modelView[6] * moveAmount;
			this.z -= this.modelView[10] * moveAmount;
		} else {
			moveAmount *= (this.isCameraUpsideDown() && this.invertForwardMovementWhenUpsideDown ? -1.0f : 1.0f);
			this.x += this.modelView[8] * moveAmount;
			this.z -= this.modelView[0] * moveAmount;
		}
	}
	
	public void moveBackward(double deltaTime) {
		double moveAmount = this.movementSpeed * deltaTime;
		if(this.freeMove) {
			this.x += this.modelView[2] * moveAmount;
			this.y += this.modelView[6] * moveAmount;
			this.z += this.modelView[10] * moveAmount;
		} else {
			moveAmount *= (this.isCameraUpsideDown() && this.invertForwardMovementWhenUpsideDown ? -1.0f : 1.0f);
			this.x -= this.modelView[8] * moveAmount;
			this.z += this.modelView[0] * moveAmount;
		}
	}
	
	public void moveLeft(double deltaTime) {
		double moveAmount = this.movementSpeed * deltaTime;
		if(this.freeMove) {
			this.x -= this.modelView[0] * moveAmount;
			this.y -= this.modelView[4] * moveAmount;
			this.z -= this.modelView[8] * moveAmount;
		} else {
			this.x -= this.modelView[0] * moveAmount;
			this.z -= this.modelView[8] * moveAmount;
		}
	}
	
	public void moveRight(double deltaTime) {
		double moveAmount = this.movementSpeed * deltaTime;
		if(this.freeMove) {
			this.x += this.modelView[0] * moveAmount;
			this.y += this.modelView[4] * moveAmount;
			this.z += this.modelView[8] * moveAmount;
		} else {
			this.x += this.modelView[0] * moveAmount;
			this.z += this.modelView[8] * moveAmount;
		}
	}
	
	public void moveUp(double deltaTime) {
		double moveAmount = this.movementSpeed * deltaTime;
		if(this.freeMove) {
			this.x += this.modelView[1] * moveAmount;
			this.y += this.modelView[5] * moveAmount;
			this.z += this.modelView[9] * moveAmount;
		} else {
			this.y += moveAmount * (this.isCameraUpsideDown() && this.invertVerticalMovementWhenUpsideDown ? -1.0f : 1.0f);
		}
	}
	
	public void moveDown(double deltaTime) {
		double moveAmount = this.movementSpeed * deltaTime;
		if(this.freeMove) {
			this.x -= this.modelView[1] * moveAmount;
			this.y -= this.modelView[5] * moveAmount;
			this.z -= this.modelView[9] * moveAmount;
		} else {
			this.y -= moveAmount * (this.isCameraUpsideDown() && this.invertVerticalMovementWhenUpsideDown ? -1.0f : 1.0f);
		}
	}
	
	public void resetCameraFields() {
		this.x = this.y = this.z = this.zDist = this.yaw = this.pitch = this.roll = 0;
		this.zNear = 0.01f;
		this.zFar = 1000.0f;
		this.fovy = this.fovy == this.targetFovy || this.fovy == this.zoomFovy ? this.fovy : this.targetFovy;
	}
	
	/** Returns whether or not camera info is printed every second, as well as
	 * when it changes.
	 * 
	 * @return Whether or not camera info is printed every second and as it
	 *         changes */
	public boolean isPrintCameraInfoEnabled() {
		return this.printCameraInfo;
	}
	
	/** Sets whether or not this {@link TestGame}'s camera info is printed every
	 * second, as well as when it changes.
	 * 
	 * @param printCameraInfo Whether or not camera info is printed
	 * @return This TestGame */
	public TestGame setPrintCameraInfo(boolean printCameraInfo) {
		this.printCameraInfo = printCameraInfo;
		return this;
	}
	
	public float getMouseSensitivity() {
		return this.mouseSensitivity;
	}
	
	public TestGame setMouseSensitivity(float mouseSensitivity) {
		this.mouseSensitivity = mouseSensitivity != mouseSensitivity || Float.isInfinite(mouseSensitivity) ? this.mouseSensitivity : mouseSensitivity;
		return this;
	}
	
	public boolean isFreeLookEnabled() {
		return this.freeLook;
	}
	
	public TestGame setFreeLookEnabled(boolean freeLook) {
		this.freeLook = freeLook;
		return this;
	}
	
	public TestGame toggleFreeLook() {
		return this.setFreeLookEnabled(!this.isFreeLookEnabled());
	}
	
	public boolean isInvertYawWhileUpsideDownEnabled() {
		return this.invertYawWhenUpsideDown;
	}
	
	public TestGame setInvertYawWhileUpsideDownEnabled(boolean invertYawWhenUpsideDown) {
		this.invertYawWhenUpsideDown = invertYawWhenUpsideDown;
		return this;
	}
	
	public TestGame toggleInvertYawWhileUpsideDown() {
		return this.setInvertYawWhileUpsideDownEnabled(!this.isInvertYawWhileUpsideDownEnabled());
	}
	
	public boolean isInvertPitchWhileUpsideDownEnabled() {
		return this.invertPitchWhenUpsideDown;
	}
	
	public TestGame setInvertPitchWhileUpsideDownEnabled(boolean invertPitchWhenUpsideDown) {
		this.invertPitchWhenUpsideDown = invertPitchWhenUpsideDown;
		return this;
	}
	
	public TestGame toggleInvertPitchWhileUpsideDown() {
		return this.setInvertPitchWhileUpsideDownEnabled(!this.isInvertPitchWhileUpsideDownEnabled());
	}
	
	public float getMovementSpeed() {
		return this.movementSpeed;
	}
	
	public TestGame setMovementSpeed(float movementSpeed) {
		this.movementSpeed = movementSpeed != movementSpeed || Float.isInfinite(movementSpeed) ? this.movementSpeed : movementSpeed;
		return this;
	}
	
	public boolean isFreeMoveEnabled() {
		return this.freeMove;
	}
	
	public TestGame setFreeMoveEnabled(boolean freeMove) {
		this.freeMove = freeMove;
		return this;
	}
	
	public TestGame toggleFreeMove() {
		return this.setFreeMoveEnabled(!this.isFreeMoveEnabled());
	}
	
	public boolean isInvertForwardMovementWhileUpsideDownEnabled() {
		return this.invertForwardMovementWhenUpsideDown;
	}
	
	public TestGame setInvertForwardMovementWhileUpsideDownEnabled(boolean invertForwardMovementWhenUpsideDown) {
		this.invertForwardMovementWhenUpsideDown = invertForwardMovementWhenUpsideDown;
		return this;
	}
	
	public TestGame toggleInvertForwardMovementWhileUpsideDown() {
		return this.setInvertForwardMovementWhileUpsideDownEnabled(!this.isInvertForwardMovementWhileUpsideDownEnabled());
	}
	
	public boolean isInvertVerticalMovementWhileUpsideDownEnabled() {
		return this.invertVerticalMovementWhenUpsideDown;
	}
	
	public TestGame setInvertVerticalMovementWhileUpsideDownEnabled(boolean invertVerticalMovementWhenUpsideDown) {
		this.invertVerticalMovementWhenUpsideDown = invertVerticalMovementWhenUpsideDown;
		return this;
	}
	
	public TestGame toggleInvertVerticalMovementWhileUpsideDown() {
		return this.setInvertVerticalMovementWhileUpsideDownEnabled(!this.isInvertVerticalMovementWhileUpsideDownEnabled());
	}
	
	public boolean isCameraUpsideDown() {
		return Float.parseFloat(MathUtil.limitDecimalNoRounding(this.modelView[5], 4)) < -0.0f;//return this.pitch > 90.0f && this.pitch < 270.0f;
	}
	
	@Override
	public void input(double deltaTime) {
		if(Keyboard.getKeyDown(Keys.VK_M)) {
			this._3D = !this._3D;
		}
		if(this._3D) {
			float x = this.x, y = this.y, z = this.z, zDist = this.zDist;
			float yaw = this.yaw, pitch = this.pitch, roll = this.roll;
			if(Mouse.isCaptured() && Mouse.shouldIListenToClickEvents()) {
				double moveAmount = this.movementSpeed * deltaTime;
				final boolean upsideDown = this.isCameraUpsideDown();//this.modelView[4] < -0.0f;
				//final boolean pitchedOver = pitch > 90.0f && pitch < 270.0f;
				final boolean rolledOver = roll > 90.0f && roll < 270.0f;
				
				if(Keyboard.isKeyDown(Keys.VK_W)) {
					if(this.freeMove) {
						x -= this.modelView[2] * moveAmount;
						y -= this.modelView[6] * moveAmount;
						z -= this.modelView[10] * moveAmount;
					} else {
						x += this.modelView[8] * moveAmount * (upsideDown && this.invertForwardMovementWhenUpsideDown ? -1.0f : 1.0f);
						z -= this.modelView[0] * moveAmount * (upsideDown && this.invertForwardMovementWhenUpsideDown ? -1.0f : 1.0f);
					}
				}
				if(Keyboard.isKeyDown(Keys.VK_S)) {
					if(this.freeMove) {
						x += this.modelView[2] * moveAmount;
						y += this.modelView[6] * moveAmount;
						z += this.modelView[10] * moveAmount;
					} else {
						x -= this.modelView[8] * moveAmount * (upsideDown && this.invertForwardMovementWhenUpsideDown ? -1.0f : 1.0f);
						z += this.modelView[0] * moveAmount * (upsideDown && this.invertForwardMovementWhenUpsideDown ? -1.0f : 1.0f);
					}
				}
				if(Keyboard.isKeyDown(Keys.VK_A)) {
					if(this.freeMove) {
						x -= this.modelView[0] * moveAmount;
						y -= this.modelView[4] * moveAmount;
						z -= this.modelView[8] * moveAmount;
					} else {
						x -= this.modelView[0] * moveAmount;
						z -= this.modelView[8] * moveAmount;
					}
				}
				if(Keyboard.isKeyDown(Keys.VK_D)) {
					if(this.freeMove) {
						x += this.modelView[0] * moveAmount;
						y += this.modelView[4] * moveAmount;
						z += this.modelView[8] * moveAmount;
					} else {
						x += this.modelView[0] * moveAmount;
						z += this.modelView[8] * moveAmount;
					}
				}
				if(Keyboard.isKeyDown(Keys.VK_SPACE)) {
					if(this.freeMove) {
						x += this.modelView[1] * moveAmount;
						y += this.modelView[5] * moveAmount;
						z += this.modelView[9] * moveAmount;
					} else {
						y += moveAmount * (upsideDown && this.invertVerticalMovementWhenUpsideDown ? -1.0f : 1.0f);
					}
				}
				if(Keyboard.isKeyDown(Keys.VK_LSHIFT)) {
					if(this.freeMove) {
						x -= this.modelView[1] * moveAmount;
						y -= this.modelView[5] * moveAmount;
						z -= this.modelView[9] * moveAmount;
					} else {
						y -= moveAmount * (upsideDown && this.invertVerticalMovementWhenUpsideDown ? -1.0f : 1.0f);
					}
				}
				
				java.awt.Point dxy = Mouse.getΔXY(false);
				yaw += dxy.x * this.mouseSensitivity * (this.fovy / this.targetFovy) * (upsideDown && this.invertYawWhenUpsideDown ? -1.0f : 1.0f);
				pitch += dxy.y * this.mouseSensitivity * (this.fovy / this.targetFovy) * ((rolledOver && this.invertPitchWhenUpsideDown) ? -1.0f : 1.0f);
				
				//if(this.freeLook) {
				if(Keyboard.isKeyDown(Keys.VK_OPEN_BRACKET)) {
					roll -= deltaTime * this.mouseSensitivity * (this.fovy / this.targetFovy) * 100.0f;
				}
				if(Keyboard.isKeyDown(Keys.VK_CLOSE_BRACKET)) {
					roll += deltaTime * this.mouseSensitivity * (this.fovy / this.targetFovy) * 100.0f;
				}
				//}
				
				if(Keyboard.getKeyDown(Keys.VK_Z)) {
					this.fovy = this.zoomFovy;
				}
				if(Keyboard.getKeyUp(Keys.VK_Z)) {
					this.fovy = this.targetFovy;
				}
				
				if(Mouse.isButtonDown(Mouse.BUTTON_MIDDLE)) {
					zDist = 0;
				}
				if(Keyboard.isKeyDown(Keys.VK_R)) {
					x = y = z = yaw = pitch = roll = 0;
					this.resetCameraFields();
				}
			}
			
			yaw = (360.0f + yaw) % 360.0f;
			pitch = (360.0f + pitch) % 360.0f;
			roll = (360.0f + roll) % 360.0f;
			if(!this.freeLook) {
				pitch = pitch > 90.0f && pitch <= 180.0f ? 90.0f : (pitch < 270.0f && pitch > 180.0f ? 270.0f : pitch);
				if(roll > 180.0f) {
					roll -= 360.0f;
					roll *= 0.95f;
					roll += 360.0f;
				} else {
					roll *= 0.95f;
				}
				roll = (360.0f + roll) % 360.0f;
				roll = Math.abs(roll) < 0.01f || Math.abs(roll) > 359.99f ? 0.0f : roll;
			}
			
			//Using the stack from the display thread and the GLThread at the same time causes the rendered frame to sometimes flicker about, so we won't do that.
			//this.modelView = this.stack.modelView(this.x, this.y, this.z, this.yaw, this.pitch, this.roll).peekf();
			
			long now = System.currentTimeMillis();
			if(now - this.lastSecond >= 1000L || (x != this.lastX || y != this.lastY || z != this.lastZ || zDist != this.lastZDist) || (yaw != this.lastYaw || pitch != this.lastPitch || roll != this.lastRoll)) {
				this.lastSecond = now;
				this.lastX = this.x = x;
				this.lastY = this.y = y;
				this.lastZ = this.z = z;
				this.lastZDist = this.zDist = zDist;
				this.lastYaw = this.yaw = yaw;
				this.lastPitch = this.pitch = pitch;
				this.lastRoll = this.roll = roll;
				if(this.printCameraInfo) {
					System.out.println("===================================");
					System.out.println(String.format("X: %s; Y: %s; Z: %s;", MathUtil.limitDecimalNoRounding(this.x, 4, true), MathUtil.limitDecimalNoRounding(this.y, 4, true), MathUtil.limitDecimalNoRounding(this.z, 4, true)));
					System.out.println("===================================");
					System.out.println(String.format("Yaw: %s; Pitch: %s; Roll: %s;", MathUtil.limitDecimalNoRounding(this.yaw, 4, true), MathUtil.limitDecimalNoRounding(this.pitch, 4, true), MathUtil.limitDecimalNoRounding(this.roll, 4, true)));
					System.out.println(GLUtil.matrix4x4ToStringf(this.modelView, 4, true));
				}
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
		if(Window.getWindow().getActiveRenderer() != this) {
			return;
		}
		if(this._3D && Mouse.isCaptured() && Mouse.shouldIListenToClickEvents()) {
			if(vertical) {
				this.zDist -= count / 3.0f;
			}
		}
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
	
	@Override
	public void onControllerButtonDown(Controller controller, int button) {
		if(Window.getWindow().isVisible()) {
			ControllerManager manager = Window.getWindow().getControllerManager();
			if(controller == manager.getController1()) {
				if(button == SDL.SDL_CONTROLLER_BUTTON_DPAD_UP) {
					this.fovy = this.zoomFovy;
				}
				
			}
			
		}
		
	}
	
	@Override
	public void onControllerButtonHeld(Controller controller, int button, double deltaTime) {
		if(Window.getWindow().isVisible()) {
			ControllerManager manager = Window.getWindow().getControllerManager();
			if(controller == manager.getController1()) {
				if(button == SDL.SDL_CONTROLLER_BUTTON_A) {
					this.moveUp(deltaTime);
				}
				if(button == SDL.SDL_CONTROLLER_BUTTON_X) {
					this.moveDown(deltaTime);
				}
				if(button == SDL.SDL_CONTROLLER_BUTTON_BACK) {
					this.resetCameraFields();
				}
				if(button == SDL.SDL_CONTROLLER_BUTTON_LEFTSHOULDER || button == SDL.SDL_CONTROLLER_BUTTON_RIGHTSHOULDER) {
					//if(this.freeLook) {
					float roll = this.roll;
					if(button == SDL.SDL_CONTROLLER_BUTTON_LEFTSHOULDER) {
						roll -= deltaTime * this.mouseSensitivity * (this.fovy / this.targetFovy) * 100.0f;
					}
					if(button == SDL.SDL_CONTROLLER_BUTTON_RIGHTSHOULDER) {
						roll += deltaTime * this.mouseSensitivity * (this.fovy / this.targetFovy) * 100.0f;
					}
					this.lastRoll = this.roll = (360.0f + roll) % 360.0f;
					//}
				}
				
			}
			
		}
		
	}
	
	@Override
	public void onControllerButtonRepeat(Controller controller, int button) {
		if(Window.getWindow().isVisible()) {
			ControllerManager manager = Window.getWindow().getControllerManager();
			if(controller == manager.getController1()) {
				
			}
			
		}
		
	}
	
	@Override
	public void onControllerButtonUp(Controller controller, int button) {
		if(Window.getWindow().isVisible()) {
			ControllerManager manager = Window.getWindow().getControllerManager();
			if(controller == manager.getController1()) {
				if(button == SDL.SDL_CONTROLLER_BUTTON_DPAD_UP) {
					this.fovy = this.targetFovy;
				}
				
			}
			
		}
		
	}
	
	@Override
	public void onControllerButtonDoubleTapped(Controller controller, int button) {
		if(Window.getWindow().isVisible()) {
			ControllerManager manager = Window.getWindow().getControllerManager();
			if(controller == manager.getController1()) {
				
			}
			
		}
		
	}
	
	@Override
	public void onControllerAxisChanged(Controller controller, int axis, float oldValue, float newValue) {
		if(Window.getWindow().isVisible()) {
			ControllerManager manager = Window.getWindow().getControllerManager();
			if(controller == manager.getController1()) {
				
			}
			
		}
		
	}
	
	@Override
	public void onControllerAxisNonZero(Controller controller, int axis, float value, double deltaTime) {
		if(Window.getWindow().isVisible()) {
			ControllerManager manager = Window.getWindow().getControllerManager();
			if(controller == manager.getController1()) {
				final boolean upsideDown = this.isCameraUpsideDown();
				final boolean rolledOver = this.roll > 90.0f && this.roll < 270.0f;//this.modelView[4] != 0.0f;
				
				if(axis == SDL.SDL_CONTROLLER_AXIS_LEFTX) {
					this.moveRight(deltaTime * value);
				}
				if(axis == SDL.SDL_CONTROLLER_AXIS_LEFTY) {
					this.moveBackward(deltaTime * value);
				}
				
				if(axis == SDL.SDL_CONTROLLER_AXIS_RIGHTX || axis == SDL.SDL_CONTROLLER_AXIS_RIGHTY) {
					float yaw = this.yaw, pitch = this.pitch;
					
					if(axis == SDL.SDL_CONTROLLER_AXIS_RIGHTX) {
						yaw += value * this.mouseSensitivity * (this.fovy / this.targetFovy) * (upsideDown && this.invertYawWhenUpsideDown ? -1.0f : 1.0f) * 625.0f * deltaTime;
					}
					if(axis == SDL.SDL_CONTROLLER_AXIS_RIGHTY) {
						pitch += value * this.mouseSensitivity * (this.fovy / this.targetFovy) * (upsideDown && rolledOver && this.invertPitchWhenUpsideDown ? -1.0f : 1.0f) * 625.0f * deltaTime;
					}
					
					yaw = (360.0f + yaw) % 360.0f;
					pitch = (360.0f + pitch) % 360.0f;
					if(!this.freeLook) {
						pitch = pitch > 90.0f && pitch <= 180.0f ? 90.0f : (pitch < 270.0f && pitch > 180.0f ? 270.0f : pitch);
					}
					this.lastYaw = this.yaw = yaw;
					this.lastPitch = this.pitch = pitch;
				}
				
			}
			
		}
		
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
		
		this.mntmCameraOptions = new MenuItem(menu, SWT.CHECK);
		this.mntmCameraOptions.setText("Camera Options");
		this.mntmCameraOptions.setToolTipText("Allows you to change settings such as mouse sensitivity, free-look mode, etc.");
		this.mntmCameraOptions.addSelectionListener(new TestGameCameraOptionsSelectionListener(this, this.mntmCameraOptions));
	}
	
	@Override
	public void onMenuBarDeletion(Menu menu) {
		this.mntmToggle2D3D.dispose();
		this.mntmToggle2D3D = null;
		
		this.mntmCameraOptions.dispose();
		this.mntmCameraOptions = null;
		
	}
	
	@Override
	public boolean providesPopupMenu() {
		return true;
	}
	
	@Override
	public void onPopupMenuCreation(final Menu menu) {
		MenuItem mntmTestItem = new MenuItem(menu, SWT.CHECK);
		mntmTestItem.setText("Test Item");
		mntmTestItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("You clicked the test item!");
			}
		});
	}
	
	@Override
	public void onPopupMenuDeletion(Menu menu) {
		for(MenuItem item : menu.getItems()) {
			item.dispose();
		}
	}
	
	@Override
	public void updateMenuItems() {
		if(this.mntmToggle2D3D != null && !this.mntmToggle2D3D.isDisposed()) {
			SWTUtil.setSelection(this.mntmToggle2D3D, this._3D);
			
			if(this.mntmCameraOptions != null && !this.mntmCameraOptions.isDisposed()) {
				if(!this.mntmCameraOptions.getSelection()) {
					SWTUtil.setEnabled(this.mntmCameraOptions, this.mntmToggle2D3D.getSelection());
				}
			}
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
