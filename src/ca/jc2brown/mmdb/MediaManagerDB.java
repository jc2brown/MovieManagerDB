package ca.jc2brown.mmdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.jc2brown.mmdb.model.Actor;
import ca.jc2brown.mmdb.model.Movie;
import ca.jc2brown.mmdb.utils.GroupedProperties;
import ca.jc2brown.mmdb.utils.Utils;


public class MediaManagerDB {
	
	
	// ////////////////////////////////////////////////////////////////////
	// Instance control
	//
	
	// Application instance
	private static MediaManagerDB main;
	

	// Program entry: creates and runs an instance of the application
	public static void main(String[] args) {
		log.info("Starting..." );
		main = new MediaManagerDB();
		main.run();
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
  		main.shutdown();
  	}
	

	// ////////////////////////////////////////////////////////////////////
	// Application 
	//

	
	// Log4j handle
	private static Logger log = Logger.getLogger( MediaManagerDB.class.getName() );
	
	// Healthy program flag
	// Also prevents multiple shutdown threads
	private boolean alive = true;
	
	
	// Application initilization
  	public MediaManagerDB() {
  		
  		log.info("Initializing...");
  		
  		log.debug("Loading configuration files...");
  		GroupedProperties mmdbProperties = Utils.getProperties("config/mmdb.properties");
  		
  		log.debug("Loading Spring application context");
    	ClassPathXmlApplicationContext factory = new ClassPathXmlApplicationContext("mmdb-applicationContext.xml");

    	Actor actor = new Actor();
    	actor.setFullName("Chris Brown");
    	actor.setFirstName("Chris");
    	actor.setLastName("Brown");
    	actor.setMovies(new HashSet<Movie>());
    	
    	
    	SessionFactory sessionFactory = (SessionFactory) factory.getBean("sessionFactory");
    	Session session = sessionFactory.openSession();
    	Transaction tx = session.beginTransaction();
    	tx.begin();
    	session.save(actor);
    	tx.commit();

    	
    	System.out.println(actor);
    	
    	actor.setFullName("Stephen Harper");
    	actor.setFirstName("Stephen");
    	actor.setLastName("Harper");
    	System.out.println(actor);

    	session = sessionFactory.openSession();
    	tx = session.beginTransaction();
    	tx.begin();
    	actor = (Actor)session.get(Actor.class, actor.getId());
    	tx.commit();

    	System.out.println(actor);
    	
    }
    
    
  	public void run() {
  		System.out.println("\nType :q followed by Enter to quit the program.");
  		BufferedReader in = new BufferedReader( new InputStreamReader (System.in) );
  		while ( alive ) {
  			String line = null;
  			try {
  				line = in.readLine();
  			} catch (IOException e) {
  				e.printStackTrace();
  			}
  			if (alive && ":q".equalsIgnoreCase(line.trim()) ) {
  				log.debug("User quit");
  				MediaManagerDB.quit();	
  			}
  		}
  		try {
  			in.close();
  		} catch (IOException e) {
  			e.printStackTrace();
  		}
  		
		MediaManagerDB.quit();	
  	}
  	
  	private void shutdown() {
  		if ( alive ) {
  	  		log.info("Starting shutdown...");
  			alive = false;
  			Thread shutdown = new Thread(new ShutdownThread());
  			shutdown.run();
  		}
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