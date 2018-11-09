package Controllers
import Database.myDBDetails
import Models.Checkout
import MainSystem.MainApp
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.Includes._
import scalafxml.core.macros.sfxml
import java.sql.{Connection,DriverManager}
import scalafx.scene.control.Alert.AlertType
import scala.collection.mutable.MutableList
import scala.collection.mutable.Map


@sfxml
class CheckoutController (		
    private val searchItemID: TextField,
    private val quantityField: TextField,
    private val checkoutGrid: GridPane,
	) {	
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		//variables			
		var itemRow: Int = 1			
		var checkoutGridPaneLabels = MutableList[Label]()			

		def moveToMainMenu() = {
			MainApp.showMainMenu()
			myDBDetails.connection.close
		}		
		
		def moveToPayment() = {
			if (checkoutGrid.children.size <= 6) { // if no items checked out
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

		def checkoutItem = {	
			//query for comparing item in database			
			val itemMatchQuery = statement.executeQuery("select * from inventory where id = '"+ searchItemID.text.value+ "'" )			
			
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
					// case to determine if item exist in database					
					itemMatchQuery.next match {
						case true => {
							var searchedItem = new Models.Inventory(itemMatchQuery.getString("name"), itemMatchQuery.getInt("id"), 
							itemMatchQuery.getDouble("price"))
							var quantity: Int = quantityField.text.value.toInt											
							var checkedOutItems = new Models.Checkout (searchedItem.id, searchedItem.name, 
								searchedItem.price, quantity)										
							addCheckoutItem(checkedOutItems)							
						}
						case false => {							
							val NotExistAlert = new Alert(AlertType.Warning){
					        initOwner(MainApp.stage)
					        title       = "Not Found"
					        headerText = "Item Not Found"
					        contentText  = "Item does not exist"
							}
						}
					}
				}
			}
		}
		    //create new column in grid pane and add labels containing all item information
		def addCheckoutItem (_checkedOutItems: Models.Checkout) {
			var duplicate = false

			for (elements <- Checkout.listOfCheckedoutItems) {
				if (elements.id == _checkedOutItems.id)
					duplicate = true									
			}				

			duplicate match {
				case false => {
					//create labels
					var rowLabel  = new Label(itemRow.toString)				
					var idLabel  = new Label(_checkedOutItems.id.toString)
					var nameLabel  = new Label(_checkedOutItems.name.toString)
					var priceLabel  = new Label(_checkedOutItems.price.toString)
					var qtyLabel  = new Label(_checkedOutItems.quantity.toString)
					var lineAmountLabel  = new Label(_checkedOutItems.lineAmount.toString)
					//add labels into label array
					checkoutGridPaneLabels += (rowLabel, idLabel, nameLabel, priceLabel, qtyLabel, lineAmountLabel)	
					//number of labels in grid
					var labelsInGrid = (itemRow - 1) * 6
					checkoutGrid.addRow(itemRow, checkoutGridPaneLabels(0 + labelsInGrid), checkoutGridPaneLabels(1 + labelsInGrid),
										checkoutGridPaneLabels(2 + labelsInGrid),checkoutGridPaneLabels(3 + labelsInGrid),
										checkoutGridPaneLabels(4 + labelsInGrid), checkoutGridPaneLabels(5 + labelsInGrid))					
					Checkout.listOfCheckedoutItems += _checkedOutItems			
					itemRow += 1
				}

				case true => {
					var labelCount = 0
					var labelNum = 0
					
					for (elements <- checkoutGridPaneLabels){					
						if (elements.text.value == _checkedOutItems.id.toString()) {						
							labelNum = labelCount							
						}
						else {
							labelCount += 1
						}						
					}
					var newQty = (checkoutGridPaneLabels(labelNum + 3).text.value.toInt) + _checkedOutItems.quantity
					var newLineAmount = newQty * _checkedOutItems.price
					checkoutGridPaneLabels(labelNum + 3).text.value = newQty.toString
					checkoutGridPaneLabels(labelNum + 4).text.value = newLineAmount.toString

					for (elements <- Checkout.listOfCheckedoutItems) {
						if (elements.id == _checkedOutItems.id){
							elements.quantity = newQty	
							elements.lineAmount = newLineAmount					
						}
					}				
				}				
			}
		}
}	
