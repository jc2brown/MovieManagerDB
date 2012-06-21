package ca.jc2brown.generic.ui;

import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class GenericTabItem extends TabItem {

	public GenericTabItem(TabFolder parent, int style, String name) {
		super(parent, style);
		
		setText(name);
		
	}

	// Required to prevent SWT from complaints about subclassing
	protected void checkSubclass() {}
}