package com.example.transp;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;

/**
 * Markets view part
 */
public class MarketsPart extends TableViewerPart {

  @Override
  public void postConstruct(TranspService service, ErrorHandler errorHandler) {
    addColumn("Name", SWT.NONE, new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        return ((Market) element).name;
      }
    });
    addColumn("Demand", SWT.RIGHT, new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        return Double.toString(((Market) element).demand);
      }
    }).setEditingSupport(new TableEditingSupport<Market>(viewer, errorHandler) {
      @Override
      protected double doGetValue(Market element) {
        return element.demand;
      }

      @Override
      protected void doSetValue(Market element, double value) {
        element.demand = value;
      }
    });

    viewer.setContentProvider(new ArrayContentProvider());
    viewer.setInput(service.markets());

    SelectionChangedListener listener = new SelectionChangedListener(service,
        viewer);
    viewer.addSelectionChangedListener(listener);
    viewer.getControl().addFocusListener(listener);
  }
}
