/*
 * Name : Medha Praneeth Reddy Modulla
 * Andrew Id : mmodulla
 */
package hw3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case>{
	//Declaring StringProperty for all the case attributes and setting 
	// the getter and setter functions of every property.
	//Constructor with all the attributes of the case is also defined
	private StringProperty caseDate = new SimpleStringProperty();
	private StringProperty caseTitle = new SimpleStringProperty();
	private StringProperty caseType = new SimpleStringProperty();
	private StringProperty caseNumber = new SimpleStringProperty();
	private StringProperty caseLink = new SimpleStringProperty();
	private StringProperty caseCategory = new SimpleStringProperty();
	private StringProperty caseNotes = new SimpleStringProperty();
	
	Case(String caseDate, String caseTitle, String caseType, String caseNumber, String caseLink, String caseCategory, String caseNotes){
		this.caseDate.set(caseDate);
		this.caseTitle.set(caseTitle);
		this.caseType.set(caseType);
		this.caseNumber.set(caseNumber);
		this.caseLink.set(caseLink);
		this.caseCategory.set(caseCategory);
		this.caseNotes.set(caseNotes);
	}
	
	public String getCaseDate() {
		return this.caseDate.get();
		
	}
	
	public String getCaseTitle() {
		return this.caseTitle.get();
	}
	
	public String getCaseType() {
		return this.caseType.get();
	}
	
	public String getCaseNumber() {
		return this.caseNumber.get();
	}
	
	public String getCaseLink() {
		return this.caseLink.get();
	}
	
	public String getCaseCategory() {
		return this.caseCategory.get();
	}
	
	public String getCaseNotes() {
		return this.caseNotes.get();
	}
	
	public void setCaseDate(String date) {
		this.caseDate.set(date);
	}
	
	public void setCaseTitle(String title) {
		this.caseTitle.set(title);
	}
	
	public void setCaseType(String type) {
		this.caseType.set(type);
	}
	
	public void setCaseNumber(String number) {
		this.caseDate.set(number);
	}
	
	public void setCaseLink(String link) {
		this.caseLink.set(link);
	}
	
	public void setCaseCategory(String category) {
		this.caseCategory.set(category);
	}
	
	public void setCaseNotes(String notes) {
		this.caseNotes.set(notes);
	}

	@Override
	public int compareTo(Case o) {
		// TODO Auto-generated method stub
		return -this.getCaseDate().compareTo(o.getCaseDate());
	}
	
	@Override
	public String toString() {
		return this.getCaseDate() + "\t" + this.getCaseTitle() + "\t" + this.getCaseType() + "\t" + this.getCaseNumber() + "\t" + this.getCaseCategory() + "\t" + this.getCaseLink() + "\t" + this.getCaseNotes(); 
	}
}
