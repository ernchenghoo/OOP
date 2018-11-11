package Controllers
import Database.myDBDetails
import Models.Checkout
import MainSystem.MainApp
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.Node
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml
import java.sql.{Connection,DriverManager}
import scalafx.scene.control.Alert.AlertType
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map
import scala.math.BigDecimal

@sfxml
class CheckoutController (		
    private val searchItemID: TextField,
    private val quantityField: TextField,
    private val checkoutGrid: GridPane,
    private val checkoutButton: Button,    
	) {	
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		//variables			
		var itemRow: Int = 1			
		var rowLabels = ListBuffer[Label]()
		var idLabels = ListBuffer[Label]()
		var nameLabels = ListBuffer[Label]()
		var unitPriceLabels = ListBuffer[Label]()
		var qtyLabels = ListBuffer[Label]()
		var lineAmountLabels = ListBuffer[Label]()
		var deleteHyperlinks = ListBuffer[Hyperlink]()	

		def moveToMainMenu() = {
			MainApp.showMainMenu()
			myDBDetails.connection.close
			statement.close()
		}		
		
		def moveToPayment() {			
			MainApp.goToPaymentMenu()
			myDBDetails.connection.close
			statement.close()				
		}

		def checkoutItem {			
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
							.showAndWait()
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
					Checkout.listOfCheckedoutItems += _checkedOutItems
					//create labels									
					idLabels  += new Label(_checkedOutItems.id.toString)
					nameLabels  += new Label(_checkedOutItems.name.toString)
					unitPriceLabels  += new Label(_checkedOutItems.price.toString)
					qtyLabels  += new Label(_checkedOutItems.quantity.toString)
					lineAmountLabels  += new Label(_checkedOutItems.lineAmount.toString)
					var deleteHyperlink = new Hyperlink ("Delete line")
					
					checkoutGrid.addRow(itemRow, idLabels(itemRow-1), nameLabels(itemRow-1), 
										unitPriceLabels(itemRow-1), qtyLabels(itemRow-1), lineAmountLabels(itemRow-1))

					deleteHyperlink.onAction = (e:ActionEvent) => {									
						if (Checkout.listOfCheckedoutItems.nonEmpty) {
							
							var rowToBeDeleted = deleteHyperlinks.indexOf(deleteHyperlink)
							var buttonIndex = checkoutGrid.children.indexOf(deleteHyperlink)							
							
							//remove from labels array							
							idLabels.remove(rowToBeDeleted)
							nameLabels.remove(rowToBeDeleted)
							unitPriceLabels.remove(rowToBeDeleted)
							qtyLabels.remove(rowToBeDeleted)
							lineAmountLabels.remove(rowToBeDeleted)
							deleteHyperlinks.remove(rowToBeDeleted)

							//delete column
							checkoutGrid.children.remove (buttonIndex-5)
							checkoutGrid.children.remove (buttonIndex-5)
							checkoutGrid.children.remove (buttonIndex-5)
							checkoutGrid.children.remove (buttonIndex-5)
							checkoutGrid.children.remove (buttonIndex-5)
							checkoutGrid.children.remove (buttonIndex-5)							

							//remove from checkoutList
							Checkout.listOfCheckedoutItems.remove (rowToBeDeleted)
							itemRow -= 1

							if (Checkout.listOfCheckedoutItems.isEmpty) {
								checkoutButton.setVisible (false)
							}
							println (checkoutGrid.children.size)						
						}						
					}					
					deleteHyperlinks += deleteHyperlink
					checkoutGrid.add(deleteHyperlinks(itemRow-1), 5, itemRow)
					
					itemRow += 1
					if (checkoutButton.isVisible == false) {
						checkoutButton.setVisible (true)
					}
					println (checkoutGrid.children.size)					
				}

				case true => {
					var labelCount = 0
					var labelNum = 0
					
					for (elements <- idLabels){					
						if (elements.text.value == _checkedOutItems.id.toString()) {						
							labelNum = labelCount							
						}
						else {
							labelCount += 1
						}						
					}
					var newQty = (qtyLabels(labelNum).text.value.toInt) + _checkedOutItems.quantity
					var newLineAmount = newQty * _checkedOutItems.price
					newLineAmount = BigDecimal(newLineAmount).setScale(2,BigDecimal.RoundingMode.UP).toDouble
					qtyLabels(labelNum).text.value = newQty.toString
					lineAmountLabels(labelNum).text.value = newLineAmount.toString

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
