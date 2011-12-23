package org.bitbucket.tsergey.vlt.handler;

import java.util.Map;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.commandline.PropertiesCommandLine;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.day.jcr.vault.cli.VaultFsApp;

public class VltUpdateHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		
		System.out.println("======== Update ========");
		VaultFsApp app = new VaultFsApp();
		CommandLine cml = new PropertiesCommandLine(root, properties);
		app.execute(cml);
		
//		VaultFsApp.main(new String[]{""});
		
		return null;
	}

}
