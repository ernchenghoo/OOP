package Models.Account

// import Controllers.MainMenuController
import MainSystem.MainApp
import scalafx.beans.property.{StringProperty, ObjectProperty, Property}
// import scalafx.scene.control._
// import scalafx.scene.layout._

case class UserInformation(name: String, age: String, gender: String, address: String, contact: String)


class UserProperty(_userID: String, _username: String, _role: String, _name: String, _age: String, _gender: String, _address: String, _contact: String)
{
	var userID = new StringProperty(_userID)
	var username = new StringProperty(_username)
	var role = new StringProperty(_role)
	var name = new StringProperty(_name)
	var age = new StringProperty(_age)
	var gender = new StringProperty(_gender)
	var address = new StringProperty(_address)
	var contact = new StringProperty(_contact)

}


class User(val userID: String ="", val username: String = "", val role: String = "", val details: UserInformation = null)
{
	

	def show(control: Controllers.MainMenuController#Controller)
	{
		
	}

	def arrange(control: Controllers.MainMenuController#Controller)
	{

	}

	def initialize(control: Controllers.MainMenuController#Controller)
	{
		show(control)
		arrange(control)
	}
}

class Cashier(_userID: String, _username: String, _role: String, _details: UserInformation) extends User(_userID, _username, _role, _details)
{
	override def show(control : Controllers.MainMenuController#Controller)
	{
		control.checkoutBtn.visible = true
	}

	override def arrange(control: Controllers.MainMenuController#Controller)
	{
		control.mainMenuGrid.add(control.checkoutBtn, 0,0)

	}

}	

class StockKeeper (_userID: String, _username: String, _role: String, _details: UserInformation) extends User(_userID, _username, _role, _details)
{
	override def show(control : Controllers.MainMenuController#Controller)
	{
		control.returnItemBtn.visible = true
		control.inventoryBtn.visible = true
		control.branchBtn.visible = true
		control.reportBtn.visible = true
	}

	override def arrange(control: Controllers.MainMenuController#Controller)
	{
		control.mainMenuGrid.add(control.returnItemBtn, 0, 0)
		control.mainMenuGrid.add(control.inventoryBtn, 1, 0)
		control.mainMenuGrid.add(control.branchBtn, 2, 0)
		control.mainMenuGrid.add(control.reportBtn, 0, 1)
	}
}

class Manager (_userID: String, _username: String, _role: String, _details: UserInformation) extends User(_userID, _username, _role, _details)
{
	override def show(control : Controllers.MainMenuController#Controller)
	{
		control.checkoutBtn.visible = true
		control.returnItemBtn.visible = true
		control.inventoryBtn.visible = true
		control.branchBtn.visible = true
		control.reportBtn.visible = true		
	}

	override def arrange(control: Controllers.MainMenuController#Controller)
	{
		control.mainMenuGrid.add(control.checkoutBtn, 0, 0)
		control.mainMenuGrid.add(control.returnItemBtn, 1, 0)
		control.mainMenuGrid.add(control.inventoryBtn, 2, 0)
		control.mainMenuGrid.add(control.branchBtn, 0, 1)
		control.mainMenuGrid.add(control.reportBtn, 1, 1)
	}

}

class Admin (_userID: String, _username: String, _role: String, _details: UserInformation) extends User(_userID, _username, _role, _details)
{
	
	override def show(control : Controllers.MainMenuController#Controller)
	{
		control.checkoutBtn.visible = true		
		control.returnItemBtn.visible = true
		control.inventoryBtn.visible = true
		control.branchBtn.visible = true
		control.reportBtn.visible = true	
		control.manageAccountBtn.visible = true	
	}

	override def arrange(control: Controllers.MainMenuController#Controller)
	{
		control.mainMenuGrid.add(control.checkoutBtn, 0 , 0)
		control.mainMenuGrid.add(control.returnItemBtn, 1, 0)
		control.mainMenuGrid.add(control.inventoryBtn, 2, 0)
		control.mainMenuGrid.add(control.branchBtn, 0, 1)
		control.mainMenuGrid.add(control.reportBtn, 1, 1)
		control.mainMenuGrid.add(control.manageAccountBtn, 2, 1)

	}


}