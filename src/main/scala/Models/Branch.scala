package Models 

import Database.myDBDetails

import java.sql.{Connection,DriverManager}
import java.sql.SQLException
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import scalafx.collections.ObservableBuffer

class Branch(_branchid: Int, _location:String) extends myDBDetails{
	var branchid = ObjectProperty[Int](_branchid)
	var location = new StringProperty(_location)

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
				statement.executeUpdate(s"Insert into branch Values(${branchid.getValue()},'${location.getValue()}')")	


				//new branch added so need to initialize data stock to 0 in itemstock table
				//all item stock of new branch will have 0 stock 
				val queryresult = statement.executeQuery("select itemid from item")
				while (queryresult.next){
					var itemid =  queryresult.getInt("itemid")
					val statement2 = connection.createStatement
					statement2.executeUpdate(s"Insert into itemstock values(${itemid},${branchid.getValue()},0)")
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
				statement.executeUpdate(s"Update branch set location='${location}' where branchid='${branchid.getValue()}'")

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
				//delete from branch table
				val statement = connection.createStatement
				statement.executeUpdate(s"Delete from branch where branchid=${branchid.getValue()}")	


				//delete from itemstock
				val statement2 = connection.createStatement
				statement2.executeUpdate(s"Delete from itemstock where branchid=${branchid.getValue()}")	

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
		val queryresult = statement.executeQuery(s"select * from branch where branchid=${branchid.getValue()}")

		var exist=false
		while (queryresult.next){
			exist = true
		}		

		return exist
	}
}

object Branch extends myDBDetails{

	def initializeTable() = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		statement.executeUpdate(
			"CREATE TABLE branch(" +
  			"branchid int(10) NOT NULL," +
  			"location varchar(100) NOT NULL," +
  			"PRIMARY KEY (branchid)" +
			")"
		)	

		statement.executeUpdate(
			"""INSERT INTO `branch` VALUES (1,'Sunway'),(2,'Puchong'),(3,'Subang Jaya'),(4,'Putrajaya');"""
		)	
		

		connection.close()
	}

	def hasInitialize(): Boolean =
	{
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement		
		try 
		{
			var queryResult = statement.executeQuery("SELECT * from branch")
			true
		}
		catch 
		{
			case _ => false
		}
	}

	def getAllBranchs : ObservableBuffer[Branch] = {

		var list:ObservableBuffer[Branch] = new ObservableBuffer[Branch]()

		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		val queryresult = statement.executeQuery("select * from branch")		

		while (queryresult.next){
			var branchid = queryresult.getInt("branchid")
			var location = queryresult.getString("location")

			list += new Branch(branchid,location)
		}	


		connection.close()

		return list
	}

	def CheckBranchId(location:String): Int = {

		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		val queryresult = statement.executeQuery(s"select * from branch where location =('${location}')")		
		var idfound:Int = 0

		while (queryresult.next){
			idfound=queryresult.getInt("branchid")			
		}	

		connection.close()
		return idfound
	}
}