package Controllers

import MainSystem.MainApp
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.stage.{Modality, Stage}
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}

@sfxml
class InventorymainController (val anchorpanetab: AnchorPane) {	


	//initial select itemcontrol 
	changeitemcontrol()

	def backtoMainmenu() = {
		MainApp.showMainMenu()

	}

	def changeitemcontrol() = {
		val resource = getClass.getResource("/Views/Inventory/Itemcontrol.fxml")
    	val loader = new FXMLLoader(resource, NoDependencyResolver)

    	loader.load()

    	val itemcontrolroot = loader.getRoot[jfxs.layout.AnchorPane]

		anchorpanetab.getChildren().clear()
        anchorpanetab.getChildren().add(itemcontrolroot)

	}

	def changestockcontrol() = {
		val resource = getClass.getResource("/Views/Inventory/StockControl.fxml")
    	val loader = new FXMLLoader(resource, NoDependencyResolver)

    	loader.load()

    	val stockcontrolroot = loader.getRoot[jfxs.layout.AnchorPane]

		anchorpanetab.getChildren().clear()
        anchorpanetab.getChildren().add(stockcontrolroot)

	}

	def changestockcheck() = {
		val resource = getClass.getResource("/Views/Inventory/StockCheck.fxml")
    	val loader = new FXMLLoader(resource, NoDependencyResolver)

    	loader.load()

    	val stockcheckroot = loader.getRoot[jfxs.layout.AnchorPane]

		anchorpanetab.getChildren().clear()
        anchorpanetab.getChildren().add(stockcheckroot)

	}
}	
