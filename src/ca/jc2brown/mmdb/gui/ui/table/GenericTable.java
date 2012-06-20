package ca.jc2brown.mmdb.gui.ui.table;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;

import ca.jc2brown.mmdb.model.BaseEntity;

public abstract class GenericTable<T extends BaseEntity> extends Table {
	
	protected abstract Map<String,String> getMap();
	
	private Map<String,TableColumn> columns;
	private Map<String,Integer> columnIndexes;


	public GenericTable(Composite parent, int style) {
		super(parent, style);
		
		columns = new HashMap<String,TableColumn>();
		columnIndexes = new HashMap<String, Integer>();
		
		int i = 0;
		// Add the columns specified by map to the table
		for ( String columnName : getMap().keySet() ) {
			TableColumn column = new TableColumn(this, SWT.NONE);
			column.setWidth(100);
			column.setText(columnName);
			columns.put(columnName, column);
			columnIndexes.put(columnName, i);
			i += 1;
		}
	}
	
	
	protected void setText(T entity) {
		Map<String,String> fields = entity.toMap();
		TableItem item = new TableItem(this, SWT.NONE);
		for ( Entry<String,String> entry : getMap().entrySet() ) {
			String columnName = entry.getKey();
			String fieldName = entry.getValue();
			String fieldValue = fields.get(fieldName);
			Integer columnIndex = columnIndexes.get(columnName);
			item.setText(columnIndex, fieldValue);
		}
	}

	// Required to prevent SWT from complaints about subclassing
	protected void checkSubclass() {}
}
