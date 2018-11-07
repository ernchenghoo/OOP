package Controllers

import MainSystem.MainApp
import Database.InventoryDatabase
import Models.Item

import scalafx.scene.control.{Alert,TableColumn,TableView,TableCell}
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.stage.{Modality, Stage}
import scalafx.event.ActionEvent
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary


@sfxml
class ItemcontrolController (
	val itemtableview: TableView[Item],
	val itemidColumn: TableColumn[Item, Int],
	val itemnameColumn: TableColumn[Item, String],
	val itemdescColumn: TableColumn[Item, String],
	val priceColumn: TableColumn[Item, Double]
	) {	
	
	//get data from sql and show to table
	RefreshItemlist()

	def RefreshItemlist() = {
		//update the itemlist from database
		InventoryDatabase.UpdateItemlist()

		//show to tableview
		itemtableview.items = InventoryDatabase.Itemlist

		// initialize columns's cell values
	  	itemidColumn.cellValueFactory = {_.value.id}
	  	itemnameColumn.cellValueFactory = {_.value.name}
	  	itemdescColumn.cellValueFactory = {_.value.desc}
	  	priceColumn.cellValueFactory = {_.value.price}
	}

	def handleDeleteItem(action : ActionEvent) = {
	    val selectedIndex = itemtableview.selectionModel().selectedIndex.value
	    if (selectedIndex >= 0) {
	    	var selecteditem:Item = InventoryDatabase.Itemlist.get(selectedIndex)
	    	var itemidOfSelecteditem = selecteditem.id.getValue()

	    	InventoryDatabase.DeletefromItemlist(itemidOfSelecteditem)
	     	itemtableview.items().remove(selectedIndex)
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

	    val selectedIndex = itemtableview.selectionModel().selectedIndex.value
	    if (selectedIndex >= 0) {
	    	var selecteditem:Item = InventoryDatabase.Itemlist.get(selectedIndex)

	    	val okClicked: Boolean = MainApp.showItemEditDialog(selecteditem,"edit")

	    	if(okClicked){
	    		//refrest the table data
	    		RefreshItemlist()
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

	    val okClicked: Boolean = MainApp.showItemEditDialog(null,"add")

    	if(okClicked){
    		//refrest the table data
    		RefreshItemlist()
    	}
	    

	}


}	
