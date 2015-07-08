
package com.example.transp;

import java.io.IOException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

public class OptimizeHandler {
	@Execute
	public void execute(Display display, final TranspService service, final ErrorHandler errorHandler) {
		BusyIndicator.showWhile(display, new Runnable() {
			@Override
			public void run() {
				try {
					service.optimize();
				} catch (IOException e) {
					errorHandler.handle(e);
				}
			}
		});
	}
}
