package Controllers

import MainSystem.MainApp
import Models.Itemstock

import scalafx.scene.control.{Alert,TableColumn,TableView,TableCell}
import scalafx.scene.control.{ChoiceBox,TextArea,TextField, TableColumn, Label, Alert}
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.stage.{Modality, Stage}
import scalafx.event.ActionEvent
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.input.KeyEvent

@sfxml
class StockcheckController (
	val stocktableview: TableView[Itemstock],
	val itemidColumn: TableColumn[Itemstock, Int],
	val itemnameColumn: TableColumn[Itemstock, String],
	val branchidColumn: TableColumn[Itemstock, Int],
	val branchnameColumn: TableColumn[Itemstock, String],
	val numofstockColumn: TableColumn[Itemstock, Int],
	val searchitemname: TextField,
	val searchbranchname: TextField
	) {	


	var ItemstockData:ObservableBuffer[Itemstock] = new ObservableBuffer[Itemstock]() 


	//only show itemstock that have at least 1 number of stock
	for(itemstockobject <- Itemstock.getAllItemStocks){
		if(itemstockobject.numofstock.getValue() > 0){
			ItemstockData +=itemstockobject
		}
	}


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


	def search(action :ActionEvent){
		var FilteredItemstockData:ObservableBuffer[Itemstock] = new ObservableBuffer[Itemstock]() 

		var searcheditemname =  searchitemname.text.value
		var searchedbranchname =  searchbranchname.text.value

		
		if(nullChecking(searcheditemname) && nullChecking(searchedbranchname)){
			for(itemstockobject <- Itemstock.getAllItemStocks){
				if(itemstockobject.numofstock.getValue() > 0){
					FilteredItemstockData +=itemstockobject
				}
			}
		}else{
			for(itemstockobject <- Itemstock.getAllItemStocks){
				var isitemnamematch = itemstockobject.getItemname().getValue().toLowerCase().contains(searcheditemname.toLowerCase())
				var isbranchnamematch = itemstockobject.getBranchlocation().getValue().toLowerCase().contains(searchedbranchname.toLowerCase()) 

				if(isitemnamematch && isbranchnamematch){
					FilteredItemstockData += itemstockobject
				}
			}
			
		}
		

		ItemstockData=FilteredItemstockData
		Refreshstocklist()
	}

	def nullChecking (x : String) = x == null || x.length == 0



}