package Database

import MainSystem.MainApp
import Database.myDBDetails
import Models.Account._

import java.sql.{Connection,DriverManager,SQLIntegrityConstraintViolationException}

import scalafx.scene.control._
import scalafx.collections.ObservableBuffer


object AccountDatabase
{

	// var AccountList: ObservableBuffer[User] = new ObservableBuffer[User]
	var AccountList: ObservableBuffer[UserProperty] = new ObservableBuffer[UserProperty]

	Class.forName(myDBDetails.driver)

	def CreateAccount(
		username: String, 
		password: String, 
		role: String, 
		details: UserInformation
	) =
	{
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
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
				val queryResult = statement.executeUpdate(s"INSERT INTO account VALUES (0,'$username', '$password', '$role', '${details.name}', ${details.age}, '${details.gender}', '${details.address}', '${details.contact}' )")
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
		
		myDBDetails.connection.close()
	}


	def Authentication(username: String, password: String) =
	{
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
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
		myDBDetails.connection.close()

	}

	def GetAllAccount() =
	{	
		AccountList.clear()
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
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

			AccountList += new UserProperty(userID, username, role, name, age, gender , address, contact)
		}
		myDBDetails.connection.close()
	}

	def DeleteAccount(userID: String) = 
	{
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		try 
		{
			var queryResult = statement.executeUpdate(s"DELETE FROM account WHERE UserID = '$userID'")
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
		myDBDetails.connection.close()		
	}

	def UpdateAccount
	(
		userID: String,
		role: String, 
		details: UserInformation
	) =
	{
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		try 
		{
			var queryResult = statement.executeUpdate(s"UPDATE account SET Role='$role', Name='${details.name}', Age='${details.age}', Gender='${details.gender}', Address='${details.address}', ContactNo='${details.contact}' WHERE UserID=$userID")
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
		myDBDetails.connection.close()
	}
}