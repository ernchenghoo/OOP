package Controllers

import MainSystem.MainApp
import Models.Account._

import scalafxml.core.macros.sfxml
import scalafx.scene.control._
import scalafx.scene.layout._


@sfxml
class MainMenuController (
	var mainMenuGrid: GridPane,
	var checkoutBtn : Button,
	var inventoryBtn : Button,
	var returnItemBtn : Button,
	var branchBtn : Button,
	var reportBtn : Button,
	var manageAccountBtn : Button,
) {	

	// MainApp.user.show(manageAccountBtn)

	def moveToCheckoutScreen () = {
		MainApp.goToCheckoutMenu()
	}  	

	def moveToMainMenu() = {
		MainApp.showMainMenu()
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

	def moveToManageAccount() =
	{
		MainApp.goToManageAccount()
	}

	def logout()
	{
		MainApp.user = new User()
		MainApp.goToLoginPage()
	}


}	
