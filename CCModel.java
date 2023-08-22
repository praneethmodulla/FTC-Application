/*
 * Name : Medha Praneeth Reddy Modulla
 * Andrew Id : mmodulla
 */
package hw3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class CCModel {
	ObservableList<Case> caseList = FXCollections.observableArrayList(); 			//a list of case objects
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();		//map with caseNumber as key and Case as value
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();	//map with each year as a key and a list of all cases dated in that year as value. 
	ObservableList<String> yearList = FXCollections.observableArrayList();			//list of years to populate the yearComboBox in ccView

	/** readCases() performs the following functions:
	 * It creates an instance of CaseReaderFactory, 
	 * invokes its createReader() method by passing the filename to it, 
	 * and invokes the caseReader's readCases() method. 
	 * The caseList returned by readCases() is sorted 
	 * in the order of caseDate for initial display in caseTableView. 
	 * Finally, it loads caseMap with cases in caseList. 
	 * This caseMap will be used to make sure that no duplicate cases are added to data
	 * @param filename
	 */
	void readCases(String filename) {
		//write your code here
		CaseReaderFactory c = new CaseReaderFactory();
		CaseReader cr = c.createCaseReader(filename);
		/*
		 * Invokes TSVCaseReader or CSVCaseReader based on the file selected 
		 * by the user. Based on the file, data is populated in caseList collection.
		 */
		if(cr instanceof TSVCaseReader) {
			caseList = FXCollections.observableArrayList(((TSVCaseReader)cr).readCases());
		}
		else {
			caseList = FXCollections.observableArrayList(((CSVCaseReader)cr).readCases());
		}
		Collections.sort(caseList);
		/*
		 * Populating caseMap based on the data populated in the caseList
		 */
		for(Case cs : caseList) {
			caseMap.put(cs.getCaseNumber(), cs);
		}
		
	}

	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */
	void buildYearMapAndList() {
		//write your code here
		/*
		 * Using StringBuilder to build yearList
		 */
		StringBuilder years = new StringBuilder();
		for(Case cs : caseList) {
			if(!years.toString().contains(cs.getCaseDate().substring(0,4))) {
				years.append(cs.getCaseDate().substring(0, 4) + "\n");
			}
		}
		yearList = FXCollections.observableArrayList(years.toString().split("\n"));
		/*
		 * Populating yearMap with year and the corresponding cases.
		 */
		for(Case cs : caseList) {
			List<Case> c = new ArrayList<>();
			if(yearMap.containsKey(cs.getCaseDate().substring(0, 4))) {
				c = yearMap.get(cs.getCaseDate().substring(0, 4));
				c.add(cs);
				yearMap.put(cs.getCaseDate().substring(0, 4), c);
			}
			else {
				c.add(cs);
				yearMap.put(cs.getCaseDate().substring(0, 4), c);
			}
		} 
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String year, String caseNumber) {
		//write your code here
		/*
		 * searches cases based on the input of title, caseType, year and caseNumber
		 * and returns the search results.
		 */
		List<Case> cseList = new ArrayList<>();
		if(title == null) {
			title = " ";
		}
		if(caseType == null) {
			caseType = " ";
		}
		if(year == null) {
			year = " ";
		}
		if(caseNumber == null) {
			caseNumber = " ";
		}
		for(Case cs : caseList) {
			StringBuilder csTest = new StringBuilder();
			csTest.append(cs.getCaseTitle().toLowerCase() + "\t" + cs.getCaseType().toLowerCase() + "\t" + cs.getCaseDate().substring(0, 4) + "\t" + cs.getCaseNumber());
			if(csTest.toString().toLowerCase().contains(title.toLowerCase()) && csTest.toString().toLowerCase().contains(caseType.toLowerCase()) && csTest.toString().contains(caseNumber) && cs.toString().contains(year)) {
				cseList.add(cs);
			}
		}
		return cseList;
	}
	
	boolean writeCases(String fileName) {
		String link;
		String category;
		String notes;
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))){
			for(Case c : caseList) {
				if(c.getCaseLink().trim().equalsIgnoreCase("")){
					link = " ";
				}
				else {
					link = c.getCaseLink();
				}
				
				if(c.getCaseCategory().trim().equalsIgnoreCase("")){
					category = " ";
				}
				else {
					category = c.getCaseCategory();
				}
				
				if(c.getCaseNotes().trim().equalsIgnoreCase("")){
					notes = " ";
				}
				else {
					notes = c.getCaseNotes();
				}
				bw.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\n", c.getCaseDate(), c.getCaseTitle(), c.getCaseType(), c.getCaseNumber(), link, category, notes));
			}
			return true;
		}
		catch(IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
