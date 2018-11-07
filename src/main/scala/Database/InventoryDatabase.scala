package Database

import Models.Item

import java.sql.{Connection,DriverManager}
import scalafx.collections.ObservableBuffer



object InventoryDatabase{

	var Itemlist: ObservableBuffer[Item] = new ObservableBuffer[Item]()

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
		statement.executeUpdate(s"Delete from item where itemid='${itemid}'")	

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

		myDBDetails.connection.close()
		UpdateItemlist()
	}



}