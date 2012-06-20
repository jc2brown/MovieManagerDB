package ca.jc2brown.mmdb.gui.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.springframework.beans.factory.annotation.Autowired;

import ca.jc2brown.mmdb.gui.ui.table.ActorTable;
import ca.jc2brown.mmdb.utils.GroupedProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;

public class ApplicationWindow extends Shell {

	private GroupedProperties mmdbProperties;
	private Table table;
	
	@Autowired
	public void setMmdbProperties(GroupedProperties mmdbProperties) {
		this.mmdbProperties = mmdbProperties;
	}
	
	public ApplicationWindow() {
		setSize(747, 420);
		setLayout(new GridLayout(1, false));
		
		new ActorTable(this, SWT.BORDER | SWT.FULL_SELECTION);
		
		/*Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmClose = new MenuItem(menu_1, SWT.NONE);
		mntmClose.setText("Close");
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");
		
		Menu menu_2 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_2);
		
		MenuItem mntmAbout = new MenuItem(menu_2, SWT.NONE);
		mntmAbout.setText("About");
		
		TabFolder tabFolder_1 = new TabFolder(this, SWT.NONE);
		GridData gd_tabFolder_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tabFolder_1.widthHint = 119;
		tabFolder_1.setLayoutData(gd_tabFolder_1);
		
		TabItem tbtmBrowse = new TabItem(tabFolder_1, SWT.NONE);
		tbtmBrowse.setText("Browse");
		
		TabFolder tabFolder = new TabFolder(tabFolder_1, SWT.NONE);
		tbtmBrowse.setControl(tabFolder);
		
		TabItem tbtmMovies = new TabItem(tabFolder, SWT.NONE);
		tbtmMovies.setText("Movies");
		
		table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbtmMovies.setControl(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnTitle = new TableColumn(table, SWT.NONE);
		tblclmnTitle.setWidth(100);
		tblclmnTitle.setText("Title");
		
		TableColumn tblclmnActors = new TableColumn(table, SWT.NONE);
		tblclmnActors.setWidth(100);
		tblclmnActors.setText("Movie File");
		
		TabItem tbtmActors = new TabItem(tabFolder, SWT.NONE);
		tbtmActors.setText("Actors");
		
		TabItem tbtmDirectors = new TabItem(tabFolder, SWT.NONE);
		tbtmDirectors.setText("Directors");
		
		TabItem tbtmWriters = new TabItem(tabFolder, SWT.NONE);
		tbtmWriters.setText("Writers");
		
		TabItem tbtmGenres = new TabItem(tabFolder, SWT.NONE);
		tbtmGenres.setText("Genres");
		
		TabItem tbtmRatings = new TabItem(tabFolder, SWT.NONE);
		tbtmRatings.setText("Ratings");
		
		TabItem tbtmFiles = new TabItem(tabFolder, SWT.NONE);
		tbtmFiles.setText("Files");
		
		TabItem tbtmSearch = new TabItem(tabFolder_1, SWT.NONE);
		tbtmSearch.setText("Search");
		
		Composite composite = new Composite(tabFolder_1, SWT.NONE);
		tbtmSearch.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		ToolItem tltmClearParameter = new ToolItem(toolBar, SWT.NONE);
		tltmClearParameter.setText("Clear Parameter");
		
		ToolItem tltmAddParameter = new ToolItem(toolBar, SWT.NONE);
		tltmAddParameter.setText("Add Parameter");
		
		TabFolder tabFolder_2 = new TabFolder(composite, SWT.NONE);
		GridData gd_tabFolder_2 = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_tabFolder_2.widthHint = 671;
		gd_tabFolder_2.heightHint = 275;
		tabFolder_2.setLayoutData(gd_tabFolder_2);
		
		TabItem tbtmNewParameter = new TabItem(tabFolder_2, SWT.NONE);
		tbtmNewParameter.setText("New Parameter");
		
		Composite composite_1 = new Composite(tabFolder_2, SWT.NONE);
		tbtmNewParameter.setControl(composite_1);
		composite_1.setLayout(new GridLayout(2, false));
		
		Combo combo = new Combo(composite_1, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		Tree tree = new Tree(composite_1, SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeItem trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem.setText("New TreeItem");
		
		TreeItem trtmNewTreeitem_1 = new TreeItem(trtmNewTreeitem, SWT.NONE);
		trtmNewTreeitem_1.setText("New TreeItem");
		trtmNewTreeitem.setExpanded(true);*/
	}
	
	
	public void open() {
        final Display display = Display.getDefault();
        createContents();
        super.open();
        layout();
        while ( ! isDisposed() ) {
            if ( ! display.readAndDispatch() ) {
                display.sleep();
            }
        }
    }
	
    protected void createContents() {
    	setSize(450, 300);
    	setText(mmdbProperties.getProperty("app.name"));
    }
	

	// Required to prevent SWT from complaints about subclassing
	protected void checkSubclass() {}
}
