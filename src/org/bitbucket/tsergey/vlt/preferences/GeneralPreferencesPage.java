package org.bitbucket.tsergey.vlt.preferences;

import org.bitbucket.tsergey.vlt.Activator;
import org.bitbucket.tsergey.vlt.messages.Messages;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbenchPropertyPage;

public class GeneralPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPropertyPage {

	public static final String JCR_ROOT_PATH = "jcr_root_path";
	public static final String USER_NAME = "user_name";
	public static final String USER_PASS = "user_pass";

	private IAdaptable element;

	public GeneralPreferencesPage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.get(Messages.PREFERENCES_PAGE_DESCRIPTION));
	}

	@Override
	protected void createFieldEditors() {
		DirectoryFieldEditor jcrRootField = new DirectoryFieldEditor(JCR_ROOT_PATH, Messages.get(Messages.PREFERENCES_JCR_ROOT_PATH), getFieldEditorParent());
		jcrRootField.setEmptyStringAllowed(false);
		addField(jcrRootField);
		
		StringFieldEditor unameField = new StringFieldEditor(USER_NAME, Messages.get(Messages.PREFERENCES_USER_NAME), getFieldEditorParent());
		unameField.setEmptyStringAllowed(false);
		addField(unameField);
		
		StringFieldEditor upassField = new StringFieldEditor(USER_PASS, Messages.get(Messages.PREFERENCES_USER_PASS), getFieldEditorParent());
		upassField.setEmptyStringAllowed(false);
		addField(upassField);
	}

	@Override
	public IAdaptable getElement() {
		return element;
	}

	@Override
	public void setElement(IAdaptable element) {
		this.element = element;
	}

}
