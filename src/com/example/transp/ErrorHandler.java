package com.example.transp;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
//import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.FrameworkUtil;

public class ErrorHandler {
	public void handle(String errorMessage) {
		String id = FrameworkUtil.getBundle(ErrorHandler.class).getSymbolicName();
		//StatusManager.getManager().handle(
		//		new Status(IStatus.ERROR, id, errorMessage), StatusManager.SHOW);
	}

	public void handle(Exception e) {
		handle(e.toString());
	}
}
