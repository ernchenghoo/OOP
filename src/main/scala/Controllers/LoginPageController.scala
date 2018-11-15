package Controllers

import Models.Account
import Database.{myDBDetails, AccountDatabase}
import MainSystem.{MainApp,Utility}
import scalafxml.core.macros.sfxml

import scalafx.scene.control._

@sfxml
class LoginPageController(val username: TextField, val password: PasswordField )
{
	def Login() =
	{
		var err = ""

		if(Utility.nullChecking(username.text.value))
		{
			err += "Username is required \n"
		}
		if(Utility.nullChecking(password.text.value))
		{
			err += "Password is required \n"
		}

		if(err.length == 0)
		{
			AccountDatabase.Authentication(username.text.value, password.text.value)
		}
		else
		{
			val alert = new Alert(Alert.AlertType.Error) 
	     	{
		        initOwner(MainApp.stage)
		        title = "Invalid Fields"
		        headerText = "Please rectify invalid fields"
		        contentText = err
		    }.showAndWait()
		}
	}

	def moveToMainMenu() = {
			MainApp.showMainMenu()
	}		
}