package Models
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty,DoubleProperty}
import scala.collection.mutable.ListBuffer
import scala.math.BigDecimal

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class Checkout (val id: Int, var name: String, var price: Double, var quantity: Int){
	
	var lineAmount: Double = (quantity * price)
	lineAmount = BigDecimal(lineAmount).setScale(2,BigDecimal.RoundingMode.HALF_UP).toDouble

}

object Checkout {
	var listOfCheckedoutItems = new ListBuffer[Checkout]()
}

class sales(_salesid: Int, _branchid: Int, _date: Timestamp, _total: Double){
	var salesid = ObjectProperty[Int](_salesid)
	var branchid = ObjectProperty[Int](_branchid)
	var date = ObjectProperty[Timestamp](_date)
	var total = ObjectProperty[Double](_total)
}

class itemsold(_salesid: Int, _itemid: Int, _itemname: String, _quantity: Int, _price:Double){
	var salesid = ObjectProperty[Int](_salesid)
	var itemid = ObjectProperty[Int](_itemid)
	var itemname = ObjectProperty[String](_itemname)
	var quantity = ObjectProperty[Int](_quantity)
	var price = ObjectProperty[Double](_price)
}
