/*
 * Name : Medha Praneeth Reddy Modulla
 * Andrew Id : mmodulla
 */
package hw3;

import java.time.format.DateTimeFormatter;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeleteCaseView extends CaseView{

	DeleteCaseView(String header) {
		super(header);
		// TODO Auto-generated constructor stub
	}

	@Override
	Stage buildView() {
		// TODO Auto-generated method stub
		/*
		 * Setting the stage for a new Delete Case Dialog Box
		 */
		stage.setTitle("Delete Case");
		updateButton.setText("Delete Case");
		caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
		stage.setScene(scene);
		
		return stage;
	}

}
