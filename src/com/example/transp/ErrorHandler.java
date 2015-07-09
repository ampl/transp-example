package com.example.transp;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.osgi.framework.FrameworkUtil;

public class ErrorHandler {
  public void handle(String errorMessage) {
    String id = FrameworkUtil.getBundle(ErrorHandler.class).getSymbolicName();
    ErrorDialog.openError(null, "Error", null,
        new Status(IStatus.ERROR, id, errorMessage));
  }

  public void handle(Exception e) {
    handle(e.toString());
  }
}
