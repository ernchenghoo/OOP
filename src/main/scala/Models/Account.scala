package Models.Account

import scalafx.beans.property.{StringProperty, ObjectProperty, Property}

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


class User(val userID: String, val username: String, val role: String, val details: UserInformation)

class Cashier (_userID: String, _username: String, _role: String, _details: UserInformation) extends User(_userID, _username, _role, _details)

class StockKeeper (_userID: String, _username: String, _role: String, _details: UserInformation) extends User(_userID, _username, _role, _details)

class Manager (_userID: String, _username: String, _role: String, _details: UserInformation) extends User(_userID, _username, _role, _details)

class Admin (_userID: String, _username: String, _role: String, _details: UserInformation) extends User(_userID, _username, _role, _details)