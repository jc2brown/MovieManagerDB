package ca.jc2brown.mmdb;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import ca.jc2brown.framework.mapping.MappingConfigurer;
import ca.jc2brown.mmdb.dao.ActorDai;
import ca.jc2brown.mmdb.gui.ui.ApplicationWindow;
import ca.jc2brown.mmdb.model.Actor;
import ca.jc2brown.mmdb.model.BaseEntity;
import ca.jc2brown.mmdb.model.Movie;
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

	private GroupedProperties mmdbProperties;
	private ApplicationWindow window;
	
	private ActorDai actorDao;
	
	@Autowired
	public void setActorDao(ActorDai actorDao) {
		this.actorDao = actorDao;
	}
	
	
	@Autowired
	public void setMmdbProperties(GroupedProperties mmdbProperties) {
		this.mmdbProperties = mmdbProperties;
	}

	@Autowired
	public void setWindow(ApplicationWindow window) {
		this.window = window;
	}

	// Application initilization
  	public MediaManagerDB() {
  		log.info("Initializing...");
    }
    
  	@PostConstruct
  	public void init() {
  		window.open();
  	}
  	
  	@PreDestroy
  	public void destroy() {
  		log.debug("destroy()");
  	}
  	
  	

  	
  	public void test() {
  		Actor actor = new Actor();
  		actor.setId(123L);
    	actor.setFullName("Chris Brown");
    	actor.setFirstName("Chris");
    	actor.setLastName("Brown");
    	Set<Movie> movies = new HashSet<Movie>();
    	Movie movie = new Movie();
    	movie.setTitle("The Good");
    	movies.add( movie );
    	actor.setMovies(movies);
    	
    	actor = actorDao.makePersistent(actor);
    	System.out.println(actor);
    	System.out.println(movie);
    	

    	actorDao.makeTransient(actor);
    	actor.setFullName("Stephen Harper");
    	actor.setFirstName("Stephen");
    	actor.setLastName("Harper");
    	movie.setTitle("The Bad");
    	System.out.println(actor);
    	

    	for (Actor a : actorDao.findAll() ) {
    		System.err.println(a);
    	}
    	
    	
    	actor = actorDao.findById(2L, false);
    	System.out.println(actor);
  	}
  	
  	
    // Begin regular execution
  	public void run() {
  		
  		String basePackage = BaseEntity.class.getPackage().getName();
  		MappingConfigurer.configure(basePackage);
  		
  		Actor actor = new Actor();
  		actor.setId(123L);
    	actor.setFullName("Chris Brown");
    	actor.setFirstName("Chris");
    	actor.setLastName("Brown");
    	Set<Movie> movies = new HashSet<Movie>();
    	Movie movie = new Movie();
    	movie.setTitle("The Good");
    	movies.add( movie );
    	actor.setMovies(movies);
  		    	
  		System.out.println( movie );
  		System.out.println( actor );
  		
  		
  		/*
  		TableFieldParser tfp = new TableFieldParser();
  		tfp.parse(Actor.class);
  		test();*/
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