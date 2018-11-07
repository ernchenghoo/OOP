package Controllers

import MainSystem.MainApp
import Database.InventoryDatabase

import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}

@sfxml
class InventorymainController (val anchorpanetab: AnchorPane) {	
	
	
	def backtoMainmenu() = {
		MainApp.showPersonOverview()

		InventoryDatabase.UpdateItemlist()
	}

	def changeitemcontrol() = {
		val resource = getClass.getResource("/Views/Inventory/Itemcontrol.fxml")
    	val loader = new FXMLLoader(resource, NoDependencyResolver)

    	loader.load()

    	val itemcontrolroot = loader.getRoot[jfxs.layout.AnchorPane]

		anchorpanetab.getChildren().clear()
        anchorpanetab.getChildren().add(itemcontrolroot)

	}
}	
