package org.bitbucket.tsergey.vlt.handler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.tsergey.vlt.model.CommandBuilder.Command;
import org.bitbucket.tsergey.vlt.utils.ResourceUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.day.jcr.vault.cli.VaultFsApp;

public abstract class BaseHandler extends AbstractHandler {

	public abstract Object handle(String path, ExecutionEvent event) throws ExecutionException;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
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
			MessageDialog.openError(HandlerUtil.getActiveShell(event).getShell(), "Error", "There is no file selection");
		}
		
		// move to the jcr_root
		// TODO read jcrRoot from the project settings
		String jcrRoot = "d:/projects/four_seasons/4sea-ibe/trunk/fourseasons/ui/src/main/content/jcr_root/";
		File jcrRootFile = new File(jcrRoot);
		try {
			jcrRoot = jcrRootFile.getCanonicalPath();
		} catch (IOException e) {
			throw new ExecutionException("JCR_ROOT not configured", e);
		}
		System.setProperty("user.dir", jcrRoot);
		
		if(StringUtils.contains(path, jcrRoot)) {
			path = StringUtils.substring(path, StringUtils.length(jcrRoot) + 1);
		}
		return handle(path, event);
	}

	protected Object defaultHandlerAction(final Command command, ExecutionEvent event) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveShell(event).getShell());
		
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Vlt " + command.getCommand(), 10);
					System.out.println(Arrays.deepToString(command.toMainAppArgs()));
					VaultFsApp.main(command.toMainAppArgs());
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
