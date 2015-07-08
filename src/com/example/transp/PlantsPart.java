package com.example.transp;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/**
 * Selection changed and focus listener for PlantsPart and MarketsPart
 */
class SelectionChangedListener implements ISelectionChangedListener, FocusListener {
	private TranspService service;
	private TableViewer viewer;

	void setSelection(ISelection sel) {
		if (sel.isEmpty() || !(sel instanceof IStructuredSelection))
			return;
		service.setLocation((Location) ((IStructuredSelection) sel).getFirstElement());
	}

	SelectionChangedListener(TranspService service, TableViewer viewer) {
		this.service = service;
		this.viewer = viewer;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent e) {
		setSelection(e.getSelection());
	}

	@Override
	public void focusGained(FocusEvent e) {
		setSelection(viewer.getSelection());
	}

	@Override
	public void focusLost(FocusEvent e) {
		service.setLocation(null);
	}
}

/**
 * Plants view
 */
public class PlantsPart {
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
				return ((Plant) element).name;
			}
		});
		addColumn("Capacity", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return Double.toString(((Plant) element).capacity);
			}
		}).setEditingSupport(new TableEditingSupport<Plant>(viewer, errorHandler) {
			@Override
			protected double doGetValue(Plant element) {
				return element.capacity;
			}

			@Override
			protected void doSetValue(Plant element, double value) {
				element.capacity = value;
			}
		});

		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(service.plants());

		SelectionChangedListener listener = new SelectionChangedListener(service, viewer);
		viewer.addSelectionChangedListener(listener);
		viewer.getControl().addFocusListener(listener);

		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	@Focus
	public void onFocus(TranspService service) {
		viewer.getControl().setFocus();
	}
}
