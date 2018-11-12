package Controllers

import MainSystem.MainApp
import Database.InventoryDatabase
import Models.Itemstock

import scalafx.scene.control.{Alert,TableColumn,TableView,TableCell}
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.stage.{Modality, Stage}
import scalafx.event.ActionEvent
import scalafx.Includes._

@sfxml
class StockcheckController (
	val stocktableview: TableView[Itemstock],
	val itemidColumn: TableColumn[Itemstock, Int],
	val itemnameColumn: TableColumn[Itemstock, String],
	val branchidColumn: TableColumn[Itemstock, Int],
	val branchnameColumn: TableColumn[Itemstock, String],
	val numofstockColumn: TableColumn[Itemstock, Int]
	) {	

	Refreshstocklist()
	def Refreshstocklist() = {
		//update the itemlist from database
		InventoryDatabase.UpdateStocklist()

		//show to tableview
		stocktableview.items = InventoryDatabase.Stocklist

		// initialize columns's cell values
	  	itemidColumn.cellValueFactory = {_.value.itemid}
	  	itemnameColumn.cellValueFactory = {_.value.getItemname()}
	  	branchidColumn.cellValueFactory = {_.value.branchid}
	  	branchnameColumn.cellValueFactory = {_.value.getBranchlocation()}
	  	numofstockColumn.cellValueFactory = {_.value.numofstock}
	}



}