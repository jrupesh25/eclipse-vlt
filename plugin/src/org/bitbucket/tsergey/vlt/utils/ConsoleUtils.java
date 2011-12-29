package org.bitbucket.tsergey.vlt.utils;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

public class ConsoleUtils {

	public static MessageConsole findConsole(String name) {
		MessageConsole result = null;
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++) {
			if (name.equals(existing[i].getName())) {
				result = (MessageConsole) existing[i];
			}
		}
		if(result == null) {
			// no console found, so create a new one
			MessageConsole myConsole = new MessageConsole(name, null);
			conMan.addConsoles(new IConsole[] { myConsole });
			result = myConsole;
		}
		return result;
	}

}
