package ca.jc2brown.mmdb.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import ca.jc2brown.mmdb.MediaManagerDB;

public class GroupedProperties extends Properties {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger( GroupedProperties.class.getName() );

	// The path to the properties file for this map
	private String configLocation;
	
	// Indicates that the file specified by configLocation has been loaded intil the map
	private Boolean loaded = false;
	
	// Loading of the properties file can optionally be deferred until needed
	private Boolean lazy;
	
	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
		if ( lazy ) {
			log.debug("configLocation set/changed but not yet loaded");
			loaded = false;
		} else {
			load();
		}
	}
	
	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}
	
	
	public GroupedProperties() {
		super();
	}
	
	// Loads the properties file specified by configLocation
	private void load() {
		try {
			load(new FileReader(configLocation));
		} catch (FileNotFoundException e) {
			log.fatal("Cannot find properties file " + configLocation);
			MediaManagerDB.quit();
		} catch (IOException e) {
			log.fatal("Error reading properties file " + configLocation);
			MediaManagerDB.quit();
		}
		log.debug("Loaded properties file " + configLocation);
		loaded = true;
	}
	
	public String getProperty(String property) {
		if ( ! loaded ) {
			load();
		}
		return super.getProperty(property);
	}
	
	
	public List<String> getValueList(String property) {
		List<String> list = new ArrayList<String>();
		String line = getProperty(property);
		if ( line == null ) {
			log.warn(property + " not found in properties file");
			return list;
		}
		for (String s : line.split(" ")) {
			list.add(s);
		}
		return list;
	}



}
