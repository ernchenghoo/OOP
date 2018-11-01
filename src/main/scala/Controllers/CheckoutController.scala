package Controllers
import Database.InventoryDB
import MainSystem.MainApp
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.event._
import scalafx.beans.property.{StringProperty}
import javafx.{scene => jfxs}
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}

@sfxml
class CheckoutController (
	private val connectDbButton: Button,
	private val checkout2: Button,	
    private val findItemTextbox: TextField,
    private val checkoutGrid: GridPane,
	) {	
		val inventoryDBClass = new InventoryDB() 

		def moveToMainMenu() = {
			MainApp.showPersonOverview()
		}
		
		def checkoutItem = {				
			var itemExists = inventoryDBClass.checkItemExistence(findItemTextbox.text.value)						
			if (itemExists) {
				checkoutGrid.add(new Label(findItemTextbox.text.value),0,1)
			}
		}
		




}	
