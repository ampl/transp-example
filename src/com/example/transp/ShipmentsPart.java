
package com.example.transp;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;

/**
 * Shipments view part
 */
public class ShipmentsPart extends TableViewerPart {

  @Override
  protected void postConstruct(final TranspService service,
      ErrorHandler errorHandler) {
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
    addColumn("Shipment", SWT.RIGHT, new ColumnLabelProvider() {
      @Override
      public String getText(Object element) {
        int index = (int) element;
        return String.format("%.1f",
            service.shipments()[index / numMarkets][index % numMarkets]);
      }
    });

    Integer[] inputs = new Integer[service.plants().length * numMarkets];
    for (int i = 0; i < inputs.length; i++)
      inputs[i] = i;
    viewer.setContentProvider(new ArrayContentProvider());
    viewer.setInput(inputs);

    service.addShipmentsChangeListener(new ShipmentsChangeListener() {
      @Override
      public void shipmentsChanged() {
        viewer.refresh();
      }
    });
  }
}
