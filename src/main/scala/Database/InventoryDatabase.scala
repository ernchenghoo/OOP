package Database

import Models.Item
import Models.stockedithistory
import Models.branch

import java.sql.{Connection,DriverManager}
import scalafx.collections.ObservableBuffer



object InventoryDatabase{

	//for storing Itemlist from database
	var Itemlist: ObservableBuffer[Item] = new ObservableBuffer[Item]()

	//for storing stockedithistory from database
	var Stockhistorylist: ObservableBuffer[stockedithistory] = new ObservableBuffer[stockedithistory]()

	//for storing branchlist from database
	var Branchlist: ObservableBuffer[branch] = new ObservableBuffer[branch]()


	//for Itemlist function
	def UpdateItemlist() = {
		Itemlist.clear()

		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		val queryresult = statement.executeQuery("select * from item")		

		while (queryresult.next){
			var id = queryresult.getInt("itemid")
			var name = queryresult.getString("itemname")
			var desc = queryresult.getString("itemdesc")
			var price = queryresult.getDouble("price")

			var itemobject = new Item(id,name,desc,price)

			Itemlist += itemobject
		}	

		myDBDetails.connection.close()
	}

	def DeletefromItemlist(itemid: Int) = {
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		//delete from item table
		statement.executeUpdate(s"Delete from item where itemid=${itemid}")	


		//delete from itemstock
		val statement2 = myDBDetails.connection.createStatement
		statement2.executeUpdate(s"Delete from itemstock where itemid=${itemid}")	


		myDBDetails.connection.close()
	}

	def EditfromItemlist(itemid: Int,itemname: String,itemdesc: String,itemprice: Double) = {
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		statement.executeUpdate(s"Update item set itemname='${itemname}', itemdesc='${itemdesc}', price=${itemprice} where itemid='${itemid}'")	

		myDBDetails.connection.close()
		UpdateItemlist()
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
		UpdateItemlist()
	}
	//End Itemlist function

	//for Stockhistoryfunction
	def UpdateStockhistorylist() = {
		Stockhistorylist.clear()

		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
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

			var stockeditobject = new stockedithistory(stockeditid,date,itemid,itemname,branchid,branchlocation,amount,desc)

			Stockhistorylist += stockeditobject
		}	

		myDBDetails.connection.close()
	}

	def getstock(itemid: Int, branchid: Int): Int = {
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		
		val statement = myDBDetails.connection.createStatement


		//check if stock is sufficient
		val queryresult = statement.executeQuery(s"select numofstock from itemstock where itemid=${itemid} and branchid=${branchid}")		

		var numofstock = 0
		while (queryresult.next){
			numofstock = queryresult.getInt("numofstock")
		}		

		myDBDetails.connection.close()

		numofstock
	}

	def addStock(stockeditid: Int, date :String, itemid: Int, itemname: String, branchid: Int, branchlocation:String, amount: Int, desc: String) = {
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		
		val statement = myDBDetails.connection.createStatement

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

		myDBDetails.connection.close()
		UpdateStockhistorylist()
	}

	def minusStock(stockeditid: Int, date :String, itemid: Int, itemname:String, branchid: Int, branchlocation:String, amount: Int, desc: String) = {
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		
		val statement = myDBDetails.connection.createStatement

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

		myDBDetails.connection.close()
		UpdateStockhistorylist()
	}

	
	//end Stockhistoryfunction

	//for branchlist function
	def UpdateBranchlist() = {
		Branchlist.clear()

		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		val queryresult = statement.executeQuery("select * from branch")		

		while (queryresult.next){
			var branchid = queryresult.getInt("branchid")
			var location = queryresult.getString("location")

			var branchobject = new branch(branchid,location)

			Branchlist += branchobject
		}	

		myDBDetails.connection.close()
	}

	//branchlist function end



}