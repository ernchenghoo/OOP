package Models
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty,DoubleProperty}
import scalafx.collections.ObservableBuffer
import scala.math.BigDecimal

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class Checkout (_id: Int, _name: String, _price: Double, _quantity: Int){
	
	var id = ObjectProperty[Int](_id)
	var name = StringProperty(_name)
	var price = ObjectProperty[Double](_price)
	var quantity = ObjectProperty[Int](_quantity)
	var lineAmount = ObjectProperty[Double](_price * _quantity)
}

object Checkout {
	var listOfCheckedoutItems = new ObservableBuffer[Checkout]()
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
