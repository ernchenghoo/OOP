package Controllers

import MainSystem.MainApp
import Database.ReturnItemDatabase

import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}

import scalafx.scene.control.{TextArea,TextField, TableColumn, Label, Alert}
import scalafx.stage.Stage
import scalafx.event.ActionEvent

@sfxml
class ReturnitemController (
	val anchorpanetab: AnchorPane,
	val itemidinputbox: TextField,
	val itemnameinputbox: TextField,
	val itemdescinputbox: TextArea,
	val itempriceinputbox: TextField) {

	var  dialogStage : Stage  = null
	var  okClicked = false

	def backtoMainmenu() = {
		MainApp.showMainMenu()

	}

	def submit(action :ActionEvent) = {
		if (checkinput()) {

			var id = itemidinputbox.text.value.toInt
			var name = itemnameinputbox.text.value
			var desc = itemdescinputbox.text.value
			var price = itempriceinputbox.text.value.toDouble
			//edit data in database

			ReturnItemDatabase.AddtoItemlist(id,name,desc,price)

			okClicked = true;
	      	dialogStage.close()

	   	}
	}

	def nullChecking (x : String) = x == null || x.length == 0

	def checkinput() :Boolean = {
		var errorMessage = ""

		if(nullChecking(itemnameinputbox.text.value))
			errorMessage += "Item name cannot be empty\n"

		if(nullChecking(itemdescinputbox.text.value))
			errorMessage += "Item description cannot be empty\n"

		if(nullChecking(itempriceinputbox.text.value))
			errorMessage += "Item price cannot be empty\n"
		else{
			try{
				itempriceinputbox.text.value.toDouble
			}catch{
				case e : NumberFormatException =>
					errorMessage += "Item price must be a number\n"
			}
		}

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
