package Models
import scala.collection.mutable.MutableList

class Checkout (val id: Int, var name: String, var price: Double, var quantity: Int){
	var lineAmount = (quantity * price)

	

}

object Checkout {
	var listOfCheckedoutItems = MutableList [Checkout]()
}