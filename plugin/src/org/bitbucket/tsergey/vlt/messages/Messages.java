package org.bitbucket.tsergey.vlt.messages;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

public final class Messages {

	public static final String PREFERENCES_JCR_ROOT_PATH = "preferences_jcr_root_path";
	public static final String PREFERENCES_USER_NAME = "preferences_user_name";
	public static final String PREFERENCES_USER_PASS = "preferences_user_pass";
	public static final String PREFERENCES_PAGE_DESCRIPTION = "preferences_page_description";
	public static final String ERRORS_CREDENTIALS_CONFIG = "errors_credentials_config";
	public static final String ERRORS_JCR_ROOT_CONFIG = "errors_jcr_root_config";
	public static final String ERRORS_FILE_NOT_SELECTED = "errors_file_not_selected";
	public static final String TITLE_OPERATION = "title_operation_";

	private static final String BUNDLE_NAME = "org.bitbucket.tsergey.vlt.messages.messages";

	private static final  ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {}

	public static String get(String key) {
		String result = StringUtils.EMPTY;
		try {
			result = RESOURCE_BUNDLE.getString(key);
		} catch(MissingResourceException e) {
			result = "?" + key + "?";
		}
		return result;
	}

	public static String get(String key, String... params) {
		String result = StringUtils.EMPTY;
		try {
			result = RESOURCE_BUNDLE.getString(key);
		} catch(MissingResourceException e) {
			result = "?" + key + "?";
		}
		result = MessageFormat.format(result, params);
		return result;
	}

}
