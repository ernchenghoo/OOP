package Models

import Database.myDBDetails
import Database.BranchDatabase
import Models.Branch

import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import scalafx.collections.ObservableBuffer
import java.sql.{Connection,DriverManager}
import java.sql.SQLException

class Itemstock(_itemid:Int, _branchid: Int, _numofstock: Int) extends myDBDetails{
	var itemid = ObjectProperty[Int](_itemid)
	var branchid = ObjectProperty[Int](_branchid)
	var numofstock = ObjectProperty[Int](_numofstock)

	def getItemname():StringProperty = {
		var itemname = new StringProperty("ItemID not found") 

		//get the lastest itemname from itemID
		for(item <- Item.getAllItems){
			if(item.id.getValue() == itemid.getValue())
				itemname = item.name
		}

		itemname
	}

	def getBranchlocation():StringProperty = {
		var branchlocation = new StringProperty("BranchID not found") 

		for(branch <- Branch.getAllBranchs){
			if(branch.branchid.getValue() == branchid.getValue())
				branchlocation = branch.location
		}

		branchlocation
	}
}

object Itemstock extends myDBDetails{

	def initializeTable() = {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement

		statement.executeUpdate(
			"CREATE TABLE itemstock ("+
  			"itemid int(10) NOT NULL,"+
  			"branchid int(10) NOT NULL,"+
  			"numofstock bigint(20) NOT NULL,"+
  			"PRIMARY KEY (itemid,branchid)"+
			")"
		)	

		connection.close()
	}

	def getAllItemStocks : ObservableBuffer[Itemstock] = {

		var list:ObservableBuffer[Itemstock] = new ObservableBuffer[Itemstock]()

		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		val queryresult = statement.executeQuery("select * from itemstock")	

		while (queryresult.next){
			var itemid = queryresult.getInt("itemid")
			var branchid = queryresult.getInt("branchid")
			var numofstock = queryresult.getInt("numofstock")

			list += new Itemstock(itemid,branchid,numofstock)
		}	

		connection.close()

		return list
	}

	def CheckItemQuantity(itemid:Int): Int = {

		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		val queryresult = statement.executeQuery(s"select * from itemstock where itemid =(${itemid}) and branchid = 1")		
		var quantity:Int = 0

		while (queryresult.next){
			quantity=queryresult.getInt("numofstock")			
		}	

		connection.close()
		return quantity
	}

	def updateItemQuantity(itemid:Int, branchid:Int, quantity:Int) = {

		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		val queryresult = statement.executeUpdate(s"update itemstock set numofstock =(${quantity}) where itemid =(${itemid}) and branchid = (${branchid})")	
		
	}
}