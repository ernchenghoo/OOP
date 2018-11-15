package Database

import Models.Item
import Models.stockedithistory
import Models.branch
import Models.Itemstock
import Models.returnitemhistory
import Models.sales
import Models.itemsold


import java.sql.{Connection,DriverManager}
import scalafx.collections.ObservableBuffer


object CheckoutDatabase{
	//for storing Saleslist from database
	var Saleslist: ObservableBuffer[sales] = new ObservableBuffer[sales]()

	var Reportlist: ObservableBuffer[itemsold] = new ObservableBuffer[itemsold]()

	//for Itemlist function
	def UpdateSaleslist() = {
		Saleslist.clear()

		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		val queryresult = statement.executeQuery("select * from sales")		

		while (queryresult.next){
			var id = queryresult.getInt("salesid")
			var branchid = queryresult.getInt("branchid")
			var date = queryresult.getTimestamp("date")
			var total = queryresult.getDouble("totalsalesamount")

			var salesobject = new sales(id,branchid,date,total)

			Saleslist += salesobject
		}	

		myDBDetails.connection.close()
	}

	def addCheckout(salesid: Int,branchid: Int, date :String, total: Double) = {
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		
		val statement = myDBDetails.connection.createStatement

		//add record 
		statement.executeUpdate(s"Insert into sales Values(${salesid},${branchid},'${date}',${total})")	

		myDBDetails.connection.close()
	}

	def addItemsold(salesid: Int,itemid: Int, itemname :String, quantity: Int, price: Double) = {
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		
		val statement = myDBDetails.connection.createStatement

		//add record 
		statement.executeUpdate(s"Insert into itemsold Values(${salesid},${itemid},'${itemname}',${quantity},${price})")	

		myDBDetails.connection.close()
	}

	def UpdateReportlist(fromDatelabel:String ,toDatelabel:String,branchdropdownlabel:Int):Double = {
		Reportlist.clear()
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		val queryresult = statement.executeQuery(s"select sum(quantity),itemname,itemsold.itemid,itemsold.price,sales.salesid from itemsold,sales where sales.salesid = itemsold.salesid and sales.date between ('${fromDatelabel}') and ('${toDatelabel}')and (${branchdropdownlabel}) group by itemname")		
		var totalSales:Double = 0.0

		while (queryresult.next){
			var salesid = queryresult.getInt("salesid")
			var id = queryresult.getInt("itemid")
			var name = queryresult.getString("itemname")
			var quantity = queryresult.getInt("sum(quantity)")
			var price = queryresult.getDouble("price")

			var reportobject = new itemsold(salesid,id,name,quantity,price)
			totalSales += quantity*price
			Reportlist += reportobject
		}	

		myDBDetails.connection.close()
		return totalSales
	}


}
//