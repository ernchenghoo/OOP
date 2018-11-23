package Database

import MainSystem.MainApp
import Models.Item
import Models.Returnitemhistory
import Models.Branch
import Models.Itemstock
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

import java.sql.{Connection,DriverManager}
import scalafx.collections.ObservableBuffer

object ReturnItemDatabase {

	/*var returnitemlist: ObservableBuffer[returnitemhistory] = new ObservableBuffer[returnitemhistory]()

	def Updatereturnitemlist() = {
		returnitemlist.clear()

		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		val queryresult = statement.executeQuery("select * from returnitem")		

		while (queryresult.next){
			var stockeditid = queryresult.getInt("returnitemid")
			var date = queryresult.getTimestamp("date")
			var salesid = queryresult.getInt("salesid")
			var itemid = queryresult.getInt("itemid")
			var itemname = queryresult.getString("itemname")
			var branchid = queryresult.getInt("branchid")
			var branchlocation = queryresult.getString("branchlocation")
			var amount = queryresult.getInt("amount")
			var desc = queryresult.getString("description")

			var returnitemobject = new returnitemhistory(stockeditid,date,salesid,itemid,itemname,branchid,branchlocation,amount,desc)

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
	          headerText  = "Quantity exceeded item sold"
	          contentText = "Please try again, Thank you!"
	        }.showAndWait()
		}
		
		myDBDetails.connection.close()
		Updatereturnitemlist()
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
		Updatereturnitemlist()
	}*/

}