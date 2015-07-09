package com.example.transp;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;

/**
 * Distances view part
 */
public class DistancesPart extends TableViewerPart {

	@Override
	public void postConstruct(final TranspService service, ErrorHandler errorHandler) {
		columnWidth = 100;
		final int numMarkets = service.markets().length;
		addColumn("Plant", SWT.NONE, new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return service.plants()[(int) element / numMarkets].name;
			}
		});
		addColumn("Market", SWT.NONE, new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return service.markets()[(int) element / numMarkets].name;
			}
		});
		addColumn("Distance", SWT.RIGHT, new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				int index = (int) element;
				return String.format("%.1f",
						service.distances()[index / numMarkets][index % numMarkets]);
			}
		}).setEditingSupport(new TableEditingSupport<Integer>(viewer, errorHandler) {
			@Override
			protected double doGetValue(Integer element) {
				int index = (int) element;
				return service.distances()[index / numMarkets][index % numMarkets];
			}

			@Override
			protected void doSetValue(Integer element, double value) {
				int index = (int) element;
				service.distances()[index / numMarkets][index % numMarkets] = value;
			}
		});

		Integer[] inputs = new Integer[service.plants().length * numMarkets];
		for (int i = 0; i < inputs.length; i++)
			inputs[i] = i;
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(inputs);
	}
}
