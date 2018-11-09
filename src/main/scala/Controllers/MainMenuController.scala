package Controllers
import MainSystem.MainApp

import scalafxml.core.macros.sfxml


@sfxml
class MainMenuController () {	
	def moveToCheckoutScreen () = {
		MainApp.goToCheckoutMenu()
	}  	

	
}	
