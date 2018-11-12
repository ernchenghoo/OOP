package Controllers
import MainSystem.MainApp

import scalafxml.core.macros.sfxml


@sfxml
class MainMenuController () {	
	def moveToCheckoutScreen () = {
		MainApp.goToCheckoutMenu()
	}  	

	
	def moveToMainMenu() = {
		MainApp.showPersonOverview()
	}

	def movetoInventory() = {
		MainApp.goToInventoryMain()
	}

	def movetoReturnItem() = {
		MainApp.goToReturnItem()
	}

	def movetoBranch() = {
		MainApp.goToBranchMain()
	}

	def moveToSearchReport() = {
		MainApp.goToSearchReport()
	}
}	
