/*
 * Name : Medha Praneeth Reddy Modulla
 * Andrew Id : mmodulla
 */
package hw3;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CyberCop extends Application{

	public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
	public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app
	

	CCView ccView = new CCView();
	CCModel ccModel = new CCModel();

	CaseView caseView; //UI for Add/Modify/Delete menu option

	GridPane cyberCopRoot;
	Stage stage;

	static Case currentCase; //points to the case selected in TableView.

	public static void main(String[] args) {
		launch(args);
	}

	/** start the application and show the opening scene */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle("Cyber Cop");
		cyberCopRoot = ccView.setupScreen();  
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();
	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields 
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {
		//write your code here
		//Binding ccView Buttons to EventHandlers
		ccView.openFileMenuItem.setOnAction(new OpenFileMenuItemHandler());
		ccView.closeFileMenuItem.setDisable(true);
		ccView.closeFileMenuItem.setOnAction(new CloseFileMenuItemHandler());
		ccView.exitMenuItem.setOnAction(new ExitMenuHandler());
		ccView.saveFileMenuItem.setOnAction(new SaveMenuItemHandler());
		ccView.saveFileMenuItem.setDisable(true);
		ccView.caseCountChartMenuItem.setOnAction(new CaseCountChartMenuItemHandler());
		ccView.caseCountChartMenuItem.setDisable(true);
		
		
		ccView.searchButton.setDisable(true);
		ccView.clearButton.setDisable(true);
		ccView.titleTextField.setDisable(true);
		ccView.caseTypeTextField.setDisable(true);
		ccView.caseNumberTextField.setDisable(true);
		ccView.yearComboBox.setDisable(true);
		
		ccView.addCaseMenuItem.setDisable(true);
		ccView.modifyCaseMenuItem.setDisable(true);
		ccView.deleteCaseMenuItem.setDisable(true);
		//Binding Case Menu Items to their respective Event Handlers
		ccView.addCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.modifyCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.deleteCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		
		
		ccView.clearButton.setOnAction(new ClearButtonHandler());
		ccView.searchButton.setOnAction(new SearchButtonHandler());
		
		//Select a particular case selected by the user in the Case Table View
		ccView.caseTableView.getSelectionModel().selectedItemProperty().addListener((obsValue, oldVal, newVal)->{
			if(newVal!=null) {
				currentCase = newVal;
				
				ccView.titleTextField.setText(currentCase.getCaseTitle());
				ccView.caseTypeTextField.setText(currentCase.getCaseType());
				ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
				ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
				ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0, 4));
				
				if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
					URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
					ccView.webEngine.load(currentCase.getCaseLink());
				} else {
					URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				}
			}
		});
		//The following is some helper code to display web-pages. It is commented out to start with
		//Uncomment it to plug it in your code as needed.  

//		if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
//			URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
//			if (url != null) ccView.webEngine.load(url.toExternalForm());
//		} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
//			ccView.webEngine.load(currentCase.getCaseLink());
//		} else {
//			URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
//			if (url != null) ccView.webEngine.load(url.toExternalForm());
//		}
	}
	
	//Event Handler for the Open File Menu Item
	private class OpenFileMenuItemHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//Open Dialog Box to select the file
			FileChooser fileSelect = new FileChooser();
			fileSelect.setTitle("Select a File");
			fileSelect.setInitialDirectory(new File(DEFAULT_PATH));
			File fl = fileSelect.showOpenDialog(stage);;
			if(fl!=null) {
				ccView.isFileOpen.setValue(true);
				
				ccView.addCaseMenuItem.setDisable(false);
				ccView.modifyCaseMenuItem.setDisable(false);
				ccView.deleteCaseMenuItem.setDisable(false);
				ccView.saveFileMenuItem.setDisable(false);
				
				ccView.searchButton.setDisable(false);
				ccView.clearButton.setDisable(false);
				ccView.titleTextField.setDisable(false);
				ccView.caseTypeTextField.setDisable(false);
				ccView.caseNumberTextField.setDisable(false);
				ccView.yearComboBox.setDisable(false);
				
				ccView.caseCountChartMenuItem.setDisable(false);
				
				stage.setTitle("Cyber Cop" + fl.getName());
				
				ccModel.readCases(fl.getAbsolutePath());
				ccModel.buildYearMapAndList();
				
				ccView.messageLabel.setText(ccModel.caseList.size() + " cases");
				Collections.sort(ccModel.yearList);
				Collections.sort(ccModel.caseList);
				//Get the current Case after loading the file.
				currentCase = ccModel.caseList.get(0);
				
				ccView.titleTextField.setText(currentCase.getCaseTitle());
				ccView.caseTypeTextField.setText(currentCase.getCaseType());
				ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
				ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
				ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0, 4));
				ccView.yearComboBox.setItems(ccModel.yearList);
				
				ccView.caseTableView.setItems(ccModel.caseList);
				
				if(ccView.closeFileMenuItem.isDisable()) {
					ccView.closeFileMenuItem.setDisable(false);
				}
				ccView.openFileMenuItem.setDisable(true);
				
			}
			
			//Display the Web View of the case if the link is present
			if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
				URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
				if (url != null) ccView.webEngine.load(url.toExternalForm());
			} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
				ccView.webEngine.load(currentCase.getCaseLink());
			} else {
				URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
				if (url != null) ccView.webEngine.load(url.toExternalForm());
			}
		}
		
	}
	//Event Handler for the Close File Menu Item
	private class CloseFileMenuItemHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			stage.setTitle("Cyber Cop");
			ccView.isFileOpen.setValue(false);
			
			currentCase = null;
			
			
			ccView.messageLabel.setText("");
			
			ccView.addCaseMenuItem.setDisable(true);
			ccView.modifyCaseMenuItem.setDisable(true);
			ccView.deleteCaseMenuItem.setDisable(true);
			
			ccView.titleTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.caseNotesTextArea.clear();
			ccView.yearComboBox.setValue("");
			ccView.yearComboBox.setItems(null);
			
			ccView.caseTableView.setItems(null);
			
			if(ccView.openFileMenuItem.isDisable()) {
				ccView.openFileMenuItem.setDisable(false);
			}
			ccView.closeFileMenuItem.setDisable(true);
			ccView.saveFileMenuItem.setDisable(true);
			ccView.searchButton.setDisable(true);
			ccView.clearButton.setDisable(true);
			ccView.titleTextField.setDisable(true);
			ccView.caseTypeTextField.setDisable(true);
			ccView.caseNumberTextField.setDisable(true);
			ccView.yearComboBox.setDisable(true);
			
			ccView.caseCountChartMenuItem.setDisable(true);
			
			ccView.webEngine.load(null);
		}
		
	}
	//Event Handler for the Exit File Menu Item
	private class ExitMenuHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Platform.exit();
		}
		
	}
	//Event Handler for the Case Menu Item
	private class CaseMenuItemHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent e) {
			// TODO Auto-generated method stub
			//Get the source of the event and identify if it is add, modify ot delete
			MenuItem menuChosen = (MenuItem) e.getSource();
			
			if(menuChosen.getText().equalsIgnoreCase("Add Case")) {	
				caseView = new AddCaseView("Add Case");
				stage = caseView.buildView();
				caseView.stage.show();
				
				caseView.updateButton.setOnAction(new AddButtonHandler());
				caseView.clearButton.setOnAction(new ClearCVButtonHandler());
				caseView.closeButton.setOnAction(new CloseCVButtonHandler());
				
			}
			else if(menuChosen.getText().equalsIgnoreCase("Modify Case")) {
				caseView = new ModifyCaseView("Modify Case");
				stage = caseView.buildView();
				caseView.stage.show();
				
				caseView.caseTypeTextField.setText(currentCase.getCaseType());
				caseView.caseLinkTextField.setText(currentCase.getCaseLink());
				caseView.caseNotesTextArea.setText(currentCase.getCaseNotes());
				caseView.categoryTextField.setText(currentCase.getCaseCategory());
				caseView.titleTextField.setText(currentCase.getCaseTitle());
				caseView.caseNumberTextField.setText(currentCase.getCaseNumber());
				caseView.caseDatePicker.setValue(LocalDate.parse(currentCase.getCaseDate()));
				
				caseView.updateButton.setOnAction(new ModifyButtonHandler());
				caseView.clearButton.setOnAction(new ClearCVButtonHandler());
				caseView.closeButton.setOnAction(new CloseCVButtonHandler());
				
				
			}
			else if(menuChosen.getText().equalsIgnoreCase("Delete Case")){
				caseView = new DeleteCaseView("Delete Case");
				stage = caseView.buildView();
				caseView.stage.show();
				
				caseView.caseTypeTextField.setText(currentCase.getCaseType());
				caseView.caseLinkTextField.setText(currentCase.getCaseLink());
				caseView.caseNotesTextArea.setText(currentCase.getCaseNotes());
				caseView.categoryTextField.setText(currentCase.getCaseCategory());
				caseView.titleTextField.setText(currentCase.getCaseTitle());
				caseView.caseNumberTextField.setText(currentCase.getCaseNumber());
				caseView.caseDatePicker.setValue(LocalDate.parse(currentCase.getCaseDate()));
				
				caseView.updateButton.setOnAction(new DeleteButtonHandler());
				caseView.clearButton.setOnAction(new ClearCVButtonHandler());
				caseView.closeButton.setOnAction(new CloseCVButtonHandler());
			}
		}
		
	}
	//Event Handler for the Search Button Item
	private class SearchButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String title = ccView.titleTextField.getText();
			String caseType = ccView.caseTypeTextField.getText();
			String caseNumber = ccView.caseNumberTextField.getText();
			String year = ccView.yearComboBox.getValue();
			
			ObservableList<Case> cases = FXCollections.observableArrayList(ccModel.searchCases(title, caseType, year, caseNumber));
			
			Collections.sort(cases);
			if(cases.size()!=0) {
				currentCase = cases.get(0);
				
				ccView.titleTextField.setText(currentCase.getCaseTitle());
				ccView.caseTypeTextField.setText(currentCase.getCaseType());
				ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
				ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
				ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0, 4));
				ccView.yearComboBox.setItems(ccModel.yearList);
				ccView.messageLabel.setText(cases.size() + " cases");
				ccView.caseTableView.setItems(cases);
			}
			
			
			if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
				URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
				if (url != null) ccView.webEngine.load(url.toExternalForm());
			} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
				ccView.webEngine.load(currentCase.getCaseLink());
			} else {
				URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
				if (url != null) ccView.webEngine.load(url.toExternalForm());
			}
		}
		
	} 
	//Event Handler for the Clear Button Item
	private class ClearButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			ccView.titleTextField.setText("");
			ccView.caseNumberTextField.setText("");
			ccView.caseTypeTextField.setText("");
			ccView.yearComboBox.setValue("");
		}
		
	}
	//Event Handler for the Add Case Button in a new Stage created while adding the case
	private class AddButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//try-catch block has code to identify and notify the user if case date, title, type and number are missing
			//In addition to that, if the user tries to add a case with duplicate case number, an appropriate message is
			//displayed to the user.
			try {
				if(caseView.caseDatePicker.getValue().toString().equalsIgnoreCase("") || caseView.titleTextField.getText().equalsIgnoreCase("") || caseView.caseNumberTextField.getText().equalsIgnoreCase("") || caseView.caseTypeTextField.getText().equalsIgnoreCase("")) {
					throw new DataException("Add Missing Data");
				}
				for(Case c : ccModel.caseList) {
					if(c.getCaseNumber().equalsIgnoreCase(caseView.caseNumberTextField.getText())) {
						throw new DataException("Duplicate Case Number");
					}
				}
				Case newCase = new Case(caseView.caseDatePicker.getValue().toString(),  caseView.titleTextField.getText(), caseView.caseTypeTextField.getText(), caseView.caseNumberTextField.getText(), caseView.caseLinkTextField.getText(), caseView.categoryTextField.getText(), caseView.caseNotesTextArea.getText());
				ccModel.caseList.add(newCase);
				ccModel.caseMap.put(newCase.getCaseNumber(), newCase);
				Collections.sort(ccModel.caseList);
				
				ccModel.buildYearMapAndList();
				ccView.yearComboBox.setItems(ccModel.yearList);
				ccView.caseTableView.setItems(ccModel.caseList);
			}
			catch(DataException ex) {
				System.out.println("Add Case Exception");
			}
			
		}
		
	}
	//Event Handler for the Modify Case Button in a new Stage created while modifying the case
	private class ModifyButtonHandler implements EventHandler<ActionEvent>{
		
		/*
		 * try-catch block has code to identify and catch for errors in user input data.
		 * Any incomplete and wrong data would be caught and an appropriate DataException error 
		 * will be displayed to the user. 
		 */

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String caseNumber = currentCase.getCaseNumber();
			ccModel.caseMap.remove(currentCase.getCaseNumber());
			
			try {
				if(caseView.caseDatePicker.getValue().toString().equalsIgnoreCase("") || caseView.titleTextField.getText().equalsIgnoreCase("") || caseView.caseNumberTextField.getText().equalsIgnoreCase("") || caseView.caseTypeTextField.getText().equalsIgnoreCase("")) {
					throw new DataException("Add Missing Data");
				}
				for(Case c : ccModel.caseList) {
					if(c.getCaseNumber().equalsIgnoreCase(caseView.caseNumberTextField.getText()) && (!c.getCaseNumber().equalsIgnoreCase(caseNumber))) {
						throw new DataException("Duplicate Case Number");
					}
				}
				Case modCase = new Case(caseView.caseDatePicker.getValue().toString(),  caseView.titleTextField.getText(), caseView.caseTypeTextField.getText(), caseView.caseNumberTextField.getText(), caseView.caseLinkTextField.getText(), caseView.categoryTextField.getText(), caseView.caseNotesTextArea.getText());
				int idx = ccModel.caseList.indexOf(currentCase);
				ccModel.caseList.set(idx, modCase);
				ccModel.caseMap.put(modCase.getCaseNumber(), modCase);
				ccModel.buildYearMapAndList();
				currentCase = ccModel.caseList.get(idx);
				
				ccView.titleTextField.setText(currentCase.getCaseTitle());
				ccView.caseTypeTextField.setText(currentCase.getCaseType());
				ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
				ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
				ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0, 4));
				ccView.yearComboBox.setItems(ccModel.yearList);
			}
			catch(DataException ex) {
				System.out.println("Modify Case Exception");
			}
			
			
			
		}
		
	}
	//Event Handler for the Delete Case Button in a new Stage created while deleting the case
	private class DeleteButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			ccModel.caseMap.remove(currentCase.getCaseNumber());
			
			ccModel.caseList.remove(currentCase);
			ccModel.buildYearMapAndList();
			
			ccView.yearComboBox.setItems(ccModel.yearList);
            ccView.messageLabel.setText(ccModel.caseList.size() + " cases"); 
		}
		
	}
	
	private class ClearCVButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			caseView.titleTextField.setText("");
			caseView.categoryTextField.setText("");
			caseView.caseDatePicker.setValue(null);
			caseView.caseLinkTextField.setText("");
			caseView.caseNotesTextArea.setText("");
			caseView.caseNumberTextField.setText("");
			caseView.caseTypeTextField.setText("");
		}
		
	}
	
	private class CloseCVButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			caseView.stage.close();
		}
		
	}
	
	//Event Handler to save the changes made to case list in a new TSV file.
	//
	private class SaveMenuItemHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			FileChooser fileSaver = new FileChooser();
			fileSaver.setTitle("Save File");
			fileSaver.setInitialDirectory(new File(DEFAULT_PATH));
			File fS = fileSaver.showSaveDialog(stage);
			
			String savedFile = fS.getAbsolutePath();
			
			boolean chkWrite = ccModel.writeCases(savedFile);
			
			if(chkWrite) {
				ccView.messageLabel.setText(fS.getName() + " saved");
			}
		}
		
	}
	
	//Event Handler to display the cases in a bar chart. 
	private class CaseCountChartMenuItemHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			ccView.showChartView(ccModel.yearMap);
		}
		
	}
}

