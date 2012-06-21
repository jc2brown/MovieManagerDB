package ca.jc2brown.mmdb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import ca.jc2brown.generic.dao.GenericDai;
import ca.jc2brown.generic.dao.GenericDao;
import ca.jc2brown.generic.model.ModelEntity;
import ca.jc2brown.generic.model.ModelConfigurer;
import ca.jc2brown.generic.ui.GenericTabItem;
import ca.jc2brown.generic.ui.GenericTable;
import ca.jc2brown.mmdb.gui.ApplicationWindow;
import ca.jc2brown.mmdb.model.BaseEntity;
import ca.jc2brown.mmdb.model.Movie;
import ca.jc2brown.mmdb.model.MovieFile;
import ca.jc2brown.mmdb.utils.GroupedProperties;

@Service
public class MediaManagerDB {
	
	// Log4j handle
	private static Logger log = Logger.getLogger( MediaManagerDB.class.getName() );
	
	// ////////////////////////////////////////////////////////////////////
	// Instance control
	//
	
	// Application instance
	private static MediaManagerDB mmdb;

	public void setMain(MediaManagerDB main) {
		MediaManagerDB.mmdb = main;
	}
	
	// Application context
	private static ApplicationContext context;
	
	// Program entry: creates and runs an instance of the application
	public static void main(String[] args) {
		log.info("Starting application..." );
		context = new ClassPathXmlApplicationContext("mmdb-applicationContext.xml");
		mmdb.run();
		log.info("Done." );
	}
 
	// Immediate, ungraceful program termination with no cleanup
  	public static void bail(String error) {
  		log.fatal("Terminating application: " + error);
  		log.debug("Use quit() if possible");
  		log.fatal("No cleanup has been performed. Some data may be lost.");
  		System.exit(-1);
  	}

  	// Graceful termination with cleanup where possible
  	public static void quit() {
  		log.debug("Shutdown requested");
  		mmdb.shutdown();
  		((ClassPathXmlApplicationContext) context).close(); 
  	}
	

	// ////////////////////////////////////////////////////////////////////
	// Application 
	//

	@SuppressWarnings("unused")
	private GroupedProperties mmdbProperties;
	private ApplicationWindow window;
	
	private SessionFactory sessionFactory;
	
	
	@Autowired
	public void setMmdbProperties(GroupedProperties mmdbProperties) {
		this.mmdbProperties = mmdbProperties;
	}

	@Autowired
	public void setWindow(ApplicationWindow window) {
		this.window = window;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	// Application initilization
  	public MediaManagerDB() {
  		log.info("Initializing...");
    }
    
  	@PostConstruct
  	public void init() {
  		String basePackage = BaseEntity.class.getPackage().getName();
		try {
			ModelConfigurer.configure(basePackage);
		} catch (Exception e) {
			e.printStackTrace();
			bail(e.toString());
		}
  	}
  	
  	@PreDestroy
  	public void destroy() {
  		log.debug("destroy()");
  	}
  	
  	
    // Begin regular execution
	public void run() {
  		
  		GenericDao.setSessionFactoryHolder(sessionFactory);
	
		TabFolder tabFolder = new TabFolder(window, SWT.NONE);
		GridData gd_tabFolder_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tabFolder_1.widthHint = 300;
		tabFolder.setLayoutData(gd_tabFolder_1);
  		
    	
		MovieFile movieFile1 = new MovieFile();
    	movieFile1.setFilename("TheGood.mpg");
    	movieFile1.setSequence(0L);

    	
    	Movie movie1 = new Movie();
    	movie1.setTitle("The Good");
    	movie1.addMovieFile(movieFile1);
    	movie1.addGenre("Comedy");
    	movie1.addGenre("Action");
    	    	
    	GenericDao.smakePersistent(movie1);

    	movie1.addGenre("Romance");
    	   	
    	
		MovieFile movieFile2 = new MovieFile();
		movieFile2.setFilename("TheBad.mpg");
		movieFile2.setSequence(0L);

    	Movie movie2 = new Movie();
    	movie2.setTitle("The Bad");
    	movie2.addMovieFile(movieFile2);
    	movie2.addGenre("Comedy");
    	movie2.addGenre("Action");

    	movie2 = GenericDao.smakePersistent(movie2);
    	
    	for ( int i = 0; i < 1000; i++ ) {
    		MovieFile movieFile = new MovieFile();
    		movieFile.setFilename("TheBad" + i + ".mpg");
    		movieFile.setSequence(0L);
        	Movie movie = new Movie();
        	movie.setTitle("The Bad " + i);
        	movie.addMovieFile(movieFile);
        	movie.addGenre("Genre " + i / 10);
        	GenericDao.smakePersistent(movie);
    	}
    	
    
		for ( Class<? extends ModelEntity> clazz : ModelConfigurer.ModelEntityes ) {
			GenericTabItem tab = new GenericTabItem(tabFolder, SWT.NONE, clazz.getSimpleName());
			GenericTable table = new GenericTable(tabFolder, SWT.BORDER | SWT.FULL_SELECTION, clazz.getSimpleName(), ModelEntity.getMap(clazz).keySet() );
			tab.setControl(table);
			GenericDai<? extends ModelEntity> dao = GenericDao.getDao(clazz);
			table.setDao(dao);
			table.updated();
		}
		
  		window.open();
  	  		
		MediaManagerDB.quit();	
  	}
	
	
	
  	
  	private void shutdown() {
  		log.info("Starting shutdown...");
		Thread shutdown = new Thread(new ShutdownThread());
		shutdown.run();
  	}
  	

  	
  	
  	class ShutdownThread implements Runnable {
  		public void run() {
  			log.info("Starting cleanup...");
  			try {
  				Thread.sleep(1);
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}
  			printReport();
  	  		log.info("Cleanup complete.");
  		}
  		
  		public void printReport() {
  			log.info("REPORT.");
  		}
  	}    
}