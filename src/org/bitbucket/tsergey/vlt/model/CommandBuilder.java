package org.bitbucket.tsergey.vlt.model;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bitbucket.tsergey.vlt.exception.VaultException;

public class CommandBuilder {

	private Command command;

	private CommandBuilder() {
		command = new Command();
	}

	public static CommandBuilder newInstance() {
		return new CommandBuilder();
	}

	public CommandBuilder setCommand(String cmd) {
		command.setCommand(cmd);
		return this;
	}

	public CommandBuilder setArgs(String[] args) {
		command.setArgs(args);
		return this;
	}

	public CommandBuilder setPath(String path) {
		command.setPath(path);
		return this;
	}

	public Command buildCommand() {
		if(StringUtils.isBlank(command.getCommand()) && StringUtils.isBlank(command.getPath())) {
			throw new VaultException(VaultException.Type.COMMAND_CONSTRUCT_ERROR);
		}
		return command;
	}

	public static class Command {

		private String command;
		private String[] args;
		private String path;
	
		private Command() {}
	
		public String getCommand() {
			return command;
		}
	
		private void setCommand(String command) {
			this.command = command;
		}
	
		public String[] getArgs() {
			return args;
		}
	
		private void setArgs(String[] args) {
			this.args = args;
		}
	
		public String getPath() {
			return path;
		}
	
		private void setPath(String path) {
			this.path = path;
		}
	
		public String[] toMainAppArgs() {
			String[] result = new String[0];
			result = (String[]) ArrayUtils.add(result, command);
			result = (String[]) ArrayUtils.addAll(result, args);
			result = (String[]) ArrayUtils.add(result, path);
			return result;
		}
	}

}
