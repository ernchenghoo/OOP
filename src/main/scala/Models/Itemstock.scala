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

		statement.executeUpdate(
			"""INSERT INTO `itemstock` VALUES 
			(1,1,60),(1,2,0),(1,3,0),(1,4,0),
			(2,1,0),(2,2,0),(2,3,0),(2,4,0),
			(3,1,0),(3,2,0),(3,3,0),(3,4,0),
			(4,1,0),(4,2,0),(4,3,0),(4,4,0),
			(5,1,0),(5,2,0),(5,3,0),(5,4,0),
			(6,1,0),(6,2,0),(6,3,0),(6,4,0),
			(7,1,0),(7,2,0),(7,3,0),(7,4,0),
			(8,1,0),(8,2,0),(8,3,0),(8,4,0),
			(9,1,0),(9,2,0),(9,3,0),(9,4,0),
			(10,1,0),(10,2,0),(10,3,0),(10,4,0),
			(11,1,0),(11,2,0),(11,3,0),(11,4,0),
			(12,1,0),(12,2,0),(12,3,0),(12,4,0),
			(13,1,0),(13,2,0),(13,3,0),(13,4,0),
			(14,1,0),(14,2,0),(14,3,0),(14,4,0),
			(15,1,0),(15,2,0),(15,3,0),(15,4,0);"""
		)		

		connection.close()
	}

	def hasInitialize(): Boolean =
	{
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement		
		try 
		{
			var queryResult = statement.executeQuery("SELECT * from itemstock")
			true
		}
		catch 
		{
			case _ => false
		}
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

	def CheckItemQuantity(itemid:Int,branchid:Int): Int = {

		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)

		val statement = connection.createStatement
		val queryresult = statement.executeQuery(s"select * from itemstock where itemid =(${itemid}) and branchid = (${branchid})")		
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