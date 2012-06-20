package ca.jc2brown.mmdb.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class Metadata extends HashMap<String, String> {

	private static Logger log = Logger.getLogger( Metadata.class.getName() );
	
	private static String invalidChars = "[\\\\/*?\"<>|]";
	
	private static final long serialVersionUID = 1L;
	public static List<String> commentKeyList;
	public static List<String> multiwords;
	
	private MMFile file;	
	
	// Returns the substring of str that matches the pattern specified by regex 
	public static String extract(String str, String regex) {
		if ( str == null ) return null;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if ( matcher.find() ) {
			log.debug("extract( " + str + ", " + regex + " ) -> " + matcher.group());
			return matcher.group();			
		}
		log.debug("extract( " + str + ", " + regex + " ) -> " + null);
		return null;
	}
	
	public static String getMultipart(String title) {
		for ( String keyword : multiwords ) {
			keyword = keyword.toLowerCase();
			title = title.toLowerCase();
			String regex = keyword + ".?\\d+";
			String match = extract(title, regex);
			if ( match != null ) { 
				log.debug("getMultipart(" + title + ") -> " + match);
				return match;
			}
		}
		log.debug("getMultipart(" + title + ") -> " + null);
		return null;
	}
	
	public static String getMultipartNum(String title) {
		String partX = getMultipart(title);	// Get ... part X ...
		String X = extract(partX, "\\d+");		// Get X
		log.debug("getMultipartNum(" + title + ") -> " + partX + " -> " + X);
		return X;
	}
	
	
	
	protected Metadata(MMFile file, String origin) {
		super();
		if ( file == null || ! FileFilters.getFiletypeFor("meta").equalsIgnoreCase(file.getFileType()) ) {
			return;
		}
		this.file = file;
		BufferedReader reader;		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ( (line = reader.readLine() ) != null ) {
				int delindex = line.indexOf("=");
				if (delindex > 0) {
					String key = line.substring(0, delindex).trim();
					String value = line.substring(delindex).trim();
					put(key, value);		
				}
			}
			reader.close();
			if ( origin != null ) {
				put("origin", origin);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	// Strip any characters that are not permitted in filenames
	private static String legalize(String s) {
		s = s.replaceAll("\\:", " - ");
		s = s.replaceAll(invalidChars, " ");
		s = s.replaceAll("  ", " ");
		return s;
	}
	

	
	public String put(String key, String value) {
		while ( value.startsWith("=") ) {
			value = value.replace("=", "");
		}
		if ( "title".equalsIgnoreCase(key) ) {
			value = legalize(value);
		}
		return super.put(key, value);
	}

	public boolean isValid() {
		String response = get("Response");
		return ! response.startsWith("Parse Error");
	}
	
	
	public MMFile getVideoFile() {
		String path = get("localpath");
		if ( path == null ) {
			log.debug("localpath not found");
			return null;
		}
		MMFile videoFile = new MMFile(path);
		if ( ! videoFile.exists() ) {
			return null;
		}
		return videoFile;
	}
	
	public String getFileTitle() {
		String part = get("part");
		String title = get("Title");
		String result = title;
		if ( title == null ) 
			return null;
		if ( part != null )
			result = result + " " + multiwords.get(0) + " " + part;
		return result;
	}
	
	public String getQueryTitle() {
		String title = get("Title");
		String result = title;
		if ( title == null ) 
			title = new MMFile(get("localpath")).getBasename();
		title = title.replaceAll("\\([1-9]\\)", "");
		return result;
		
	}
	
	public List<String> getValueList(String key) {
		List<String> list = new ArrayList<String>();
		String line = get(key);
		if ( line == null ) {
			log.warn(key + " not found");
			return list;
		}
		for (String s : line.split(", ")) {
			list.add(s);
		}
		return list;
	}
	
	
	public void write() {
		
	
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(file));
		
			log.debug("Writing metadata");
			
			out.write(";FFMETADATA1\n");
			
			/*
			for (String field : values()) {
				String value = get(field);
				if ( value != null ) {
					out.write(field + "=" + value + "\n");
				}
			}*/
			
			if ( get("origin") == null && get("localhost") != null ) {
				put("origin", get("localhost"));
			}
			
			
			
			for (java.util.Map.Entry<String, String> entry : entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if ( value != null ) {
					out.write(key + "=" + value + "\n");
				}
			}
			
			StringBuffer sb = new StringBuffer();
			for (String key : commentKeyList) {
				String value = get(key);
				if ( value != null ) {
					sb.append(key + ": " + value + "   ");
				}
			}
			out.write("Comment=" + sb.toString() + "\n");
			out.write("Comments=" + sb.toString() + "\n");
			/*
			String part = get("part");
			if ( part != null ) {
				out.write("part=" + part + "\n");
			}*/
			out.close();
			

		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

	public void setVideoFile(MMFile videoFile) {
		String title = videoFile.getBasename();
		//title = title.replaceAll("\\([1-9]\\)", "");	// remove duplicate
		String part = getMultipartNum(title);
		if ( part != null ){
			put("part", part);
		}
		String path = videoFile.getPath();
		//path = path.replaceAll("\\([1-9]\\)", "");	// remove duplicate
		if ( get("origin") == null ) {
			put("origin", path);
		}
		put("localpath", path);
	}
	
	
	public void writeVideoFile(MMFile videoFile) {
		setVideoFile(videoFile);
		write();
	}
	
}
