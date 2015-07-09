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
import com.ampl.DataFrame;

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

interface ShipmentsChangeListener {
	void shipmentsChanged();
}

/**
 * Transportation service - manages data and AMPL session
 */
public class TranspService {
	private Plant[] plants = new Plant[] {
			new Plant("Seattle", 350, new LatLng(47.6097, -122.3331)),
			new Plant("San Diego", 500, new LatLng(32.7150, -117.1625)),
			new Plant("Charlotte", 250, new LatLng(35.2269, -80.8433))
	};

	private Market[] markets = new Market[] {
			new Market("New York", 325, new LatLng(40.7127, -74.0059)),
			new Market("Chicago", 300, new LatLng(41.8369, -87.6847)),
			new Market("Topeka", 275, new LatLng(39.0558, -95.6894)),
			new Market("Pittsburgh", 200, new LatLng(40.4397, -79.9764))
	};

	// distances[i][j] is a distance from plant i to market j in thousands of
	// miles
	private double[][] distances = new double[][] {
		new double[] {2857, 2064, 1851, 2529},
		new double[] {2761, 2083, 1514, 2411},
		new double[] { 629,  755, 1024,  447}
	};

	// Freight in dollars per case per thousand miles
	private double freight = 90;
	
	// shipments[i][j] is a shipment from plant i to market j in cases
	private double[][] shipments = new double[][] {
		new double[4], new double[4], new double[4]
	};

	private List<LocationChangeListener> locationChangeListeners =
			new ArrayList<LocationChangeListener>();

	private List<ShipmentsChangeListener> shipmentsChangeListeners =
			new ArrayList<ShipmentsChangeListener>();

	public Plant[] plants() {
		return plants;
	}

	public Market[] markets() {
		return markets;
	}

	public double[][] distances() {
		return distances;
	}

	public double freight() {
		return freight;
	}

	public void setFreight(double value) {
		freight = value;
	}

	public double[][] shipments() {
		return shipments;
	}

	/**
	 * Set current location.
	 */
	public void setLocation(Location location) {
		for (LocationChangeListener listener : locationChangeListeners)
			listener.setLocation(location);
	}

	public void addLocationChangeListener(LocationChangeListener listener) {
		locationChangeListeners.add(listener);
	}

	public void removeLocationChangeListener(LocationChangeListener listener) {
		locationChangeListeners.remove(listener);
	}

	public void addShipmentsChangeListener(ShipmentsChangeListener listener) {
		shipmentsChangeListeners.add(listener);
	}

	public URL getResourceURL(String filename) throws IOException {
		Bundle bundle = FrameworkUtil.getBundle(TranspService.class);
		URL url = FileLocator.find(bundle, new Path(filename), null);
		return FileLocator.toFileURL(url);
	}
	
	public void optimize() throws IOException {
		try (AMPL ampl = new AMPL()) {
			// Load model.
			ampl.read(getResourceURL("transp.ampl").getPath());

			// Pass data to AMPL.
			Object[] plantIDs = new Object[plants.length];
			DataFrame df = new DataFrame(1, "Plants", "Capacity");
			for (int i = 0; i < plants.length; i++) {
				plantIDs[i] = i;
				df.addRow(i, plants[i].capacity);
			}
			ampl.setData(df, "Plants");
			
			Object[] marketIDs = new Object[markets.length];
			df = new DataFrame(1, "Markets", "Demand");
			for (int i = 0; i < markets.length; i++) {
				marketIDs[i] = i;
				df.addRow(i, markets[i].demand);
			}
			ampl.setData(df, "Markets");
			
			df = new DataFrame(2, "Plants", "Markets", "Distance");
			df.setMatrix(distances, plantIDs, marketIDs);
			ampl.setData(df);
			
			ampl.getParameter("Freight").set(freight);

			// Solve the problem.
			ampl.solve();

			// Get solution.
			double[] data = ampl.getData("shipment").getColumnAsDoubles("shipment");
			int index = 0;
			for (int i = 0; i < plants.length; i++) {
				for (int j = 0; j < markets.length; j++)
					shipments[i][j] = data[index++];
			}
			for (ShipmentsChangeListener listener: shipmentsChangeListeners)
				listener.shipmentsChanged();
		}
	}
}
