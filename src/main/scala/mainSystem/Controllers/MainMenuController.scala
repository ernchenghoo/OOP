package mainSystem.Controllers
import mainSystem.MainApp
import scalafx.scene.control._
import scalafxml.core.macros.sfxml
import scalafx.event._
import scalafx.beans.property.{StringProperty}
import javafx.{scene => jfxs}
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}

@sfxml
class MainMenuController (val checkout2: Button) {	
	def moveToCheckoutScreen () = {
		mainSystem.MainApp.goToCheckoutMenu()
	}  	

	def moveToMainMenu() = {
		mainSystem.MainApp.showPersonOverview()
	}
}	
