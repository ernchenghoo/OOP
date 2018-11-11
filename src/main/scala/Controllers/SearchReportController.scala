package Controllers

import MainSystem.MainApp
import Database.BranchDatabase

import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.scene.control.{ChoiceBox,DatePicker, Alert}
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.stage.Stage
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary
import java.util.Date
import java.time.LocalDate


@sfxml
class SearchReportController (
	val fromDate:DatePicker,
	val toDate:DatePicker,
	val branchdropdown: ChoiceBox[String]) {

	var  dialogStage : Stage  = null

	def initializebranch() = {

	BranchDatabase.UpdateBranchlist()
	//initialize branch choose
		branchdropdown.getItems().add("Select Branch");
		branchdropdown.setValue("Select Branch")
		for(branch <- BranchDatabase.Branchlist){
			branchdropdown.getItems().add(branch.location.getValue());
		}
	}

	def backtoMainmenu() = {
		MainApp.showPersonOverview()
	}

	def handleReport() = {
		if (checkinput()) {
			var fromDate1 = fromDate.getValue()
			var toDate1 = toDate.getValue()
			var branchdropdown1 = branchdropdown.getValue()
			MainApp.showReport(fromDate1,toDate1,branchdropdown1)
		}
	}

	def checkinput() :Boolean = {
		var errorMessage = ""
		var fromdatenotnull = false
		var todatenotnull = false
		var branchnotnull = false

		if(fromDate.getValue() == null)
			errorMessage += "Please Select from Date! \n"
		else
			fromdatenotnull = true

		if(toDate.getValue() == null)
			errorMessage += "Please Select to Date! \n"
		else
			todatenotnull = true

		if(fromDate.getValue().toEpochDay() > toDate.getValue().toEpochDay())
			errorMessage += "From Date cannot greater than to Date! \n"

		if(branchdropdown.getValue() == "Select Branch")
			errorMessage += "Please Select Branch! \n"
		else
			branchnotnull = true

		if (errorMessage.length() == 0) {
	      return true;
	    } else {
	      // Show the error message.
	      val alert = new Alert(Alert.AlertType.Error){
	        initOwner(dialogStage)
	        title = "Invalid Fields"
	        headerText = "Please correct invalid fields"
	        contentText = errorMessage
	      }.showAndWait()

	      return false;
	    }
	}
}	