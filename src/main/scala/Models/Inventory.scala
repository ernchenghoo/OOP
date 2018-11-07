package Models 

import java.sql.Timestamp
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}

class Inventory(var name: String, val id: Int, var price: Double) {	
	
	
	
}

class Item(_id: Int,_name: String, _desc: String, _price: Double){
	var id = ObjectProperty[Int](_id)
	var name = new StringProperty(_name)
	var desc = new StringProperty(_desc)
	var price = ObjectProperty[Double](_price)
}

class Itemstock(val itemid:Int,var branchid: Int, var numofstock: Int)

class stockedithistory(val date: Timestamp, val itemid: Int, val branchid: Int, val amount: Int,val description:String)