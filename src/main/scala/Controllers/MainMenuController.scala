package Controllers

import MainSystem.MainApp
import scalafx.scene.control._
import scalafxml.core.macros.sfxml
import scalafx.event._
import scalafx.beans.property.{StringProperty}
import javafx.{scene => jfxs}
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import java.sql.{Connection,DriverManager}

@sfxml
class MainMenuController (val checkout2: Button) {	
	def moveToCheckoutScreen () = {
		MainApp.goToCheckoutMenu()
	}  	

	def moveToMainMenu() = {
		MainApp.showPersonOverview()
	}

	def movetotest() = {
		MainApp.goToInventoryMain()
	}
}	
