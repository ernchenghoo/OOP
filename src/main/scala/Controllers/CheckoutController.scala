package Controllers

import MainSystem.MainApp
import scalafx.scene.control._
import scalafxml.core.macros.sfxml
import scalafx.event._
import scalafx.beans.property.{StringProperty}
import javafx.{scene => jfxs}
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}

@sfxml
class CheckoutController (val checkout2: Button) {	
	def moveToMainMenu() = {
		MainApp.showPersonOverview()
	}
}	
