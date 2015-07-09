package com.example.transp;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
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

public abstract class TableViewerPart {
	protected TableViewer viewer;
	protected int columnWidth = 200;

	protected TableViewerColumn addColumn(String name, int style, ColumnLabelProvider provider) {
		TableViewerColumn column = new TableViewerColumn(viewer, style);
		column.getColumn().setWidth(columnWidth);
		column.getColumn().setText(name);
		column.setLabelProvider(provider);
		return column;
	}
	
	protected abstract void postConstruct(TranspService service, ErrorHandler errorHandler);

	@PostConstruct
	public final void postConstruct(
			Composite parent, TranspService service, ErrorHandler errorHandler) {
		viewer = new TableViewer(parent,
				SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		postConstruct(service, errorHandler);

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	@Focus
	public void onFocus(TranspService service) {
		viewer.getControl().setFocus();
	}
}
