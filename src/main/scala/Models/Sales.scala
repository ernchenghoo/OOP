package Models

import Database.myDBDetails
import java.sql.{Connection,DriverManager}
import java.sql.SQLException

import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty,DoubleProperty}
import scalafx.collections.ObservableBuffer
import scala.math.BigDecimal
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class Sales(_salesid: Int, _branchid: Int, _date: Timestamp, _total: Double) extends myDBDetails{
	var salesid = ObjectProperty[Int](_salesid)
	var branchid = ObjectProperty[Int](_branchid)
	var date = ObjectProperty[Timestamp](_date)
	var total = ObjectProperty[Double](_total)
}

object Sales extends myDBDetails{
	def initializeTable() = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		statement.executeUpdate(
			"CREATE TABLE sales("+
  			"salesid int(10) NOT NULL,"+
  			"branchid int(10) NOT NULL,"+
  			"date timestamp NOT NULL,"+
  			"totalsalesamount decimal(12,2) NOT NULL,"+
  			"PRIMARY KEY (salesid,branchid)"+
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
			var queryResult = statement.executeQuery("SELECT * from Sales")
			true
		}
		catch 
		{
			case _ => false
		}
	}

	//for storing Saleslist from database
	var Saleslist: ObservableBuffer[Sales] = new ObservableBuffer[Sales]()

	def UpdateSaleslist() = {
		Saleslist.clear()

		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)
		val statement = connection.createStatement
		val queryresult = statement.executeQuery("select * from sales")		

		while (queryresult.next){
			var id = queryresult.getInt("salesid")
			var branchid = queryresult.getInt("branchid")
			var date = queryresult.getTimestamp("date")
			var total = queryresult.getDouble("totalsalesamount")

			var salesobject = new Sales(id,branchid,date,total)

			Saleslist += salesobject
		}	

		connection.close()
	}

	def addCheckout(salesid: Int,branchid: Int, date :String, total: Double) = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)
		
		val statement = connection.createStatement

		//add record 
		statement.executeUpdate(s"Insert into sales Values(${salesid},${branchid},'${date}',${total})")	

		connection.close()
	}
}