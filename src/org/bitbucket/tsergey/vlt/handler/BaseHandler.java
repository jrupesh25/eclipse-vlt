package org.bitbucket.tsergey.vlt.handler;

import java.io.File;
import java.io.IOException;
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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;

import com.day.jcr.vault.cli.VaultFsApp;

public abstract class BaseHandler extends AbstractHandler {

	public abstract Object handle(String path, ExecutionEvent event) throws ExecutionException;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String path = initPath(event);
		Object result = null;
		try {
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
			dialog.run(true, true, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(Messages.get(Messages.TITLE_OPERATION + cmd.getCommand()), 10);
					monitor.worked(5);
					System.out.println("Ececuting command: \nvlt " + StringUtils.join(cmd.toMainAppArgs(), " "));
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

	private String initPath(ExecutionEvent event) {
		String path = StringUtils.EMPTY;
		ISelection selectedObject = HandlerUtil.getCurrentSelection(event);
		IFile file = null;
		if(selectedObject != null) {
			if(selectedObject instanceof IStructuredSelection) {
				Object firstSelectedObject = ((IStructuredSelection)selectedObject).getFirstElement();
				file = ResourceUtils.getResource(firstSelectedObject);
			} else {
				file = ResourceUtils.getResource(selectedObject);
			}
		}
		if(file == null) {
			file = ResourceUtils.getResourceFromEditor();
		}
		if(file != null) {
			try {
				path = file.getLocation().toFile().getCanonicalPath();
			} catch (IOException e) {
				path = StringUtils.EMPTY;
			}
		}
		
		if(StringUtils.isBlank(path)) {
			throw new VaultException(Type.NO_FILE_SELECTION_ERROR, Messages.get(Messages.ERRORS_FILE_NOT_SELECTED));
		}
		
		// change working directory
		String jcrRoot = Activator.getDefault().getPreferenceStore().getString(GeneralPreferencesPage.JCR_ROOT_PATH);
		File jcrRootFile = new File(jcrRoot);
		try {
			jcrRoot = jcrRootFile.getCanonicalPath();
		} catch (IOException e) {
			throw new VaultException(Type.JCR_ROOT_CONFIG_ERROR, Messages.get(Messages.ERRORS_JCR_ROOT_CONFIG), e);
		}
		if(StringUtils.isBlank(jcrRoot)) {
			throw new VaultException(Type.JCR_ROOT_CONFIG_ERROR, Messages.get(Messages.ERRORS_JCR_ROOT_CONFIG));
		}
		System.setProperty("user.dir", jcrRoot);
		
		if(StringUtils.contains(path, jcrRoot)) {
			path = StringUtils.substring(path, StringUtils.length(jcrRoot) + 1);
		}
		return path;
	}

}
