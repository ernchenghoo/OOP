package Controllers

import Database.{myDBDetails, AccountDatabase}
import MainSystem.MainApp
import Models.Account._

import scalafxml.core.macros.sfxml
import scalafx.scene.control._
import scalafx.Includes._


import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary


@sfxml
class ManageAccountController
(
	accountTableView: TableView[UserProperty],
	userIDTableColumn: TableColumn[UserProperty, String],
	usernameTableColumn: TableColumn[UserProperty, String],
	roleTableColumn: TableColumn[UserProperty, String],
	nameTableColumn: TableColumn[UserProperty, String],
	ageTableColumn: TableColumn[UserProperty, String],
	genderTableColumn: TableColumn[UserProperty, String],
	addressTableColumn: TableColumn[UserProperty, String],
	contactTableColumn: TableColumn[UserProperty, String],
)
{

	loadAccountTable()

	def loadAccountTable() =
	{
		AccountDatabase.GetAllAccount()

	  	accountTableView.items = AccountDatabase.AccountList

		userIDTableColumn.cellValueFactory = {_.value.userID}
		usernameTableColumn.cellValueFactory = {_.value.username}
		roleTableColumn.cellValueFactory = {_.value.role}
		nameTableColumn.cellValueFactory = {_.value.name}
		ageTableColumn.cellValueFactory = {_.value.age}
		genderTableColumn.cellValueFactory = {_.value.gender}
		addressTableColumn.cellValueFactory = {_.value.address}
		contactTableColumn.cellValueFactory = {_.value.contact}

	}

	def createAction()
	{
	   
    	MainApp.goToAccountPage(0, "Create")
	}
	

	def editAction()
	{
	    val selectedIndex = accountTableView.selectionModel().selectedIndex.value
	    if (selectedIndex >= 0) 
	    {
	    	
	    	MainApp.goToAccountPage(selectedIndex, "Edit")

	    } 
	    else 
	    {
	      // Nothing selected.
	      val alert = new Alert(AlertType.Error){
	        initOwner(MainApp.stage)
	        title       = "No Selection"
	        headerText  = "No Item Selected"
	        contentText = "Please select a item in the table."
	      }.showAndWait()
	    }
	}
	

	def deleteAction()
	{
	    val selectedIndex = accountTableView.selectionModel().selectedIndex.value
	    if (selectedIndex >= 0) 
	    {
	    	var selectedAccount = AccountDatabase.AccountList.get(selectedIndex)
	    	var selectedUserID = selectedAccount.userID.getValue()

	    	AccountDatabase.DeleteAccount(selectedUserID)
	     	loadAccountTable()
	    } 
	    else 
	    {
	      // Nothing selected.
	      val alert = new Alert(AlertType.Error){
	        initOwner(MainApp.stage)
	        title       = "No Selection"
	        headerText  = "No Item Selected"
	        contentText = "Please select a item in the table."
	      }.showAndWait()
	    }
	}


	def moveToMainMenu() = {
		MainApp.showMainMenu()
	}			
}