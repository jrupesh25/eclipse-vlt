package org.bitbucket.tsergey.vlt.handler;

import org.bitbucket.tsergey.vlt.model.CommandBuilder;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class VltDeleteHandler extends BaseHandler {

	@Override
	public Object handle(final String path, ExecutionEvent event) throws ExecutionException {
		CommandBuilder deleteBuilder = CommandBuilder.newInstance();
		deleteBuilder.setCommand("delete")
			.setArgs(new String[]{"-v", "--force"})
			.setPath(path);
		Object result = defaultHandlerAction(deleteBuilder.buildCommand(), event);
		
		CommandBuilder commitBuilder = CommandBuilder.newInstance();
		commitBuilder.setCommand("ci")
			.setArgs(new String[]{"-v", "--force"})
			.setPath(path);
		defaultHandlerAction(commitBuilder.buildCommand(), event);
		
		return result;
	}

}
