package Controllers
import Database.myDBDetails
import MainSystem.MainApp
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import java.sql.{Connection,DriverManager}
import scalafx.scene.control.Alert.AlertType
import scala.collection.mutable.MutableList


@sfxml
class CheckoutController (
	private val connectDbButton: Button,
	private val checkout2: Button,
    private val searchItemID: TextField,
    private val quantityField: TextField,
    private val checkoutGrid: GridPane,
	) {	
		//establish database connection when enter checkout page
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		//variables
		var itemRow: Int = 1
		var checkedOutItem = MutableList[Models.Inventory]()

		def moveToMainMenu() = {
			MainApp.showPersonOverview()
			myDBDetails.connection.close
		}

		def moveToPayment() = {
			if (checkedOutItem.isEmpty) {
				val emptyAlert = new Alert(AlertType.Warning){
		          initOwner(MainApp.stage)
		          title       = "Checkout Invalid"
		          headerText = "Checkout Empty"
		          contentText  = "No items to be checked out"
		        }
		        .showAndWait()
			}				
			else {
				MainApp.goToPaymentMenu()
				myDBDetails.connection.close
			}	
		}
		//create new column in grid pane and add labels containing all item information
		def addCheckoutItem (foundItem: Models.Inventory) {
			checkoutGrid.addRow(itemRow)

			var lineAmount: Double = foundItem.price.toString.toDouble * quantityField.text.value.toDouble	
			checkoutGrid.add (new Label(itemRow.toString), 0, itemRow)		
			checkoutGrid.add (new Label(foundItem.name.toString),1,itemRow)
			checkoutGrid.add (new Label(foundItem.id.toString),2,itemRow)
			checkoutGrid.add (new Label(foundItem.price.toString),3,itemRow)
			checkoutGrid.add (new Label(quantityField.text.value.toString),4,itemRow)
			checkoutGrid.add (new Label(lineAmount.toString),5,itemRow)	
			itemRow += 1	
		}
		
		def checkoutItem = {	
			//query for comparing item in database			
			val queryValue = statement.executeQuery("select * from inventory where id = '"+ searchItemID.text.value+ "'" )			
			
			//if item found in database
		    if (queryValue.next && quantityField.text.value.nonEmpty ) {
				var searchedItem = new Models.Inventory(queryValue.getString("name"), queryValue.getInt("id"), queryValue.getDouble("price"))
				addCheckoutItem(searchedItem)
				checkedOutItem += searchedItem				
		    }
		    //if either textfield is empty
		    else if (searchItemID.text.value.isEmpty || quantityField.text.value.isEmpty ){
		        val emptyAlert = new Alert(AlertType.Warning){
		          initOwner(MainApp.stage)
		          title       = "No Selection"
		          headerText = "Empty Field"
		          contentText  = "Input value incomplete"
		        }
		        .showAndWait()
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
}	
