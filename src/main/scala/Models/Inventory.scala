package Models 

import Models.Branch

import java.sql.Timestamp
import java.text.SimpleDateFormat
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import java.time.format.DateTimeFormatter



class Inventory(var name: String, val id: Int, var price: Double) {	
	
}

class returnitemhistory( _returnitemid: Int, _date: Timestamp, _salesid: Int, _itemid: Int, _itemnamefromtable:String,  _branchid: Int, _branchlocationfromtable:String, val _amount: Int,val _description:String){
	var returnitemid = ObjectProperty[Int](_returnitemid)
	var date = ObjectProperty[Timestamp](_date)
	var salesid = ObjectProperty[Int](_salesid)
	var itemid = ObjectProperty[Int](_itemid)
	var itemnamefromtable = new StringProperty(_itemnamefromtable)
	var branchid = ObjectProperty[Int](_branchid)
	var branchlocationfromtable = new StringProperty(_branchlocationfromtable)
	var amount = ObjectProperty[Int](_amount)
	var description = new StringProperty(_description)

	def DatetoString():StringProperty = {
		var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		var stringdate = date.getValue().toLocalDateTime().format(formatter)
		new StringProperty(stringdate)
	}

	def getItemname():StringProperty = {
		var itemname = new StringProperty("ItemID not found") 

		//get the lastest itemname from itemID
		for(item <- Item.getAllItems){
			if(item.id.getValue() == itemid.getValue())
				itemname = item.name
		}

		//if ItemID still available in item table get lastest itemname
		//if string still is itemid not found the use then itemname from stockedithistory table
		if(itemname.getValue() == "ItemID not found")
			return itemnamefromtable
		else
			return itemname
	}

	def getBranchlocation():StringProperty = {
		var branchlocation = new StringProperty("BranchID not found") 

		for(branch <- Branch.getAllBranchs){
			if(branch.branchid.getValue() == branchid.getValue())
				branchlocation = branch.location
		}

		//if branchID still available in branch table get lastest branchlocation
		//if string still is BranchID not found then use the branchlocation from stockedithistory table
		if(branchlocation.getValue() == "BranchID not found")
			return branchlocationfromtable
		else
			return branchlocation
	}

	

}

