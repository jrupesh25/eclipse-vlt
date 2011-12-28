package org.bitbucket.tsergey.vlt;

import java.io.PrintStream;

import org.bitbucket.tsergey.vlt.utils.ConsoleUtils;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.bitbucket.tsergey.vlt"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		// TODO find out a way how to correctly redirect vlt out to the eclipse console.
		System.setOut(new PrintStream(ConsoleUtils.findConsole(Activator.PLUGIN_ID).newOutputStream()));
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

}
