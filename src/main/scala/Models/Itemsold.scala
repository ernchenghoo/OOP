package Models

import Database.myDBDetails
import java.sql.{Connection,DriverManager}
import java.sql.SQLException

import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty,DoubleProperty}
import scalafx.collections.ObservableBuffer
import scala.math.BigDecimal


class Itemsold(_salesid: Int, _itemid: Int, _itemname: String, _quantity: Int, _price:Double) extends myDBDetails{
	var salesid = ObjectProperty[Int](_salesid)
	var itemid = ObjectProperty[Int](_itemid)
	var itemname = ObjectProperty[String](_itemname)
	var quantity = ObjectProperty[Int](_quantity)
	var price = ObjectProperty[Double](_price)
}

object Itemsold extends myDBDetails{
	def initializeTable() = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		statement.executeUpdate(
			"CREATE TABLE itemsold("+
  			"salesid int(10) NOT NULL,"+
  			"itemid int(10) NOT NULL,"+
  			"itemname varchar(200) NOT NULL,"+
  			"quantity int(10) NOT NULL,"+
  			"price decimal(12,2) NOT NULL,"+
  			"PRIMARY KEY (salesid,itemid)"+
			")"
		)	

		connection.close()
	}

	var Reportlist: ObservableBuffer[Itemsold] = new ObservableBuffer[Itemsold]()

	def addItemsold(salesid: Int,itemid: Int, itemname :String, quantity: Int, price: Double) = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)
		
		val statement = connection.createStatement

		//add record 
		statement.executeUpdate(s"Insert into itemsold Values(${salesid},${itemid},'${itemname}',${quantity},${price})")	

		connection.close()
	}

	def UpdateReportlist(fromDatelabel:String ,toDatelabel:String,branchdropdownlabel:Int):Double = {
		Reportlist.clear()
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)
		val statement = connection.createStatement
		val queryresult = statement.executeQuery(s"select sum(quantity),itemname,itemsold.itemid,itemsold.price,sales.salesid from itemsold,sales where sales.salesid = itemsold.salesid and sales.date between ('${fromDatelabel}') and ('${toDatelabel}')and (${branchdropdownlabel}) group by itemname,price")		
		var totalSales:Double = 0.0

		while (queryresult.next){
			var salesid = queryresult.getInt("salesid")
			var id = queryresult.getInt("itemid")
			var name = queryresult.getString("itemname")
			var quantity = queryresult.getInt("sum(quantity)")
			var price = queryresult.getDouble("price")

			var reportobject = new Itemsold(salesid,id,name,quantity,price)
			totalSales += quantity*price
			Reportlist += reportobject
		}	

		connection.close()
		return totalSales
	}
}