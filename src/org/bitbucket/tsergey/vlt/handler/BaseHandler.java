package org.bitbucket.tsergey.vlt.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public abstract class BaseHandler extends AbstractHandler {

	public abstract Object handle(ExecutionEvent arg0) throws ExecutionException;

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// move to the jcr_root
		String jcrRoot = "d:/projects/four_seasons/4sea-ibe/trunk/fourseasons/ui/src/main/content/jcr_root/";
		System.setProperty("user.dir", jcrRoot);
		return handle(arg0);
	}

}
