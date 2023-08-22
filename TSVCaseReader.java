package hw3;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TSVCaseReader extends CaseReader{

	static int casesInvalid = 0;

	TSVCaseReader(String filename) {
		super(filename);
		// TODO Auto-generated constructor stub
	}

	@Override
	List<Case> readCases() throws DataException{
		// TODO Auto-generated method stub
		/*
		 * This functionality reads the user selected TSV File and parses it and creates case objects
		 * and returns the list of cases.
		 * 
		 * In addition to reading cases, this function also throws DataException to notify user of the 
		 * missing values in the TSV file. 
		 */
		List<Case> caseList = new ArrayList<Case>();
		StringBuilder sb = new StringBuilder();
		Scanner readFile;
		try {
			readFile = new Scanner(new File(super.filename));
			while(readFile.hasNextLine()) {
				sb.append(readFile.nextLine() + "\n");
			}
			String [] str = sb.toString().split("\n");
			for(String s : str) {
				String [] st = s.split("\t");

				if(st[0].trim().equalsIgnoreCase("") || st[1].trim().equalsIgnoreCase("") || st[2].trim().equalsIgnoreCase("") || st[3].trim().equalsIgnoreCase("")) {
					casesInvalid++;
				}

				Case c = new Case(st[0].trim(), st[1].trim(), st[2].trim(), st[3].trim(), st[4].trim(), st[5].trim(), st[6].trim());
				caseList.add(c);
			}
			try {
				if(casesInvalid > 0) {
					throw new DataException("File Missing Data");
				}
			}
			catch(DataException ex) {
				System.out.println("Missing Data in the TSV input File");
			}
		}
		catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return caseList;
	}


}
