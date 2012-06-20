package ca.jc2brown.mmdb.file;

import java.io.File;
import java.util.Comparator;

 	
/**
 * The FileComparator is used by the dirsToVisit P-Queue.
 * It uses depth (i.e. number of parent directories) as the determinant
 */

public class FileComparator implements Comparator<File> {
	
	private PathComparator comparator;
	
	public FileComparator() {
		comparator = new PathComparator();
	}
	
	public int compare(File f1, File f2) {
		String p1 = f1.getPath();
		String p2 = f2.getPath();
		return comparator.compare(p1, p2);
	}
}