package Controllers

import MainSystem.MainApp
import Database.InventoryDatabase
import Models.Stockedithistory

import scalafx.scene.control.{Alert,TableColumn,TableView,TableCell}
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.stage.{Modality, Stage}
import scalafx.event.ActionEvent
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary


@sfxml
class StockcontrolController (
	val stocktableview: TableView[Stockedithistory],
	val idColumn: TableColumn[Stockedithistory,Int],
	val dateColumn: TableColumn[Stockedithistory, String],
	val itemColumn: TableColumn[Stockedithistory, String],
	val branchColumn: TableColumn[Stockedithistory, String],
	val amountColumn: TableColumn[Stockedithistory, Int],
	val descriptionColumn: TableColumn[Stockedithistory, String]
	) {	
	
	//get data from sql and show to table
	RefreshStockedithistorylist()

	def RefreshStockedithistorylist() = {

		//show to tableview
		stocktableview.items = Stockedithistory.getAllStockedithistorys

		// initialize columns's cell values
		idColumn.cellValueFactory = {_.value.stockeditid}
	  	dateColumn.cellValueFactory = {_.value.DatetoString()}
	  	itemColumn.cellValueFactory = {_.value.getItemname()}
	  	branchColumn.cellValueFactory = {_.value.getBranchlocation()}
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
