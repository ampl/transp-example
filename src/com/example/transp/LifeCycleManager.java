package com.example.transp;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;

/**
 * Lifecycle manager that registers a transportation service in the
 * Eclipse context.
 */
@SuppressWarnings("restriction")
public class LifeCycleManager {
	@PostContextCreate
	void postContextCreate(IEclipseContext context) {
		context.set(TranspService.class, new TranspService());
	}
}
