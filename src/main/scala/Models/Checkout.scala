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

object Checkout extends myDBDetails {
	Class.forName(driver)
	var listOfCheckedoutItems = new ObservableBuffer[Checkout]()

	def verifyCheckoutItem (itemID: Int): Any = {
		connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = connection.createStatement

		val itemMatchQuery = statement.executeQuery("select * from item where itemid = '"+ itemID+ "'" )

		if (itemMatchQuery.next){			
			return true
		}
		else {
			return false
		}
		connection.close()
	}

	def checkDuplicate (itemID: Int): Boolean = {
		var isDuplicate = false
		for (elements <- listOfCheckedoutItems) {
			if (elements.id.value ==  itemID)
				isDuplicate = true
		}
		isDuplicate
	}

	def addCheckoutItem (itemID: Int, itemQuantity: Int) {
		connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = connection.createStatement

		val checkoutItemQuery = statement.executeQuery("select * from item where itemid = '"+ itemID+ "'" )
		checkoutItemQuery.next()
		var searchedItem = new Models.Inventory(checkoutItemQuery.getString("itemname"), checkoutItemQuery.getInt("itemid"), 
								checkoutItemQuery.getDouble("price"))		
		var lineItem = new Models.Checkout (searchedItem.id, searchedItem.name, 
			searchedItem.price, itemQuantity)

		listOfCheckedoutItems += lineItem
		connection.close()
	}

	def updateLineItem (itemID: Int, itemQuantity: Int) {
		for (elements <- Models.Checkout.listOfCheckedoutItems) {
			if (elements.id.value == itemID) {				
				elements.quantity.value = elements.quantity.value + itemQuantity
				elements.lineAmount.value = elements.quantity.value * elements.price.value
			}
		}
	}
}



