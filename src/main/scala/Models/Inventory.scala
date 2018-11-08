package Models 

import Database.InventoryDatabase

import java.sql.Timestamp
import java.text.SimpleDateFormat
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import java.time.format.DateTimeFormatter

class branch(_branchid: Int, _location:String){
	var branchid = ObjectProperty[Int](_branchid)
	var location = new StringProperty(_location)
}

class Item(_id: Int,_name: String, _desc: String, _price: Double){
	var id = ObjectProperty[Int](_id)
	var name = new StringProperty(_name)
	var desc = new StringProperty(_desc)
	var price = ObjectProperty[Double](_price)
}

class Itemstock(_itemid:Int, _branchid: Int, _numofstock: Int){
	var itemid = ObjectProperty[Int](_itemid)
	var branchid = ObjectProperty[Int](_branchid)
	var numofstock = ObjectProperty[Int](_numofstock)
}

class stockedithistory( _stockeditid: Int, _date: Timestamp, _itemid: Int, _branchid: Int, val _amount: Int,val _description:String){
	var stockeditid = ObjectProperty[Int](_stockeditid)
	var date = ObjectProperty[Timestamp](_date)
	var itemid = ObjectProperty[Int](_itemid)
	var branchid = ObjectProperty[Int](_branchid)
	var amount = ObjectProperty[Int](_amount)
	var description = new StringProperty(_description)

	def DatetoString():StringProperty = {
		var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		var stringdate = date.getValue().toLocalDateTime().format(formatter)
		new StringProperty(stringdate)
	}

	def getItemname():StringProperty = {
		var itemname = new StringProperty("ItemID not found") 

		for(item <- InventoryDatabase.Itemlist){
			if(item.id.getValue() == itemid.getValue())
				itemname = item.name
		}
		itemname
	}

	def getBranchname():StringProperty = {
		var branchlocation = new StringProperty("BranchID not found") 

		for(branch <- InventoryDatabase.Branchlist){
			if(branch.branchid.getValue() == branchid.getValue())
				branchlocation = branch.location
		}
		branchlocation
	}

	

}
