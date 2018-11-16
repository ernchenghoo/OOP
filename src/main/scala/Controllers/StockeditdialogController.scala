package Controllers

import Models.Item
import Models.Branch
import Models.Stockedithistory

import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.scene.control.{ChoiceBox,TextArea,TextField, TableColumn, Label, Alert}
import scalafx.stage.Stage
import scalafx.event.ActionEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@sfxml
class StockeditdialogController (
	val title: Label,
	val idinputbox: TextField,
	val itemdropdown: ChoiceBox[String],
	val branchdropdown: ChoiceBox[String],
	val amountinputbox: TextField,
	val descinputbox: TextArea
	) {	

	var  dialogStage : Stage  = null
	var addorminus:String = null
	var  okClicked = false


	//initialze the data
	def initializedata() = {
		//set title label
		if(addorminus == "add")
			title.setText("Add stock")
		else
			title.setText("Minus stock")

		//initialize id
		var maxid = 0
		for(stockedit <-Stockedithistory.getAllStockedithistorys){
			if(stockedit.stockeditid.getValue() > maxid){
				maxid = stockedit.stockeditid.getValue()
			}
		}
		//new id
		maxid = maxid + 1
		idinputbox.text.value = maxid.toString()

		//initialize item choose
		itemdropdown.getItems().add("Select Item");
		itemdropdown.setValue("Select Item")
		for(item <- Item.getAllItems){
			itemdropdown.getItems().add(item.name.getValue());
		}

		//initialize branch choose
		branchdropdown.getItems().add("Select Branch");
		branchdropdown.setValue("Select Branch")
		for(branch <- Branch.getAllBranchs){
			branchdropdown.getItems().add(branch.location.getValue());
		}
	}

	def submit(action :ActionEvent) = {
		
		if (checkinput()) {
			var stockeditid = idinputbox.text.value.toInt
			var datenow = new Date()
			var formmater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			formmater.setTimeZone(TimeZone.getTimeZone("UTC"))
			var datestring = formmater.format(datenow)
			var itemname = itemdropdown.getValue()
			var branchlocation = branchdropdown.getValue()
			var amount = amountinputbox.text.value.toInt
			var desc = descinputbox.text.value
			//edit data in database

			//find itemid with item name
			var itemid = 0
			for(item <- Item.getAllItems){
				if(item.name.getValue() == itemname)
					itemid = item.id.getValue()
			}

			//find branchid with branch name
			var branchid = 0
			for(branch <- Branch.getAllBranchs){
				if(branch.location.getValue() == branchlocation)
					branchid = branch.branchid.getValue()
			}

			if(addorminus == "add"){
				Stockedithistory.addStock(stockeditid,datestring,itemid,itemname,branchid,branchlocation,amount,desc)
			}else{
				Stockedithistory.minusStock(stockeditid,datestring,itemid,itemname,branchid,branchlocation,amount,desc)
			}

			

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
		var itemnotnull = false
		var branchnotnull = false
		var amountvalid = false


		if(itemdropdown.getValue() == "Select Item")
			errorMessage += "Please Select Item! \n"
		else
			itemnotnull = true

		if(branchdropdown.getValue() == "Select Branch")
			errorMessage += "Please Select Branch! \n"
		else
			branchnotnull = true

		if(nullChecking(amountinputbox.text.value))
			errorMessage += "Amount cannot be empty\n"
		else{
			try{
				amountinputbox.text.value.toInt

				if(amountinputbox.text.value.toInt < 1)
					errorMessage += "Amount must have at least 1\n"
				else
					amountvalid = true

			}catch{
				case e : NumberFormatException =>
					errorMessage += "Amount must be a number\n"
			}
		}

		if(nullChecking(descinputbox.text.value))
			errorMessage += "Description cannot be empty\n"

		//minus only need to check stock
		if(addorminus == "minus"){
			if(itemnotnull && branchnotnull && amountvalid){
				var itemid = 0
				var branchid = 0
				var amount = amountinputbox.text.value.toInt

				for(item <- Item.getAllItems){
					if(item.name.getValue() == itemdropdown.getValue())
						itemid = item.id.getValue()
				}

				for(branch <- Branch.getAllBranchs){
					if(branch.location.getValue() == branchdropdown.getValue())
						branchid = branch.branchid.getValue()
				}

				var stockavailable = Stockedithistory.getstock(itemid,branchid)
				if(amount > stockavailable)
					errorMessage += s"Item Stock insufficient! Please lower the amount (Available stock:${stockavailable})\n"
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
