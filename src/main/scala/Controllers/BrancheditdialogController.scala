package Controllers

import Models.branch
import Database.BranchDatabase

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


	//initialze the data
	def initializedata(branch: branch){

		if(addoredit == "add"){
			title.setText("Add Item")
			var maxid = 0
			for(branch <-BranchDatabase.Branchlist){
				if(branch.branchid.getValue() > maxid){
					maxid = branch.branchid.getValue()
				}
			}
			//new id
			maxid = maxid + 1
			branchidinputbox.text.value = maxid.toString()

		}else{
			title.setText("Edit Item")
			branchidinputbox.text.value = branch.branchid.getValue().toString()
			locationinputbox.text.value = branch.location.getValue()
		}	
	}

	def submit(action :ActionEvent){
		if (checkinput()) {

			var branchid = branchidinputbox.text.value.toInt
			var location = locationinputbox.text.value
			

			//edit data in database
			if(addoredit == "add"){
				BranchDatabase.AddtoBranchlist(branchid,location)
			}else{
				BranchDatabase.EditfromBranchlist(branchid,location)
			}

			
	      	okClicked = true;
	      	dialogStage.close()
	    }
	}

	def cancel(action :ActionEvent) {
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
