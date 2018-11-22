package Models.Account

import MainSystem.MainApp
import Database.myDBDetails

import scalafx.beans.property.{StringProperty, ObjectProperty, Property}
import java.sql.{Connection,DriverManager,SQLIntegrityConstraintViolationException}


import scalafx.scene.control._
import scalafx.collections.ObservableBuffer


case class UserInformation(name: String, age: String, gender: String, address: String, contact: String)

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


class Account(_userID: String, _username: String, _role: String, _name: String, _age: String, _gender: String, _address: String, _contact: String) extends myDBDetails
{

	Class.forName(driver)

	var userID = new StringProperty(_userID)
	var usernameAcc = new StringProperty(_username)
	var role = new StringProperty(_role)
	var name = new StringProperty(_name)
	var age = new StringProperty(_age)
	var gender = new StringProperty(_gender)
	var address = new StringProperty(_address)
	var contact = new StringProperty(_contact)

	def DeleteAccount() = 
	{
		connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = connection.createStatement
		try 
		{
			var queryResult = statement.executeUpdate(s"DELETE FROM account WHERE UserID = '${userID.value}'")
			if(queryResult > 0)
			{
				val alert = new Alert(Alert.AlertType.Information) 
		     	{
			        initOwner(MainApp.stage)
			        title = "Success"
			        headerText = "Account successfully deleted"
			    }.showAndWait()
			}
		} 
		catch 
		{
			case unknown:Throwable =>
				{
					val alert = new Alert(Alert.AlertType.Error) 
			     	{
				        initOwner(MainApp.stage)
				        title = "Error"
				        headerText = "Database error. Please contact administrator"
				        contentText = unknown.toString
				    }.showAndWait()			
				}
		}
		connection.close()		
	}	

	def UpdateAccount() =
	{
		connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = connection.createStatement
		try 
		{
			var queryResult = statement.executeUpdate(
				s"""UPDATE account SET 
				Role='${role.value}',
				Name='${name.value}',
				Age='${age.value}',
				Gender='${gender.value}',
				Address='${address.value}',
			    ContactNo='${contact.value}' 
			    WHERE UserID='${userID.value}'"""
			)
			if(queryResult > 0)
			{
				val alert = new Alert(Alert.AlertType.Information) 
		     	{
			        initOwner(MainApp.stage)
			        title = "Success"
			        headerText = "Account successfully updated"
			    }.showAndWait()	
			    MainApp.goToManageAccount()			
			}
		} 
		catch 
		{
			case unknown :Throwable =>
			{
				val alert = new Alert(Alert.AlertType.Error) 
		     	{
			        initOwner(MainApp.stage)
			        title = "Error"
			        headerText = "Database error. Please contact administrator"
			        contentText = unknown.toString
			    }.showAndWait()				
			}
		}		
		connection.close()
	}

}

object Account extends myDBDetails
{
	var accountList: ObservableBuffer[Account] = new ObservableBuffer[Account]	
	Class.forName(driver)

	def setupAccountTable() = 
	{			
		connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = connection.createStatement

		if(!hasInitialize())
		{
			try 
			{
				initializeTable()
			}
			catch 
			{
				case unknown: Throwable => 
				{
					val alert = new Alert(Alert.AlertType.Error) 
			     	{
				        initOwner(MainApp.stage)
				        title = "Error"
				        headerText = "Account table initialization failed. Resetting table"
				    }.showAndWait()
					statement.executeUpdate("DROP TABLE IF EXISTS account")
					initializeTable()
				}			
			}
		}
	}

	def initializeTable() =
	{
		connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = connection.createStatement

		statement.executeUpdate(
			"""CREATE TABLE account (
			UserID IDENTITY(1001,1),
			Username varchar(200) DEFAULT NULL,
			Password varchar(200) DEFAULT NULL,
			Role varchar(200) DEFAULT NULL,
			Name varchar(200) DEFAULT NULL,
			Age int(50) DEFAULT NULL,
			Gender varchar(200) DEFAULT NULL,
			Address varchar(200) DEFAULT NULL,
		 	ContactNo varchar(200) DEFAULT NULL,
			UNIQUE KEY Username_UNIQUE (Username))"""
		)

		statement.executeUpdate(
			"""INSERT INTO account (Username,Password,Role,Name,Age,Gender,Address,ContactNo) 
			VALUES 
			('admin','admin','Admin','admin',20,'Male','admin','admin'),
			('cashier','cashier','Cashier','cashier',10,'Male','cashier','cashier'),
			('stockkeeper','stockkeeper','Stock Keeper','stockkeeper',10,'Male','stockkeeper','stockkeeper'),
			('manager','manager','Manager','manager',12,'Female','manager','manager')"""
		)

		// statement.executeUpdate("ALTER TABLE account AUTO_INCREMENT=1005")		
		connection.close()
	}

	def hasInitialize(): Boolean =
	{
		connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)

		val statement = connection.createStatement		
		try 
		{
			var queryResult = statement.executeQuery("SELECT * from account WHERE UserID = 1001")
			if(queryResult.next) true else false
		}
		catch 
		{
			case _ => false
		}
	}

	def CreateAccount(
		username: String, 
		password: String, 
		role: String, 
		details: UserInformation
	) =
	{
		connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = connection.createStatement
		val queryResult = statement.executeQuery(s"SELECT username FROM account where Username='$username'")
		//check duplicate username
		if(queryResult.next)
		{
			val alert = new Alert(Alert.AlertType.Error) 
	     	{
		        initOwner(MainApp.stage)
		        title = "Error"
		        headerText = "Username is taken, please insert a new username."
		    }.showAndWait()
		}
		else
		{
			try 
			{
				val queryResult = statement.executeUpdate(s"INSERT INTO account (Username,Password,Role,Name,Age,Gender,Address,ContactNo) VALUES ('$username', '$password', '$role', '${details.name}', ${details.age}, '${details.gender}', '${details.address}', '${details.contact}' )")
				if(queryResult > 0)
				{
					val alert = new Alert(Alert.AlertType.Information) 
			     	{
				        initOwner(MainApp.stage)
				        title = "Success"
				        headerText = "Account successfully created"
				    }.showAndWait()
				}
				MainApp.goToManageAccount()
			} catch 
			{
				case unknown: Throwable => 
				{
					val alert = new Alert(Alert.AlertType.Error) 
			     	{
				        initOwner(MainApp.stage)
				        title = "Error"
				        headerText = "Database error. Please contact administrator"
				        contentText = unknown.toString
				    }.showAndWait()
				}
			}
		}
		
		connection.close()
	}


	def Authentication(username: String, password: String) = 
	{
		connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = connection.createStatement
		var queryResult = statement.executeQuery(s"SELECT * FROM account WHERE Username='$username' AND Password='$password'")
		if(queryResult.next)
		{

			var details = UserInformation(queryResult.getString("Name"),queryResult.getString("Age"),queryResult.getString("Gender"),queryResult.getString("Address"),queryResult.getString("ContactNo"))
			queryResult.getString("Role").trim match 
			{
				case "Cashier" =>
				{
					MainApp.user = new Cashier(queryResult.getString("UserID"),queryResult.getString("Username"),queryResult.getString("Role"), details)
				}

				case "Stock Keeper" =>
				{
					MainApp.user = new StockKeeper(queryResult.getString("UserID"),queryResult.getString("Username"),queryResult.getString("Role"), details)

				}

				case "Manager" =>
				{
					MainApp.user = new Manager(queryResult.getString("UserID"),queryResult.getString("Username"),queryResult.getString("Role"), details)

				}

				case "Admin" =>
				{
					MainApp.user = new Admin(queryResult.getString("UserID"),queryResult.getString("Username"),queryResult.getString("Role"), details)
				}

				case _ =>
				{
					val alert = new Alert(Alert.AlertType.Error) 
			     	{
				        initOwner(MainApp.stage)
				        title = "Error"
				        headerText = "Role not assigned for this account. Please contact administrator."
				    }.showAndWait()					
				}
			}
			MainApp.showMainMenu()
		}
		else
		{
			val alert = new Alert(Alert.AlertType.Error) 
	     	{
		        initOwner(MainApp.stage)
		        title = "Error"
		        headerText = "Invalid credentials, please try again."
		    }.showAndWait()
		}
		connection.close()
	}

	def GetAllAccount() =
	{	
		accountList.clear()
		connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = connection.createStatement
		var queryResult = statement.executeQuery("SELECT UserID,Username,Role,Name,Age,Gender,Address,ContactNo FROM account")
		while(queryResult.next)
		{
			var userID = queryResult.getString("UserID")
			var username = queryResult.getString("Username")
			var role = queryResult.getString("Role")
			var name = queryResult.getString("Name")
			var age = queryResult.getString("Age")
			var gender =queryResult.getString("Gender")
			var address = queryResult.getString("Address")
			var contact = queryResult.getString("ContactNo")

			accountList += new Account(userID, username, role, name, age, gender , address, contact)
		}
		connection.close()
		
	}


}

