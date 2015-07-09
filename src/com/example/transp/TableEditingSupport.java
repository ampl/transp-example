package com.example.transp;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

public abstract class TableEditingSupport<T> extends EditingSupport {

  private CellEditor editor;
  private ErrorHandler errorHandler;

  public TableEditingSupport(TableViewer viewer, ErrorHandler errorHandler) {
    super(viewer);
    editor = new TextCellEditor(viewer.getTable());
    this.errorHandler = errorHandler;
  }

  @Override
  protected CellEditor getCellEditor(Object element) {
    return editor;
  }

  @Override
  protected boolean canEdit(Object element) {
    return true;
  }

  protected abstract double doGetValue(T element);

  protected abstract void doSetValue(T element, double value);

  @SuppressWarnings("unchecked")
  private T cast(Object element) {
    return (T) element;
  }

  @Override
  protected Object getValue(Object element) {
    return Double.toString(doGetValue(cast(element)));
  }

  @Override
  protected void setValue(Object element, Object value) {
    double dblValue;
    try {
      dblValue = Double.parseDouble((String) value);
    } catch (NumberFormatException e) {
      errorHandler.handle(String.format("%s is not a valid number", value));
      return;
    }
    if (dblValue < 0) {
      errorHandler.handle("Value cannot be negative");
      return;
    }
    doSetValue(cast(element), dblValue);
    getViewer().update(element, null);
  }

}
