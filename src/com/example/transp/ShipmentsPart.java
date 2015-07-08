 
package com.example.transp;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/**
 * Shipments view part
 */
public class ShipmentsPart {

	TableViewer viewer;

	private TableViewerColumn addColumn(String name, ColumnLabelProvider provider) {
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(100);
		column.getColumn().setText(name);
		column.setLabelProvider(provider);
		return column;
	}

	@PostConstruct
	public void postConstruct(Composite parent, final TranspService service) {
		viewer = new TableViewer(parent,
				SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		addColumn("", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return service.plants()[(int) element].name;
			}
		});
		final Market[] markets = service.markets();
		for (int i = 0; i < markets.length; i++) {
			final int market_index = i;
			addColumn(markets[i].name, new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return Double.toString(service.shipments()[(int) element][market_index]);
				}
			});
		}

		Integer[] inputs = new Integer[service.plants().length];
		for (int i = 0; i < inputs.length; i++)
			inputs[i] = i;
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(inputs);

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		service.addShipmentsChangeListener(new ShipmentsChangeListener() {
			@Override
			public void shipmentsChanged() {
				viewer.refresh();
			}
		});
	}

	@Focus
	public void onFocus() {
		viewer.getControl().setFocus();
	}
}