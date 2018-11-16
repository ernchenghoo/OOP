package Models

import Database.myDBDetails

import java.sql.{Connection,DriverManager}
import java.sql.SQLException
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import scalafx.collections.ObservableBuffer



class Item(_id: Int,_name: String, _desc: String, _price: Double) extends myDBDetails{
	var id = ObjectProperty[Int](_id)
	var name = new StringProperty(_name)
	var desc = new StringProperty(_desc)
	var price = ObjectProperty[Double](_price)

	def save() : Boolean = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)
		
		var isSaveSuccess = true
		if (!(isExist)) {
			//if there error exist then its not save successfully
			try{
				//if not exist create new record to database
			
				//add the item to database
				val statement = connection.createStatement
				statement.executeUpdate(s"Insert into item Values(${id.getValue()},'${name.getValue()}','${desc.getValue()}',${price.getValue()})")	


				//new item added so need to initialize data stock to 0 in itemstock table
				//for all branch stock will have 0 stock of this newitem
				val queryresult = statement.executeQuery("select branchid from branch")
				while (queryresult.next){
					var branchid =  queryresult.getInt("branchid")
					val statement2 = connection.createStatement
					statement2.executeUpdate(s"Insert into itemstock values(${id.getValue()},${branchid},0)")
				}
			}catch{
				case e : SQLException =>
					isSaveSuccess = false
			}

		} else {
			//if there error exist then its not save successfully
			try{
				//if exist then update the data
				val statement = connection.createStatement
				statement.executeUpdate(s"Update item set itemname='${name.getValue()}', itemdesc='${desc.getValue()}', price=${price.getValue()} where itemid='${id.getValue()}'")	
			}catch{
				case e : SQLException =>
					isSaveSuccess = false
			}
			
		}
		connection.close()

		return isSaveSuccess
	}

	def delete() : Boolean = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		var isDeleteSuccessful = true
		if (isExist) {
			try{
				val statement = connection.createStatement
				//delete from item table
				statement.executeUpdate(s"Delete from item where itemid=${id.getValue()}")	


				//delete from itemstock
				val statement2 = connection.createStatement
				statement2.executeUpdate(s"Delete from itemstock where itemid=${id.getValue()}")	
			}catch{
				case e : SQLException =>
					isDeleteSuccessful = false
			}
			
		} else
			isDeleteSuccessful = false

		connection.close()

		return isDeleteSuccessful
	}

	def isExist : Boolean =  {
		val statement = connection.createStatement
		val queryresult = statement.executeQuery(s"select * from item where itemid=${id.getValue()}")

		var exist=false
		while (queryresult.next){
			exist = true
		}		

		return exist
	}
}

object Item extends myDBDetails{

	def initializeTable() = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		statement.executeUpdate(
			"CREATE TABLE item("+
  			"itemid int(10) NOT NULL,"+
  			"itemname varchar(100) NOT NULL,"+
  			"itemdesc varchar(500) NOT NULL,"+
  			"price decimal(12,2) NOT NULL,"+
  			"PRIMARY KEY (itemid)"+
			")"
		)	

		connection.close()
	}

	def getAllItems : ObservableBuffer[Item] = {

		var list:ObservableBuffer[Item] = new ObservableBuffer[Item]()

		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		val queryresult = statement.executeQuery("select * from item")		
		while (queryresult.next){
			var id = queryresult.getInt("itemid")
			var name = queryresult.getString("itemname")
			var desc = queryresult.getString("itemdesc")
			var price = queryresult.getDouble("price")

			list += new Item(id,name,desc,price)
		}	

		connection.close()

		return list
	}
}