package Controllers

import MainSystem.MainApp
import Models.Item

import scalafx.scene.control.{Alert,TableColumn,TableView,TableCell}
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.stage.{Modality, Stage}
import scalafx.event.ActionEvent
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary
import scalafx.collections.ObservableBuffer

@sfxml
class ItemcontrolController (
	val itemtableview: TableView[Item],
	val itemidColumn: TableColumn[Item, Int],
	val itemnameColumn: TableColumn[Item, String],
	val itemdescColumn: TableColumn[Item, String],
	val priceColumn: TableColumn[Item, Double]
	) {	
	
	//get data from sql and show to table
	var ItemData:ObservableBuffer[Item] = new ObservableBuffer[Item]() 

	ItemData ++= Item.getAllItems
	RefreshItemlist()

	def RefreshItemlist() = {
		
		//show to tableview
		itemtableview.items = ItemData

		// initialize columns's cell values
	  	itemidColumn.cellValueFactory = {_.value.id}
	  	itemnameColumn.cellValueFactory = {_.value.name}
	  	itemdescColumn.cellValueFactory = {_.value.desc}
	  	priceColumn.cellValueFactory = {_.value.price}
	}

	def handleDeleteItem(action : ActionEvent) = {
	    val selectedIndex = itemtableview.selectionModel().selectedIndex.value
	    if (selectedIndex >= 0) {
	    	var selecteditem:Item = ItemData.get(selectedIndex)
	    	
	    	//if selecteditem().delete return true means delete from database successfully
	    	if (selecteditem.delete()){
	    		itemtableview.items().remove(selectedIndex)
	    	}else{
	    		val alert = new Alert(AlertType.Error){
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

	def handleEditItem(action : ActionEvent) = {

	    val selectedIndex = itemtableview.selectionModel().selectedIndex.value
	    if (selectedIndex >= 0) {
	    	var selecteditem:Item = ItemData.get(selectedIndex)

	    	val okClicked: Boolean = MainApp.showItemEditDialog(selecteditem,"edit")

	    	if(okClicked){
	    		//if selecteditem.save() return true means successfully save data to database
	    		if(selecteditem.save()){
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

	def handleAddItem(action : ActionEvent) = {
		var newitem = new Item(0,"","",0)
	    val okClicked: Boolean = MainApp.showItemEditDialog(newitem,"add")

    	if(okClicked){
    		if(newitem.save()){
    			ItemData += newitem
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
