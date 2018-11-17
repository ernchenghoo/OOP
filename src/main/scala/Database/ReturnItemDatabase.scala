package Database

import Models.Item
import Models.returnitemhistory
import Models.branch
import Models.Itemstock

import java.sql.{Connection,DriverManager}
import scalafx.collections.ObservableBuffer

object ReturnItemDatabase {

	var returnitemlist: ObservableBuffer[returnitemhistory] = new ObservableBuffer[returnitemhistory]()

	def Updatereturnitemlist() = {
		returnitemlist.clear()

		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		val queryresult = statement.executeQuery("select * from returnitem")		

		while (queryresult.next){
			var stockeditid = queryresult.getInt("returnitemid")
			var date = queryresult.getTimestamp("date")
			var itemid = queryresult.getInt("itemid")
			var itemname = queryresult.getString("itemname")
			var branchid = queryresult.getInt("branchid")
			var branchlocation = queryresult.getString("branchlocation")
			var amount = queryresult.getInt("amount")
			var desc = queryresult.getString("description")

			var returnitemobject = new returnitemhistory(stockeditid,date,itemid,itemname,branchid,branchlocation,amount,desc)

			returnitemlist += returnitemobject
		}	

		myDBDetails.connection.close()
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

	def addStock(returnitemid: Int, date :String, itemid: Int, itemname: String, branchid: Int, branchlocation:String, amount: Int, desc: String) = {
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
		statement.executeUpdate(s"Insert into returnitem Values(${returnitemid},'${date}',${itemid},'${itemname}',${branchid},'${branchlocation}',${amount},'${desc}')")	

		myDBDetails.connection.close()
		Updatereturnitemlist()
	}

	def minusstock(returnitemid: Int) = {
		Class.forName(myDBDetails.driver)

		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		
		val statement = myDBDetails.connection.createStatement

		statement.executeUpdate(s"Delete from returnitem where returnitemid=${returnitemid}")	
		
		myDBDetails.connection.close()
		Updatereturnitemlist()
	}

}