package org.bitbucket.tsergey.vlt.handler;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.tsergey.vlt.Activator;
import org.bitbucket.tsergey.vlt.exception.VaultException;
import org.bitbucket.tsergey.vlt.exception.VaultException.Type;
import org.bitbucket.tsergey.vlt.messages.Messages;
import org.bitbucket.tsergey.vlt.model.CommandBuilder;
import org.bitbucket.tsergey.vlt.model.CommandBuilder.Command;
import org.bitbucket.tsergey.vlt.preferences.GeneralPreferencesPage;
import org.bitbucket.tsergey.vlt.utils.ResourceUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;

import com.day.jcr.vault.cli.VaultFsApp;

public abstract class BaseHandler extends AbstractHandler {

	public abstract Object handle(String path, ExecutionEvent event) throws ExecutionException;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object result = null;
		try {
			String jcrRoot = ResourceUtils.initJCRRoot();
			String path = ResourceUtils.retrieveSelectedPath(event, jcrRoot);
			result = handle(path, event);
		} catch(VaultException e) {
			Type type = e.getType();
			Status myStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, type.toString(), e);
			StatusManager.getManager().handle(myStatus, StatusManager.SHOW);
		}
		return result;
	}

	protected Object defaultHandlerAction(Command command, ExecutionEvent event) {
		String uname = Activator.getDefault().getPreferenceStore().getString(GeneralPreferencesPage.USER_NAME);
		String upass = Activator.getDefault().getPreferenceStore().getString(GeneralPreferencesPage.USER_PASS);
		
		if(StringUtils.isBlank(upass) && StringUtils.isBlank(uname)) {
			throw new VaultException(Type.CREDENTIALS_CONFIG_ERROR, Messages.get(Messages.ERRORS_CREDENTIALS_CONFIG));
		}
		String[] credentials = new String[]{"--credentials", uname + ":" + upass};
		CommandBuilder builder = CommandBuilder.newInstance();
		builder.setCommand(command.getCommand())
			.setArgs(command.getArgs())
			.setPath(command.getPath())
			.setOperationArgs(credentials);
		final Command cmd = builder.buildCommand();
		
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveShell(event).getShell());
		
		try {
			dialog.run(true, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(Messages.get(Messages.TITLE_OPERATION + cmd.getCommand()), 10);
					monitor.worked(5);
					VaultFsApp.main(cmd.toMainAppArgs());
					monitor.done();
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Boolean.TRUE;
	}

}
