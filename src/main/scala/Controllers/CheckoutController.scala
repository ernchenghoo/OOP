package Controllers
import Database.InventoryDB
import MainSystem.MainApp
import scalafx.scene.control._
import scalafxml.core.macros.sfxml
import scalafx.event._
import scalafx.beans.property.{StringProperty}
import javafx.{scene => jfxs}
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}

@sfxml
class CheckoutController (
	private val connectDbButton: Button,
	private val checkout2: Button,
	private val firstID : Label,
    private val firstName : Label,
    private val firstPrice : Label,
    private val secondID : Label,
    private val secondName :  Label,
    private val secondPrice : Label,
    private val findItemTextbox: TextField
	) {	
		val inventoryDBClass = new InventoryDB() 

		def moveToMainMenu() = {
			MainApp.showPersonOverview()
		}
		def checkoutItem = {			
			var itemExists = inventoryDBClass.checkItemExistence(findItemTextbox.text.value)
			if (itemExists)
				firstID.setVisible(true)
				firstID.text.value = findItemTextbox.text.value
		}




}	
