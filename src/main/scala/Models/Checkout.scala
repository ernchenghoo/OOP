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



