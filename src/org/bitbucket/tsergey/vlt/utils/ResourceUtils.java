package org.bitbucket.tsergey.vlt.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.ResourceUtil;

public class ResourceUtils {

	public static IFile getResource(Object o) {
		IFile file = null;
		if(o != null) {
			file = (IFile) Platform.getAdapterManager().getAdapter(o, IFile.class);
			if(file == null) {
				if(o instanceof IAdaptable) {
					file = (IFile) ((IAdaptable)o).getAdapter(IFile.class);
				}
			}
		}
		return file;
	}

	public static IFile getResourceFromEditor() {
		IFile file = null;
		IWorkbenchWindow window = getActiveWindow();
		if(window != null) {
			IWorkbenchPage page = window.getActivePage();
			if(page != null) {
				IEditorPart editorPart = page.getActiveEditor();
				if(editorPart != null) {
					IEditorInput editorInput = editorPart.getEditorInput();
					file = ResourceUtil.getFile(editorInput);
				}
			}
		}
		return file;
	}

	public static IWorkbenchWindow getActiveWindow() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			window = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		}
		return window;
	}

}
