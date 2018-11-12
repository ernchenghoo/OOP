package Controllers

import MainSystem.MainApp
import Database.InventoryDatabase
import Database.ReturnItemDatabase
import Models.returnitemhistory

import scalafx.scene.control.{Alert,TableColumn,TableView,TableCell}
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.stage.{Modality, Stage}
import scalafx.event.ActionEvent
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary


@sfxml
class ReturnitemcontrolController (
	val stocktableview: TableView[returnitemhistory],
	val idColumn: TableColumn[returnitemhistory,Int],
	val dateColumn: TableColumn[returnitemhistory, String],
	val itemColumn: TableColumn[returnitemhistory, String],
	val branchColumn: TableColumn[returnitemhistory, String],
	val amountColumn: TableColumn[returnitemhistory, Int],
	val descriptionColumn: TableColumn[returnitemhistory, String]
	) {	
	
	//get data from sql and show to table
	Refreshreturnitemhistorylist()

	def backtoMainmenu() = {
		MainApp.showPersonOverview()

	}

	def handleDeletePerson(action : ActionEvent) = {
	    val selectedIndex = stocktableview.selectionModel().selectedIndex.value
	    if (selectedIndex >= 0) {
	        stocktableview.items().remove(selectedIndex);
	    } else {
	        // Nothing selected.
	        val alert = new Alert(AlertType.Warning){
	          initOwner(MainApp.stage)
	          title       = "No Selection"
	          headerText  = "No Items Selected"
	          contentText = "Please select an Item in the table."
	        }.showAndWait()
	    }
	}

	def Refreshreturnitemhistorylist() = {
		//update the itemlist from database
		ReturnItemDatabase.Updatereturnitemlist()

		//show to tableview
		stocktableview.items = ReturnItemDatabase.returnitemlist

		// initialize columns's cell values
		idColumn.cellValueFactory = {_.value.returnitemid}
	  	dateColumn.cellValueFactory = {_.value.DatetoString()}
	  	itemColumn.cellValueFactory = {_.value.getItemname()}
	  	branchColumn.cellValueFactory = {_.value.getBranchlocation()}
	  	amountColumn.cellValueFactory = {_.value.amount}
	  	descriptionColumn.cellValueFactory = {_.value.description}
	}

	def addstock() = {
		val okClicked:Boolean = MainApp.showReturnItemDialog("add")

		if(okClicked){
    		//refresh the table data
    		Refreshreturnitemhistorylist()
    	}
	}

	/*def minusstock() = {
		val okClicked:Boolean = MainApp.showReturnItemDialog("minus")

		if(okClicked){
    		//refresh the table data
    		Refreshreturnitemhistorylist()
    	}
	}*/

}	
