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
 * Options view part
 */
public class OptionsPart {
	private TableViewer viewer;

	private TableViewerColumn addColumn(String name, ColumnLabelProvider provider) {
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText(name);
		column.setLabelProvider(provider);
		return column;
	}

	@PostConstruct
	public void postConstruct(Composite parent, TranspService service, ErrorHandler errorHandler) {
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		addColumn("Name", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "Freight";
			}
		});
		addColumn("Value", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return Double.toString(((TranspService) element).freight());
			}
		}).setEditingSupport(new TableEditingSupport<TranspService>(viewer, errorHandler) {
			@Override
			protected double doGetValue(TranspService element) {
				return element.freight();
			}

			@Override
			protected void doSetValue(TranspService element, double value) {
				element.setFreight(value);
			}
		});

		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(new Object[] { service });

		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	@Focus
	public void onFocus(TranspService service) {
		viewer.getControl().setFocus();
	}
}
