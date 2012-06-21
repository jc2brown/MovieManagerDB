package ca.jc2brown.mmdb.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.jc2brown.mmdb.utils.GroupedProperties;

// A repository of FileFilter objects that can be passed to File.listFiles()
@Service
public class FileFilters extends HashMap<String, FileFilter> {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger( FileFilters.class.getName() );
	
	// We're using the singleton pattern. Static methods act on this instance.
	private static FileFilters instance = null;

	private GroupedProperties mmdbProperties;

	@Autowired
	public void setMmdbProperties(GroupedProperties mmdbProperties) {
		this.mmdbProperties = mmdbProperties;
	}
	
	// Filter instances
	public FileFilter directoryFilter;
	public FileFilter videoFilter;
	public FileFilter metadataFilter;
	public FileFilter tempfileFilter;

	// Lists of supported filetypes
	private List<String> videotypes;
	private List<String> metatypes; 
	private List<String> temptypes;
	
	
	public FileFilters() {}

	@PostConstruct
	@SuppressWarnings("unused")
	private void initialize() {
		this.videotypes = mmdbProperties.getValueList("videotypes");
		this.metatypes = new ArrayList<String>();
		this.metatypes.add(mmdbProperties.getProperty("metatype"));
		this.temptypes = new ArrayList<String>();
		this.temptypes.add(mmdbProperties.getProperty("temptype"));
		directoryFilter = new DirectoryFilter();
		videoFilter = new FileTypeFilter(this.videotypes);
		metadataFilter = new FileTypeFilter(this.metatypes);
		tempfileFilter = new FileTypeFilter(this.temptypes);
		put("directories", directoryFilter);
		put("video", videoFilter);
		put("meta", metadataFilter);
		put("temp", tempfileFilter);
		instance = this;
		log.debug("Filters initialized");
	}
	
	
	
	// Produces the FileFilter associated with the given key, 
	// or null if non-existent per java.util.Map
	public static FileFilter getFilterFor(String key) {
		return instance.get(key);
	}
	
	// This produces the unique filetype associated with the given key,
	// or null if non-unique or non-existent 
	public static String getFiletypeFor(String key) {
		FileTypeFilter filter = (FileTypeFilter) instance.get(key);
		if ( filter.filetypes.size() != 1 ) {
			return null;
		}
		return filter.filetypes.get(0);
	}
	
	// Allow only directories
	class DirectoryFilter implements FileFilter {
		
		public DirectoryFilter() {}

		public boolean accept(File path) {
			return path.isDirectory();
		}
	}

	// Allow only files of the type(s) specified by filetypes list
	class FileTypeFilter implements FileFilter {
		
		private List<String> filetypes;
		
		public FileTypeFilter(List<String> filetypes) {
			this.filetypes = filetypes;
		}
		
		public boolean accept(File path) {
			if ( path.isFile() ) {
				String filename = path.getPath();
				for ( String filetype : filetypes ) {
					if ( filename.endsWith(filetype) ) {
						return true;
					}
				}
			}
			return false;
		}
	}
}

	

