package org.bitbucket.tsergey.vlt.handler;

import org.bitbucket.tsergey.vlt.model.CommandBuilder;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class VltStatusHandler extends BaseHandler {

	@Override
	public Object handle(final String path, ExecutionEvent event) throws ExecutionException {
		CommandBuilder builder = CommandBuilder.newInstance();
		builder.setCommand("status")
			.setArgs(new String[]{"-v"})
			.setPath(path);
		return defaultHandlerAction(builder.buildCommand(), event);
	}

}
