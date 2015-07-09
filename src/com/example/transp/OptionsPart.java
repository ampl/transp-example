package com.example.transp;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;

/**
 * Options view part
 */
public class OptionsPart extends TableViewerPart {

  @Override
  public void postConstruct(TranspService service, ErrorHandler errorHandler) {
    addColumn("Name", SWT.NONE, new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        return "Freight";
      }
    });
    addColumn("Value", SWT.RIGHT, new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        return Double.toString(((TranspService) element).freight());
      }
    }).setEditingSupport(
        new TableEditingSupport<TranspService>(viewer, errorHandler) {
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
  }
}
