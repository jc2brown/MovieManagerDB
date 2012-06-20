package ca.jc2brown.mmdb.gui.ui.table;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;

import ca.jc2brown.mmdb.model.Actor;

public class ActorTable extends GenericTable<Actor> {
	
	
	private static Map<String,String> map;
	
	static {
		map = new HashMap<String,String>();
		map.put("First Name", "firstName");	
		map.put("Last Name", "lastName");	
	}

	public ActorTable(Composite arg0, int arg1) {
		super(arg0, arg1);
	}

	// Required to prevent SWT from complaints about subclassing
	protected void checkSubclass() {}

	@Override
	protected Map<String, String> getMap() {
		return map;
	}
}
