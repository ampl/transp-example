package com.example.transp;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;

/**
 * Plants view part
 */
public class PlantsPart extends TableViewerPart {
  @Override
  public void postConstruct(TranspService service, ErrorHandler errorHandler) {
    addColumn("Name", SWT.NONE, new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        return ((Plant) element).name;
      }
    });
    addColumn("Capacity", SWT.RIGHT, new ColumnLabelProvider() {
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

    SelectionChangedListener listener = new SelectionChangedListener(service,
        viewer);
    viewer.addSelectionChangedListener(listener);
    viewer.getControl().addFocusListener(listener);
  }
}
