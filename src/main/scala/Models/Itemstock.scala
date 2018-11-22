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
			(1,1,60),(1,2,20),(1,3,20),(1,4,20),
			(2,1,20),(2,2,20),(2,3,20),(2,4,20),
			(3,1,20),(3,2,20),(3,3,20),(3,4,20),
			(4,1,20),(4,2,20),(4,3,20),(4,4,20),
			(5,1,20),(5,2,20),(5,3,20),(5,4,20),
			(6,1,20),(6,2,20),(6,3,20),(6,4,20),
			(7,1,20),(7,2,20),(7,3,20),(7,4,20),
			(8,1,20),(8,2,20),(8,3,20),(8,4,20),
			(9,1,20),(9,2,20),(9,3,20),(9,4,20),			
			(11,1,20),(11,2,20),(11,3,20),(11,4,20),
			(12,1,20),(12,2,20),(12,3,20),(12,4,20),
			(13,1,20),(13,2,20),(13,3,20),(13,4,20),
			(14,1,20),(14,2,20),(14,3,20),(14,4,20),
			(15,1,20),(15,2,20),(15,3,20),(15,4,20);"""
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