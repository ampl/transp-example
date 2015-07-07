package com.example.transp;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * Map view
 */
public class MapPart {
	private Browser browser;
	private LocationChangeListener listener;

	private void addMarkers(Location[] locations, String color) {
		StringBuilder sb = new StringBuilder("addMarkers({");
		// In real application the name should be escaped.
		for (Location plant : locations)
			sb.append(String.format("'%s': %s, ", plant.name, plant.latlng));
		sb.append(String.format("}, '%s');", color));
		browser.execute(sb.toString());
	}

	@PostConstruct
	public void postConstruct(Composite parent, final TranspService service) throws IOException {
		Bundle bundle = FrameworkUtil.getBundle(MapPart.class);
		URL url = FileLocator.find(bundle, new Path("map.html"), null);
		browser = new Browser(parent, SWT.NONE);
		browser.setUrl(FileLocator.toFileURL(url).toString());

		browser.addProgressListener(new ProgressListener() {
			@Override
			public void completed(ProgressEvent event) {
				addMarkers(service.plants(), "01bf00");
				addMarkers(service.markets(), "6b98ff");
			}

			@Override
			public void changed(ProgressEvent event) {
			}
		});

		listener = new LocationChangeListener() {
			@Override
			public void setLocation(Location location) {
				String name = location != null ? String.format("'%s'", location.name) : "null";
				browser.execute(String.format("setLocation(%s);", name));
			}
		};
		service.addLocationChangeListener(listener);
	}

	@PreDestroy
	public void postConstruct(TranspService service) {
		service.removeLocationChangeListener(listener);
	}
}
