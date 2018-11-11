package Models
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import scala.collection.mutable.ListBuffer
import scala.math.BigDecimal

class Checkout (val id: Int, var name: String, var price: Double, var quantity: Int){
	
	var lineAmount: Double = (quantity * price)
	lineAmount = BigDecimal(lineAmount).setScale(2,BigDecimal.RoundingMode.HALF_UP).toDouble

}

object Checkout {
	var listOfCheckedoutItems = new ListBuffer[Checkout]()
}
