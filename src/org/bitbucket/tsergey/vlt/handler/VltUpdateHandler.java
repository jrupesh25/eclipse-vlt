package org.bitbucket.tsergey.vlt.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.day.jcr.vault.cli.VaultFsApp;

public class VltUpdateHandler extends BaseHandler {

	@Override
	public Object handle(ExecutionEvent arg0) throws ExecutionException {
		System.out.println("======== Update ========");
		VaultFsApp.main(new String[]{"up", "-v", "--force", "apps/fourseasons/components/booking/bk1-1-booking-flow-indicator/bk1-1-booking-flow-indicator.jsp"});
		System.out.println("======== Success ========");
		
		return null;
	}

}
