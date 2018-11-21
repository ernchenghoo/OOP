package Controllers

import MainSystem.MainApp
import Models.Itemstock

import scalafx.scene.control.{Alert,TableColumn,TableView,TableCell}
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.stage.{Modality, Stage}
import scalafx.event.ActionEvent
import scalafx.Includes._
import scalafx.collections.ObservableBuffer

@sfxml
class StockcheckController (
	val stocktableview: TableView[Itemstock],
	val itemidColumn: TableColumn[Itemstock, Int],
	val itemnameColumn: TableColumn[Itemstock, String],
	val branchidColumn: TableColumn[Itemstock, Int],
	val branchnameColumn: TableColumn[Itemstock, String],
	val numofstockColumn: TableColumn[Itemstock, Int]
	) {	

	var ItemstockData:ObservableBuffer[Itemstock] = new ObservableBuffer[Itemstock]() 

	ItemstockData ++= Itemstock.getAllItemStocks

	Refreshstocklist()
	def Refreshstocklist() = {
		
		//show to tableview
		stocktableview.items = ItemstockData

		// initialize columns's cell values
	  	itemidColumn.cellValueFactory = {_.value.itemid}
	  	itemnameColumn.cellValueFactory = {_.value.getItemname()}
	  	branchidColumn.cellValueFactory = {_.value.branchid}
	  	branchnameColumn.cellValueFactory = {_.value.getBranchlocation()}
	  	numofstockColumn.cellValueFactory = {_.value.numofstock}
	}



}