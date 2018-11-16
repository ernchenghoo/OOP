package Controllers

import MainSystem.MainApp
import Database.BranchDatabase
import Models.Branch

import scalafx.scene.control.{Alert,TableColumn,TableView,TableCell}
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary
import scalafx.collections.ObservableBuffer

@sfxml
class BranchmainController (
	val branchtableview: TableView[Branch],
	val branchidColumn: TableColumn[Branch, Int],
	val branchlocationColumn: TableColumn[Branch, String]
	) {	


	var BranchData:ObservableBuffer[Branch] = new ObservableBuffer[Branch]()

	BranchData ++= Branch.getAllBranchs

	Refreshbranchlist()
	def Refreshbranchlist() = {
		
		//show to tableview
		branchtableview.items = BranchData

		// initialize columns's cell values
	  	branchidColumn.cellValueFactory = {_.value.branchid}
	  	branchlocationColumn.cellValueFactory = {_.value.location}
	  
	}

	def backtoMainmenu() = {
		MainApp.showMainMenu()

	}

	def handleDeletebranch(action : ActionEvent) = {
	    val selectedIndex = branchtableview.selectionModel().selectedIndex.value
	    if (selectedIndex >= 0) {
	    	var selectedbranch:Branch = BranchData.get(selectedIndex)
	    	
	    	//if selectedbranch.delete() return true the item is deleted successfully
	    	if(selectedbranch.delete()){
	    		branchtableview.items().remove(selectedIndex)
	    	}else{
	    		val alert = new Alert(Alert.AlertType.Warning){
			      initOwner(MainApp.stage)
			      title = "Failed to Save"
	              headerText = "Database Error"
	              contentText = "Database problem filed to save changes"
			    }.showAndWait()
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

	def handleEditbranch(action : ActionEvent) = {

	    val selectedIndex = branchtableview.selectionModel().selectedIndex.value
	    if (selectedIndex >= 0) {
	    	var selectedbranch:Branch = BranchData.get(selectedIndex)

	    	val okClicked: Boolean = MainApp.showBranchEditDialog(selectedbranch,"edit")

	    	if(okClicked){
	    		//if selectedbranch.save() return true then the data is save successfully to database
	    		if(selectedbranch.save()){
	    			//nothing
	    		}else{
	    			val alert = new Alert(Alert.AlertType.Warning) {
		              initOwner(MainApp.stage)
		              title = "Failed to Save"
		              headerText = "Database Error"
		              contentText = "Database problem filed to save changes"
		            }.showAndWait()
	    		}

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

	def handleAddbranch(action : ActionEvent) = {
		var newbranch = new Branch(0,"")

	    val okClicked: Boolean = MainApp.showBranchEditDialog(newbranch,"add")

    	if(okClicked){
    		if(newbranch.save()){
    			BranchData += newbranch
    		}else{
    			val alert = new Alert(Alert.AlertType.Warning) {
	              initOwner(MainApp.stage)
	              title = "Failed to Save"
	              headerText = "Database Error"
	              contentText = "Database problem filed to save changes"
	            }.showAndWait()
    		}
    	}
	    

	}



}