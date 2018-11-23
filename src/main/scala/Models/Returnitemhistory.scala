package Models 

import Database.myDBDetails
import MainSystem.MainApp
//import Models.Item
//import Models.returnitemhistory
//import Models.Branch
//import Models.Itemstock
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

import java.sql.{Connection,DriverManager}
import java.sql.SQLException
import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import scalafx.collections.ObservableBuffer

class Returnitemhistory( _returnitemid: Int, _date: Timestamp, _salesid: Int, _itemid: Int, _itemnamefromtable:String,  _branchid: Int, _branchlocationfromtable:String, val _amount: Int,val _description:String){
	var returnitemid = ObjectProperty[Int](_returnitemid)
	var date = ObjectProperty[Timestamp](_date)
	var salesid = ObjectProperty[Int](_salesid)
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


object Returnitemhistory extends myDBDetails{

	def initializeTable() = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		statement.executeUpdate(
			"CREATE TABLE `returnitem` (" +
			"`returnitemid` int(10) NOT NULL," +
			"`date` timestamp NOT NULL," +
			"`salesid` int(10) NOT NULL," +
			"`itemid` int(10) NOT NULL," +
			"`itemname` varchar(100) NOT NULL," +
			"`branchid` int(10) NOT NULL," +
			"`branchlocation` varchar(100) NOT NULL," +
			"`amount` bigint(20) NOT NULL," +
			"`description` varchar(500) NOT NULL," +
			"PRIMARY KEY (`returnitemid`)" +
			")"
		)	

		connection.close()
	}

	def hasInitialize(): Boolean =
	{
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement		
		try 
		{
			var queryResult = statement.executeQuery("SELECT * from returnitem")
			true
		}
		catch 
		{
			case _ => false
		}
	}

	def getAllReturnitemhistory : ObservableBuffer[Returnitemhistory] = {

		var returnitemlist: ObservableBuffer[Returnitemhistory] = new ObservableBuffer[Returnitemhistory]()

		//def Updatereturnitemlist() = {
		//returnitemlist.clear()

			Class.forName(myDBDetails.driver)
			myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
			val statement = myDBDetails.connection.createStatement
			val queryresult = statement.executeQuery("select * from returnitem")		

			while (queryresult.next){
				var returnitemid = queryresult.getInt("returnitemid")
				var date = queryresult.getTimestamp("date")
				var salesid = queryresult.getInt("salesid")
				var itemid = queryresult.getInt("itemid")
				var itemname = queryresult.getString("itemname")
				var branchid = queryresult.getInt("branchid")
				var branchlocation = queryresult.getString("branchlocation")
				var amount = queryresult.getInt("amount")
				var desc = queryresult.getString("description")

				var returnitemobject = new Returnitemhistory(returnitemid,date,salesid,itemid,itemname,branchid,branchlocation,amount,desc)

			returnitemlist += returnitemobject
		}	

		myDBDetails.connection.close()

		return returnitemlist

	}

	def AddtoItemlist(itemid: Int,itemname: String,itemdesc: String,itemprice: Double) = {

		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		statement.executeUpdate(s"Insert into item Values(${itemid},'${itemname}','${itemdesc}',${itemprice})")	


		//new item added so need to initialize data stock to 0 in itemstock table
		//for all branch stock will have 0 stock of this newitem
		val queryresult = statement.executeQuery("select branchid from branch")
		while (queryresult.next){
			var branchid =  queryresult.getInt("branchid")
			val statement2 = myDBDetails.connection.createStatement
			statement2.executeUpdate(s"Insert into itemstock values(${itemid},${branchid},0)")
		}		

		myDBDetails.connection.close()
	
	}

	def addStock(returnitemid: Int, date :String, salesid: Int, itemid: Int, itemname: String, branchid: Int, branchlocation:String, amount: Int, desc: String) = {
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		
		//get item quantity for particular sales in itemsold
		val statement1 = myDBDetails.connection.createStatement
		val queryresult1 = statement1.executeQuery(s"select quantity from itemsold where salesid=${salesid} and itemid=${itemid}")		
		var salesquantity = 0
		while (queryresult1.next){
			salesquantity = queryresult1.getInt("quantity")
		}

		//get num of stock in itemstock
		val statement = myDBDetails.connection.createStatement
		val queryresult = statement.executeQuery(s"select numofstock from itemstock where itemid=${itemid} and branchid=${branchid}")		
		var numofstock = 0
		while (queryresult.next){
			numofstock = queryresult.getInt("numofstock")
		}

		//Check return amount exceeds sales quantity
		if(amount <= salesquantity){
			numofstock += amount
			salesquantity -= amount

			//update itemstock
			statement.executeUpdate(s"Update itemstock set numofstock=${numofstock} where itemid=${itemid} and branchid=${branchid}")	
			//add record to returnitem
			statement.executeUpdate(s"Insert into returnitem Values(${returnitemid},'${date}','${salesid}',${itemid},'${itemname}',${branchid},'${branchlocation}',${amount},'${desc}')")	
			//update itemsold
			statement.executeUpdate(s"Update itemsold set quantity=${salesquantity} where salesid=${salesid} and itemid=${itemid}")	

		} else {
			val alert = new Alert(AlertType.Warning){
	          initOwner(MainApp.stage)
	          title       = "Unable to add"
	          headerText  = "Incorrect Sales ID/ Item Quantity"
	          contentText = "Please try again, Thank you!"
	        }.showAndWait()
		}
		
		myDBDetails.connection.close()
		//Updatereturnitemlist()
	}

	def minusstock(returnitemid: Int) = {
		Class.forName(myDBDetails.driver)

		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		
		val statement1 = myDBDetails.connection.createStatement
		val queryresult1 = statement1.executeQuery(s"select amount,salesid,itemid,branchid from returnitem where returnitemid=${returnitemid}")		
		var amount = 0
		var salesid = 0
		var itemid = 0
		var branchid = 0

		while (queryresult1.next){
			amount = queryresult1.getInt("amount")
			salesid = queryresult1.getInt("salesid")
			itemid = queryresult1.getInt("itemid")
			branchid = queryresult1.getInt("branchid")
		}

		val statement2 = myDBDetails.connection.createStatement
		val queryresult2 = statement2.executeQuery(s"select numofstock from itemstock where itemid=${itemid} and branchid=${branchid}")		
		var numofstock = 0
		while (queryresult2.next){
			numofstock = queryresult2.getInt("numofstock")
		}

		val statement3 = myDBDetails.connection.createStatement
		val queryresult3 = statement3.executeQuery(s"select quantity from itemsold where salesid=${salesid} and itemid=${itemid}")		
		var salesquantity = 0
		while (queryresult3.next){
			salesquantity = queryresult3.getInt("quantity")
		}

		if (amount <= numofstock){

			salesquantity += amount
			numofstock -= amount
			val statement = myDBDetails.connection.createStatement

			statement.executeUpdate(s"Update itemstock set numofstock=${numofstock} where itemid=${itemid} and branchid=${branchid}")	
			statement.executeUpdate(s"Delete from returnitem where returnitemid=${returnitemid}")	
			statement.executeUpdate(s"Update itemsold set quantity=${salesquantity} where salesid=${salesid} and itemid=${itemid}")	
			
		} else {
			val alert = new Alert(AlertType.Warning){
	          initOwner(MainApp.stage)
	          title       = "Unable to Remove"
	          headerText  = "Quantity exceeded item stock"
	          contentText = "Please try again, Thank you!"
	        }.showAndWait()
		}

		myDBDetails.connection.close()
		//Updatereturnitemlist()
	}

}