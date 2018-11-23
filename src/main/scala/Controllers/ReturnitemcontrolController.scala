package Controllers

import MainSystem.MainApp
import Models.Returnitemhistory

import scalafx.scene.control.{Alert,TableColumn,TableView,TableCell}
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.stage.{Modality, Stage}
import scalafx.event.ActionEvent
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary


@sfxml
class ReturnitemcontrolController (
	val stocktableview: TableView[Returnitemhistory],
	val idColumn: TableColumn[Returnitemhistory,Int],
	val dateColumn: TableColumn[Returnitemhistory, String],
	val salesidColumn: TableColumn[Returnitemhistory, Int],
	val itemColumn: TableColumn[Returnitemhistory, String],
	val branchColumn: TableColumn[Returnitemhistory, String],
	val amountColumn: TableColumn[Returnitemhistory, Int],
	val descriptionColumn: TableColumn[Returnitemhistory, String]
	) {	
	
	//get data from sql and show to table
	Refreshreturnitemhistorylist()

	def backtoMainmenu() = {
		MainApp.showMainMenu()

	}

	def handleDeleteItem(action : ActionEvent) = { 

	    try
	    {

		    val selectedIndex = stocktableview.selectionModel().selectedIndex.value
		    val selectedItem = stocktableview.selectionModel().selectedItem.value.returnitemid
	    	Returnitemhistory.minusstock(selectedItem.value)
	    	Refreshreturnitemhistorylist()	


	    }catch{

	    	case x: NullPointerException =>
	    		{
			     val alert = new Alert(AlertType.Warning){
			          initOwner(MainApp.stage)
			          title       = "No Selection"
			          headerText  = "No Items Selected"
			          contentText = "Please select an Item in the table."
			        }.showAndWait()
	    		}
	    }
	    		    		
	}

	def Refreshreturnitemhistorylist() = {
		//update the itemlist from database
		//ReturnItemDatabase.Updatereturnitemlist()

		//show to tableview
		stocktableview.items = Returnitemhistory.getAllReturnitemhistory

		// initialize columns's cell values
		idColumn.cellValueFactory = {_.value.returnitemid}
	  	dateColumn.cellValueFactory = {_.value.DatetoString()}
	  	salesidColumn.cellValueFactory = {_.value.salesid}
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

}	
