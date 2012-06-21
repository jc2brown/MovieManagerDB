package ca.jc2brown.generic.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;

import ca.jc2brown.generic.dao.GenericDai;
import ca.jc2brown.generic.model.ModelEntity;

public class GenericTable extends Table {
	
	private static Logger log = Logger.getLogger( GenericTable.class.getName() );
	
	//protected abstract Map<String,String> getMap();
	
	private Map<String,TableColumn> columns;
	private Map<String,Integer> columnIndexes;

	private GenericDai<? extends ModelEntity> dao;


	public GenericTable(Composite parent, int style, String name, Set<String> columnNames) {
		super(parent, style);
		
		columns = new HashMap<String,TableColumn>();
		columnIndexes = new HashMap<String, Integer>();
		
		int i = 0;
		// Add the columns specified by map to the table
		for ( String columnName : columnNames ) {
			if ( columnName.equals("representative") ) {
				continue;
			}
			columnName = unCamelCase(columnName);
			log.debug("adding column " + columnName);
			TableColumn column = new TableColumn(this, SWT.NONE);
			column.setWidth(100);
			column.setMoveable(true);
			column.setText(columnName);
			columns.put(columnName, column);
			columnIndexes.put(columnName, i);
			i += 1;
		}

		setHeaderVisible(true);
	}
	
	static String unCamelCase(String s) {
			s = s.substring(0,1).toUpperCase() + s.substring(1);
		   return s.replaceAll(
		      String.format("%s|%s|%s",
		         "(?<=[A-Z])(?=[A-Z][a-z])",
		         "(?<=[^A-Z])(?=[A-Z])",
		         "(?<=[A-Za-z])(?=[^A-Za-z])"
		      ),
		      " "
		   );
		}
	
	
	
	
	public <T extends ModelEntity> TableItem setText(T entity ) {
		Map<String,String> fields = entity.toMap();
		TableItem item = new TableItem(this, SWT.NONE);
		for ( Entry<String,String> entry : fields.entrySet() ) {
			String columnName = unCamelCase(entry.getKey());
			log.debug("columnName=" + columnName);
			String fieldValue = entry.getValue();
			Integer columnIndex = columnIndexes.get(columnName);
			log.debug("columnIndex=" + columnIndex);
			log.debug("fieldValue=" + fieldValue);
			if ( columnIndex != null ) { 
				item.setText(columnIndex, fieldValue);
			}
		}
		return item;
	}
	
	public <T extends ModelEntity> void setDao(GenericDai<T> dao ) {
		this.dao = dao;
	}
	
	public <T extends ModelEntity> void updated() {
		List<? extends ModelEntity> entities = dao.findAll();
		for ( ModelEntity entity : entities ) {
			setText(entity);
		}
	}

	// Required to prevent SWT from complaints about subclassing
	protected void checkSubclass() {}

}
