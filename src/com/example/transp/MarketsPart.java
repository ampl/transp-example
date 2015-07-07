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
 * Markets view
 */
public class MarketsPart {
	private TableViewer viewer;

	private void addColumn(String name, ColumnLabelProvider provider) {
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText(name);
		column.setLabelProvider(provider);
	}

	@PostConstruct
	public void postConstruct(Composite parent, TranspService service) {
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		addColumn("Name", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Market) element).name;
			}
		});
		addColumn("Demand", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return Double.toString(((Market) element).demand);
			}
		});

		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(service.markets());

		SelectionChangedListener listener =
				new SelectionChangedListener(service, viewer);
		viewer.addSelectionChangedListener(listener);
		viewer.getControl().addFocusListener(listener);

		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	@Focus
    public void onFocus() {
		viewer.getControl().setFocus();
    }
}
