package Controllers

import MainSystem.MainApp
import Database.BranchDatabase
import Models.branch

import scalafx.scene.control.{Alert,TableColumn,TableView,TableCell}
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary


@sfxml
class BranchmainController (
	val branchtableview: TableView[branch],
	val branchidColumn: TableColumn[branch, Int],
	val branchlocationColumn: TableColumn[branch, String]
	) {	


	Refreshbranchlist()
	def Refreshbranchlist() = {
		//update the itemlist from database
		BranchDatabase.UpdateBranchlist()

		//show to tableview
		branchtableview.items = BranchDatabase.Branchlist

		// initialize columns's cell values
	  	branchidColumn.cellValueFactory = {_.value.branchid}
	  	branchlocationColumn.cellValueFactory = {_.value.location}
	  
	}

	def backtoMainmenu() = {
		MainApp.showPersonOverview()

	}

	def handleDeleteItem(action : ActionEvent) = {
	    val selectedIndex = branchtableview.selectionModel().selectedIndex.value
	    if (selectedIndex >= 0) {
	    	var selecteditem:branch = BranchDatabase.Branchlist.get(selectedIndex)
	    	var itemidOfSelecteditem = selecteditem.branchid.getValue()

	    	BranchDatabase.DeletefromBranchlist(itemidOfSelecteditem)
	     	branchtableview.items().remove(selectedIndex)
	    } else {
	      // Nothing selected.
	      val alert = new Alert(AlertType.Error){
	        initOwner(MainApp.stage)
	        title       = "No Selection"
	        headerText  = "No Item Selected"
	        contentText = "Please select a item in the table."
	      }.showAndWait()
	    }
	}

	def handleEditItem(action : ActionEvent) = {

	    val selectedIndex = branchtableview.selectionModel().selectedIndex.value
	    if (selectedIndex >= 0) {
	    	var selecteditem:branch = BranchDatabase.Branchlist.get(selectedIndex)

	    	val okClicked: Boolean = MainApp.showBranchEditDialog(selecteditem,"edit")

	    	if(okClicked){
	    		//refresh the table data
	    		Refreshbranchlist()
	    	}

	    } else {
	      // Nothing selected.
	      val alert = new Alert(AlertType.Error){
	        initOwner(MainApp.stage)
	        title       = "No Selection"
	        headerText  = "No Item Selected"
	        contentText = "Please select a item in the table."
	      }.showAndWait()
	    }
	}

	def handleAddItem(action : ActionEvent) = {

	    val okClicked: Boolean = MainApp.showBranchEditDialog(null,"add")

    	if(okClicked){
    		//refresh the table data
    		Refreshbranchlist()
    	}
	    

	}



}