package Controllers

import Database.myDBDetails
import MainSystem.{MainApp, Utility}
import Models.Account._

import scalafxml.core.macros.sfxml
import scalafx.scene.control._
import scalafx.collections.ObservableBuffer
@sfxml
class AccountPageController(
	val username: TextField,
	val password: PasswordField,
	val cpassword: PasswordField,
	val role: ComboBox[String],
	val name: TextField,
	val age: TextField,
	val gender: ComboBox[String],
	val address: TextField,
	val contact: TextField,
	val doubleActionButton: Button 
	)
{

	
	var selectedIndex = 0;
	var action = "";


	role.items = ObservableBuffer[String]("Cashier","Stock Keeper", "Manager", "Admin")
	role.selectionModel().selectFirst()

	gender.items = ObservableBuffer[String]("Male", "Female")
	gender.selectionModel().selectFirst()

	
	def loadData() = 
	{
		var selectedAccount = Account.accountList.get(selectedIndex)
		username.text.value = selectedAccount.usernameAcc.getValue()
		username.disable = true
		password.text.value = "dummy"
		cpassword.text.value = "dummy"
		password.disable = true
		cpassword.disable = true
		role.value.value = selectedAccount.role.getValue()
		name.text.value = selectedAccount.name.getValue()
		age.text.value = selectedAccount.age.getValue()
		gender.value.value = selectedAccount.gender.getValue()
		address.text.value = selectedAccount.address.getValue()
		contact.text.value = selectedAccount.contact.getValue()		

	}


	def doubleAction() =
	{	

		if(isNotNull())
		{

			if(password.text.value.trim == cpassword.text.value.trim)
			{
				if(action == "Create")
				{	

					Account.CreateAccount(username.text.value.trim, password.text.value.trim, role.value.value, new UserInformation(name.text.value.trim, age.text.value.trim, gender.value.value.trim, address.text.value.trim, contact.text.value.trim) )
				}
				else
				{

					var account = Account.accountList.get(selectedIndex)
					account.role.value = role.value.value
					account.name.value = name.text.value
					account.age.value = age.text.value
					account.gender.value = gender.value.value
					account.address.value = address.text.value
					account.contact.value = contact.text.value
					account.UpdateAccount()

				}
			}
			else
			{
				
				val alert = new Alert(Alert.AlertType.Error) 
		     	{
			        initOwner(MainApp.stage)
			        title = "Invalid Fields"
			        headerText = "Please rectify invalid fields"
			        contentText = "Password and Confirm Password does not match"
			    }.showAndWait()			
			}
		}

	}

	def isNotNull(): Boolean =
	{
		var err: String = ""
		var err2: String = ""

		if(Utility.nullChecking(username.text.value.trim))
		{
			err += "Username, "
		}
		if(Utility.nullChecking(password.text.value.trim))
		{
			err += "Password, "
		}
		if(Utility.nullChecking(cpassword.text.value.trim))
		{
			err += "Confirm Password, "
		}
		if(Utility.nullChecking(role.value.value.trim))
		{
			err += "Role, "
		}
		if(Utility.nullChecking(name.text.value.trim))
		{
			err += "Name, "
		}
		if(Utility.nullChecking(age.text.value.trim))
		{
			err += "Age, "
		}
		else
		{
			try 
			{
		        Integer.parseInt(age.text.value.trim);
		    } 
		    catch 
		    {
		    	case e: NumberFormatException =>
		    		{
		       		 	err2 += "Invalid entry of Age, number ONLY"
		    		}
		    }			
		}
		if(Utility.nullChecking(gender.value.value.trim))
		{
			err += "Gender, "
		}
		if(Utility.nullChecking(address.text.value.trim))
		{
			err += "Address, "
		}
		if(Utility.nullChecking(contact.text.value.trim))
		{
			err += "Contact, "
		}

		if(err.length == 0 && err2.length == 0)
		{
			true
		}
		else
		{
			if (err.length != 0) err = err.trim.substring(0, err.trim.length - 1) + " is required\n" 
			err += err2
			val alert = new Alert(Alert.AlertType.Error) 
	     	{
		        initOwner(MainApp.stage)
		        title = "Invalid Fields"
		        headerText = "Please rectify invalid fields"
		        contentText = err
		    }.showAndWait()			
		    false
		}

	}

	def backAction() = {
		MainApp.goToManageAccount()
	}	


}