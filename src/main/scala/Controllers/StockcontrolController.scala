package Controllers

import MainSystem.MainApp
import Database.InventoryDatabase
import Models.stockedithistory

import scalafx.scene.control.{Alert,TableColumn,TableView,TableCell}
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.stage.{Modality, Stage}
import scalafx.event.ActionEvent
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary


@sfxml
class StockcontrolController (
	val stocktableview: TableView[stockedithistory],
	val idColumn: TableColumn[stockedithistory,Int],
	val dateColumn: TableColumn[stockedithistory, String],
	val itemColumn: TableColumn[stockedithistory, String],
	val branchColumn: TableColumn[stockedithistory, String],
	val amountColumn: TableColumn[stockedithistory, Int],
	val descriptionColumn: TableColumn[stockedithistory, String]
	) {	
	
	//get data from sql and show to table
	RefreshStockedithistorylist()

	def RefreshStockedithistorylist() = {
		//update the itemlist from database
		InventoryDatabase.UpdateStockhistorylist()

		//show to tableview
		stocktableview.items = InventoryDatabase.Stockhistorylist

		// initialize columns's cell values
		idColumn.cellValueFactory = {_.value.stockeditid}
	  	dateColumn.cellValueFactory = {_.value.DatetoString()}
	  	itemColumn.cellValueFactory = {_.value.getItemname()}
	  	branchColumn.cellValueFactory = {_.value.getBranchname()}
	  	amountColumn.cellValueFactory = {_.value.amount}
	  	descriptionColumn.cellValueFactory = {_.value.description}
	}

	def addstock() = {
		val okClicked:Boolean = MainApp.showStockEditDialog("add")

		if(okClicked){
    		//refresh the table data
    		RefreshStockedithistorylist()
    	}
	}

	def minusstock() = {
		val okClicked:Boolean = MainApp.showStockEditDialog("minus")

		if(okClicked){
    		//refresh the table data
    		RefreshStockedithistorylist()
    	}
	}

}	
