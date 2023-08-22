/*
 * Name : Medha Praneeth Reddy Modulla
 * Andrew Id : mmodulla
 */
package hw3;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*
 * DataException inherits from RuntimeException and raises a Exception Dialog box 
 * to notify user of the data errors present in the TSV files. 
 * It also describes the kind of the error to the user.
 */

public class DataException extends RuntimeException{
	

	DataException(String message){
		Alert exceptionAlert = new Alert(AlertType.ERROR);
		exceptionAlert.setTitle("Data Error");
		//TSV Data has missing values 
		if(message.equalsIgnoreCase("File Missing Data")) {
			exceptionAlert.setContentText(TSVCaseReader.casesInvalid + " cases rejected.\n The file must have cases with\n tab seperated date, title, type and case number!");
			exceptionAlert.showAndWait();
			TSVCaseReader.casesInvalid = 0;
		}
		//User adds or modifies with incomplete data while adding a new Case
		else if(message.equalsIgnoreCase("Add Missing Data")) {
			exceptionAlert.setContentText("Case must have date, title, type and number");
			exceptionAlert.showAndWait();
		}
		//User adds or modifies with a duplicate case number
		else if(message.equalsIgnoreCase("Duplicate Case Number")) {
			exceptionAlert.setContentText("Duplicate case number");
			exceptionAlert.showAndWait();
		}
	}

}
