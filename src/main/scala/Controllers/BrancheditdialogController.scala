package Controllers

import Models.Branch
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.scene.control.{TextArea,TextField, TableColumn, Label, Alert}
import scalafx.stage.Stage
import scalafx.event.ActionEvent

@sfxml
class BrancheditdialogController (
	val title: Label,
	val branchidinputbox: TextField,
	val locationinputbox: TextField
	) {	

	var  dialogStage : Stage  = null
	var addoredit:String = null
	var  okClicked = false

	var branchinDialog: Branch = null


	//initialze the data
	def initializedata() = {

		if(addoredit == "add"){
			title.setText("Add Item")
			var maxid = 0
			for(branch <-Branch.getAllBranchs){
				if(branch.branchid.getValue() > maxid){
					maxid = branch.branchid.getValue()
				}
			}
			//new id
			maxid = maxid + 1
			branchidinputbox.text.value = maxid.toString()

		}else{
			title.setText("Edit Item")
			branchidinputbox.text.value = branchinDialog.branchid.getValue().toString()
			locationinputbox.text.value = branchinDialog.location.getValue()
		}	
	}

	def submit(action :ActionEvent) = {
		if (checkinput()) {

			branchinDialog.branchid.value = branchidinputbox.text.value.toInt
			branchinDialog.location.value = locationinputbox.text.value.toString

			
	      	okClicked = true;
	      	dialogStage.close()
	    }
	}

	def cancel(action :ActionEvent) = {
	   dialogStage.close();
	}

	def nullChecking (x : String) = x == null || x.length == 0

	def checkinput() :Boolean = {
		var errorMessage = ""

		if(nullChecking(locationinputbox.text.value))
			errorMessage += "Location cannot be empty\n"

		

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
