package Controllers

import Models.Item

import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.scene.control.{TextArea,TextField, TableColumn, Label, Alert}
import scalafx.stage.Stage
import scalafx.event.ActionEvent

@sfxml
class ItemeditdialogController (
	val title: Label,
	val itemidinputbox: TextField,
	val itemnameinputbox: TextField,
	val itemdescinputbox: TextArea,
	val itempriceinputbox: TextField
	) {	

	var  dialogStage : Stage  = null
	var addoredit:String = null
	var  okClicked = false

	var IteminDialog:Item = null

	//initialze the data
	def initializeitemdata() = {

		if(addoredit == "add"){
			title.setText("Add Item")
			var maxid = 0

			for(item <- Item.getAllItems){
				if(item.id.getValue() > maxid){
					maxid = item.id.getValue()
				}
			}
			//new id
			maxid = maxid + 1
			itemidinputbox.text.value = maxid.toString()

		}else{
			title.setText("Edit Item")
			itemidinputbox.text.value = IteminDialog.id.getValue().toString()
			itemnameinputbox.text.value = IteminDialog.name.getValue()
			itemdescinputbox.text.value = IteminDialog.desc.getValue()
			itempriceinputbox.text.value = IteminDialog.price.getValue().toString()
		}	
	}

	def submit(action :ActionEvent) = {
		if (checkinput()) {

			IteminDialog.id.value = itemidinputbox.text.value.toInt
			IteminDialog.name.value = itemnameinputbox.text.value
			IteminDialog.desc.value = itemdescinputbox.text.value
			IteminDialog.price.value = itempriceinputbox.text.value.toDouble
			//edit data in database

			

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
