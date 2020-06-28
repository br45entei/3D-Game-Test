package com.gmail.br45entei.test;

import com.gmail.br45entei.game.Game.GameAdapter;
import com.gmail.br45entei.game.ui.Window;

/** @author Brian_Entei */
public class InterfaceTest implements GameAdapter {
	
	/** @param args Program command line arguments */
	public static final void main(String[] args) {
		new Window(null, 800, 600, Window.getDefaultRefreshRate(), Window.createDefaultGLData(), new InterfaceTest()).setIconImages(new String[] {null});
		while(true) {
			Window.getWindow().open();// Mwuahahahaha!
		}
	}
	
	private volatile boolean initialized = false;
	
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
	public void initialize() {
		
		this.initialized = true;
	}
	
	@Override
	public void onCleanup() {
		
		this.initialized = false;
	}
	
}
