package Models

import Database.myDBDetails
import Models.Branch

import java.sql.{Connection,DriverManager}
import java.sql.SQLException
import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import scalafx.collections.ObservableBuffer


class Stockedithistory( _stockeditid: Int, _date: Timestamp, _itemid: Int, _itemnamefromtable:String,  _branchid: Int, _branchlocationfromtable:String, val _amount: Int,val _description:String) extends myDBDetails{
	var stockeditid = ObjectProperty[Int](_stockeditid)
	var date = ObjectProperty[Timestamp](_date)
	var itemid = ObjectProperty[Int](_itemid)
	var itemnamefromtable = new StringProperty(_itemnamefromtable)
	var branchid = ObjectProperty[Int](_branchid)
	var branchlocationfromtable = new StringProperty(_branchlocationfromtable)
	var amount = ObjectProperty[Int](_amount)
	var description = new StringProperty(_description)

	def DatetoString():StringProperty = {
		var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		var stringdate = date.getValue().toLocalDateTime().format(formatter)
		new StringProperty(stringdate)
	}

	def getItemname():StringProperty = {
		var itemname = new StringProperty("ItemID not found") 

		//get the lastest itemname from itemID
		for(item <- Item.getAllItems){
			if(item.id.getValue() == itemid.getValue())
				itemname = item.name
		}

		//if ItemID still available in item table get lastest itemname
		//if string still is itemid not found the use then itemname from stockedithistory table
		if(itemname.getValue() == "ItemID not found")
			return itemnamefromtable
		else
			return itemname
	}

	def getBranchlocation():StringProperty = {
		var branchlocation = new StringProperty("BranchID not found") 

		for(branch <- Branch.getAllBranchs){
			if(branch.branchid.getValue() == branchid.getValue())
				branchlocation = branch.location
		}

		//if branchID still available in branch table get lastest branchlocation
		//if string still is BranchID not found then use the branchlocation from stockedithistory table
		if(branchlocation.getValue() == "BranchID not found")
			return branchlocationfromtable
		else
			return branchlocation
	}
}

object Stockedithistory extends myDBDetails{

	def initializeTable() = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		statement.executeUpdate(
			"CREATE TABLE `stockedithistory` (" +
			"`stockeditid` int(10) NOT NULL," +
			"`date` timestamp NOT NULL," +
			"`itemid` int(10) NOT NULL," +
			"`itemname` varchar(100) NOT NULL," +
			"`branchid` int(10) NOT NULL," +
			"`branchlocation` varchar(100) NOT NULL," +
			"`amount` bigint(20) NOT NULL," +
			"`description` varchar(500) NOT NULL," +
			"PRIMARY KEY (`stockeditid`)" +
			")"
		)	

		statement.executeUpdate(
			"""INSERT INTO `stockedithistory` VALUES 
			(1,'2018-11-11 23:57:58',1,'Keyboard',1,'Sunway',60,'Bought Stock from ABC Sdn Bhd');"""
		)	
		

		connection.close()
	}

	def hasInitialize(): Boolean =
	{
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement		
		try 
		{
			var queryResult = statement.executeQuery("SELECT * from stockedithistory")
			true
		}
		catch 
		{
			case _ => false
		}
	}

	def getAllStockedithistorys : ObservableBuffer[Stockedithistory] = {

		var list:ObservableBuffer[Stockedithistory] = new ObservableBuffer[Stockedithistory]()

		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		val queryresult = statement.executeQuery("select * from stockedithistory")		

		while (queryresult.next){
			var stockeditid = queryresult.getInt("stockeditid")
			var date = queryresult.getTimestamp("date")
			var itemid = queryresult.getInt("itemid")
			var itemname = queryresult.getString("itemname")
			var branchid = queryresult.getInt("branchid")
			var branchlocation = queryresult.getString("branchlocation")
			var amount = queryresult.getInt("amount")
			var desc = queryresult.getString("description")

			list += new Stockedithistory(stockeditid,date,itemid,itemname,branchid,branchlocation,amount,desc)
		}	

		connection.close()

		return list
	}

	def getstock(itemid: Int, branchid: Int): Int = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)
		
		val statement = connection.createStatement


		//check if stock is sufficient
		val queryresult = statement.executeQuery(s"select numofstock from itemstock where itemid=${itemid} and branchid=${branchid}")		

		var numofstock = 0
		while (queryresult.next){
			numofstock = queryresult.getInt("numofstock")
		}		

		connection.close()

		numofstock
	}

	def addStock(stockeditid: Int, date :String, itemid: Int, itemname: String, branchid: Int, branchlocation:String, amount: Int, desc: String):Boolean = {
		var isAddedSuccessful = false
		try {
			Class.forName(driver)
			connection = DriverManager.getConnection(url, username, password)
			
			val statement = connection.createStatement

			//Update the Stock
			val queryresult = statement.executeQuery(s"select numofstock from itemstock where itemid=${itemid} and branchid=${branchid}")		

			var numofstock = 0
			while (queryresult.next){
				numofstock = queryresult.getInt("numofstock")
			}

			numofstock += amount
			statement.executeUpdate(s"Update itemstock set numofstock=${numofstock} where itemid=${itemid} and branchid=${branchid}")	

			//add record to edit history
			statement.executeUpdate(s"Insert into stockedithistory Values(${stockeditid},'${date}',${itemid},'${itemname}',${branchid},'${branchlocation}',${amount},'${desc}')")	

			connection.close()
		}catch{
			case e:SQLException=>
				isAddedSuccessful = false
		}

		return isAddedSuccessful
		
	}

	def minusStock(stockeditid: Int, date :String, itemid: Int, itemname:String, branchid: Int, branchlocation:String, amount: Int, desc: String):Boolean = {
		var isMinusSuccessful = false

		try{
			Class.forName(driver)
			connection = DriverManager.getConnection(url, username, password)
			
			val statement = connection.createStatement

			//Update the Stock
			val queryresult = statement.executeQuery(s"select numofstock from itemstock where itemid=${itemid} and branchid=${branchid}")		

			var numofstock = 0
			while (queryresult.next){
				numofstock = queryresult.getInt("numofstock")
			}

			numofstock -= amount
			statement.executeUpdate(s"Update itemstock set numofstock=${numofstock} where itemid=${itemid} and branchid=${branchid}")	

			//add record to edit history
			var minusamount = amount * -1
			statement.executeUpdate(s"Insert into stockedithistory Values(${stockeditid},'${date}',${itemid},'${itemname}',${branchid},'${branchlocation}',${minusamount},'${desc}')")	

			connection.close()
		}catch{
			case e:SQLException=>
				isMinusSuccessful = false
		}

		return isMinusSuccessful
	}


}