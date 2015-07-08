package com.example.transp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.ampl.AMPL;

/**
 * A point in geographical coordinates: latitude and longitude.
 */
class LatLng {
	private double lat;
	private double lng;

	public LatLng(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public double lat() {
		return lat;
	}

	public double lng() {
		return lng;
	}

	@Override
	public String toString() {
		return String.format("{lat: %g, lng: %g}", lat, lng);
	}
}

class Location {
	String name;
	LatLng latlng;

	Location(String name, LatLng latlng) {
		this.name = name;
		this.latlng = latlng;
	}
}

class Plant extends Location {
	// Capacity of plant in cases
	double capacity;

	Plant(String name, double capacity, LatLng latlng) {
		super(name, latlng);
		this.capacity = capacity;
	}
}

class Market extends Location {
	// Demand at market in cases
	double demand;

	Market(String name, double demand, LatLng latlng) {
		super(name, latlng);
		this.demand = demand;
	}
}

interface LocationChangeListener {
	void setLocation(Location location);
}

/**
 * Transportation service - manages data and AMPL session
 */
public class TranspService {
	private Plant[] plants = new Plant[] { new Plant("Seattle", 350, new LatLng(47.6097, -122.3331)),
			new Plant("San Diego", 600, new LatLng(32.7150, -117.1625)) };

	private Market[] markets = new Market[] { new Market("New York", 325, new LatLng(40.7127, -74.0059)),
			new Market("Chicago", 300, new LatLng(41.8369, -87.6847)),
			new Market("Topeka", 275, new LatLng(39.0558, -95.6894)) };

	// distances[i, j] is a distance from plant i to market j in thousands of
	// miles
	private double[] distances = new double[] { 2.5, 1.7, 1.8, 2.5, 1.8, 1.4 };

	// Freight in dollars per case per thousand miles
	private double freight = 90;

	private List<LocationChangeListener> listeners = new ArrayList<LocationChangeListener>();

	public Plant[] plants() {
		return plants;
	}

	public Market[] markets() {
		return markets;
	}

	public double[] distances() {
		return distances;
	}

	public double freight() {
		return freight;
	}

	/**
	 * Set current location.
	 */
	public void setLocation(Location location) {
		for (LocationChangeListener listener : listeners)
			listener.setLocation(location);
	}

	public void addLocationChangeListener(LocationChangeListener listener) {
		listeners.add(listener);
	}

	public void removeLocationChangeListener(LocationChangeListener listener) {
		listeners.remove(listener);
	}
	
	public URL getResourceURL(String filename) throws IOException {
		Bundle bundle = FrameworkUtil.getBundle(TranspService.class);
		URL url = FileLocator.find(bundle, new Path(filename), null);
		return FileLocator.toFileURL(url);
	}

	public void optimize() throws IOException {
		try (AMPL ampl = new AMPL()) {
			ampl.read(getResourceURL("transp.ampl").getPath());
			ampl.solve();
			System.out.println(ampl.getVariable("x").value());
		}
	}
}
