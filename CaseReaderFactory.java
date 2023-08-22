/*
 * Name : Medha Praneeth Reddy Modulla
 * Andrew Id : mmodulla
 */
package hw3;

public class CaseReaderFactory {
	
	/*
	 * Returns a caseReader object based on the user selected csv or tsv file
	 */
	
	public CaseReader createCaseReader(String filename) {
		int i = filename.lastIndexOf('.');
		String tsv = "tsv";
		if(tsv.equalsIgnoreCase(filename.substring(i+1))) {
			return new TSVCaseReader(filename);
		}
		else {
			return new CSVCaseReader(filename);
		}
	}

}
