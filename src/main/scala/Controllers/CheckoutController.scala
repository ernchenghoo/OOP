package Controllers
import Models.Checkout
import MainSystem.MainApp
import Models.Itemstock
import Database.myDBDetails
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.Node
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.geometry.HPos
import scalafxml.core.macros.sfxml
import java.sql.{Connection,DriverManager}
import scalafx.scene.control.Alert.AlertType
import scalafx.beans.property.{StringProperty, ObjectProperty}
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map
import scala.math.BigDecimal

@sfxml
class CheckoutController (		
    private val searchItemID: TextField,
    private val quantityField: TextField,   
    private val checkoutButton: Button,
    private val deleteLineButton: Button,
    private val checkoutTable: TableView [Checkout],
    private val idCol: TableColumn [Checkout, Int],
    private val nameCol: TableColumn [Checkout, String],
    private val priceCol: TableColumn [Checkout, Double],
    private val qtyCol: TableColumn [Checkout, Int],
    private val lineAmountCol: TableColumn [Checkout, Double]
	) {	
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement	

		checkoutTable.items = Models.Checkout.listOfCheckedoutItems
		idCol.cellValueFactory = {_.value.id}
		nameCol.cellValueFactory = {_.value.name}
		priceCol.cellValueFactory = {_.value.price}
		qtyCol.cellValueFactory = {_.value.quantity}
		lineAmountCol.cellValueFactory = {_.value.lineAmount}

		if (Models.Checkout.listOfCheckedoutItems.nonEmpty) {
			checkoutButton.setVisible (true)
		}			
		
		def moveToMainMenu() = {			
			myDBDetails.connection.close
			statement.close()
			MainApp.showMainMenu()			
		}		
		
		def moveToPayment() {			
			MainApp.goToPaymentMenu()
			myDBDetails.connection.close
			statement.close()				
		}
		//if not empty means already went to payment but came back to edit checkout, so need to populate grid pane
		if (Models.Checkout.listOfCheckedoutItems.nonEmpty) {
			
		}

		def checkoutItem {		
			//query for comparing item in database			
			val itemMatchQuery = statement.executeQuery("select * from item where itemid = '"+ searchItemID.text.value+ "'" )			
			
			//case to determine if there are value for item ID
			searchItemID.text.value.isEmpty match {
				case true => {
					val emptyAlert = new Alert(AlertType.Warning){
			          initOwner(MainApp.stage)
			          title       = "No Selection"
			          headerText = "Empty Field"
			          contentText  = "Input value incomplete"
			        }
			        .showAndWait()
				}
				case false => {
					// search value not empty				
					itemMatchQuery.next match {
						case true => {//item found
							//check item quantity
							var itemid = itemMatchQuery.getInt("itemid")
							var checkquantity:Int = Itemstock.CheckItemQuantity(itemid)

							if(checkquantity != 0){
								var searchedItem = new Models.Inventory(itemMatchQuery.getString("itemname"), itemMatchQuery.getInt("itemid"), 
								itemMatchQuery.getDouble("price"))
								var quantity: Int = quantityField.text.value.toInt										
								var checkedOutItems = new Models.Checkout (searchedItem.id, searchedItem.name, 
									searchedItem.price, quantity)										
								addCheckoutItem(checkedOutItems)
							}
							else {								
								val NotExistAlert = new Alert(AlertType.Warning){
						        initOwner(MainApp.stage)
						        title       = "Not Found"
						        headerText = "Item Not Found"
						        contentText  = "Item does not exist"
								}
								.showAndWait()
							}							
						}
						case false => {							
							val NotExistAlert = new Alert(AlertType.Warning){
					        initOwner(MainApp.stage)
					        title       = "Not Found"
					        headerText = "Item Not Found"
					        contentText  = "Item does not exist"
							}
							.showAndWait()
						}
					}
				}
			}
		}
		    //create new column in grid pane and add labels containing all item information
		def addCheckoutItem (_checkedOutItems: Models.Checkout) {
			var duplicate = false			

			//check for duplicate
			for (elements <- Models.Checkout.listOfCheckedoutItems) {				
				if (elements.id.value ==  _checkedOutItems.id.value)
					duplicate = true					
			}				

			duplicate match {
				case false => {
					Models.Checkout.listOfCheckedoutItems += _checkedOutItems
					if (checkoutButton.isVisible == false) {
						checkoutButton.setVisible (true)
					}									
				}

				case true => {					
					for (elements <- Models.Checkout.listOfCheckedoutItems) {
						// check item quantity
						var checkquantity:Int = Itemstock.CheckItemQuantity(_checkedOutItems.id.value)

						if (elements.id.value == _checkedOutItems.id.value) {
							if (elements.quantity.value < checkquantity){
								elements.quantity.value = elements.quantity.value + _checkedOutItems.quantity.value
								elements.lineAmount.value = elements.quantity.value * _checkedOutItems.price.value
							}
							else 
							{
								val ExceedQuantityAlert = new Alert(AlertType.Warning){
						        initOwner(MainApp.stage)
						        title       = "Not enough stock"
						        headerText = "Item Not enough"
						        contentText  = "Please add more stock for this Item"
							}
							.showAndWait()
							}

						}
					}

				}
			}
		}

		def deleteLineItem () {
			if(checkoutTable.selectionModel().selectedItem.value != null) {
				Checkout.listOfCheckedoutItems.remove (checkoutTable.selectionModel().selectedIndex.value)
			}
			else {
				val NotExistAlert = new Alert(AlertType.Warning){
			        initOwner(MainApp.stage)
			        title       = "No Selection"
			        headerText = "No line item selected"
			        contentText  = "Please select a line item to be deleted."
					}
					.showAndWait()
			}			
		}
}
