package Controllers
import Database.myDBDetails
import Models.Checkout
import MainSystem.MainApp
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.Node
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.geometry.HPos
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
		var deleteButtons = ListBuffer[Hyperlink]()
		
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
					GridPane.setConstraints(idLabels(itemRow-1), 0, itemRow)
					GridPane.setHalignment(idLabels(itemRow-1), HPos.CENTER)

					nameLabels  += new Label(_checkedOutItems.name.toString)
					GridPane.setConstraints(nameLabels(itemRow-1), 1, itemRow)
					GridPane.setHalignment(nameLabels(itemRow-1), HPos.CENTER)

					unitPriceLabels  += new Label(_checkedOutItems.price.toString)
					GridPane.setConstraints(unitPriceLabels(itemRow-1), 2, itemRow)
					GridPane.setHalignment(unitPriceLabels(itemRow-1), HPos.CENTER)

					qtyLabels  += new Label(_checkedOutItems.quantity.toString)
					GridPane.setConstraints(qtyLabels(itemRow-1), 3, itemRow)
					GridPane.setHalignment(qtyLabels(itemRow-1), HPos.CENTER)

					lineAmountLabels  += new Label(_checkedOutItems.lineAmount.toString)
					GridPane.setConstraints(lineAmountLabels(itemRow-1), 4, itemRow)
					GridPane.setHalignment(lineAmountLabels(itemRow-1), HPos.CENTER)

					var deleteButton = new Hyperlink ("Delete line")

					deleteButton.onAction = (e:ActionEvent) => {									
						if (Checkout.listOfCheckedoutItems.nonEmpty) {
							itemRow -= 1
							var rowToBeDeleted = deleteButtons.indexOf(deleteButton)
							var buttonIndex = checkoutGrid.children.indexOf(deleteButton)
							var rowIndex = GridPane.getRowIndex (deleteButton)
							println (rowIndex)
							//remove from labels array							
							idLabels.remove(rowToBeDeleted)
							nameLabels.remove(rowToBeDeleted)
							unitPriceLabels.remove(rowToBeDeleted)
							qtyLabels.remove(rowToBeDeleted)
							lineAmountLabels.remove(rowToBeDeleted)
							deleteButtons.remove(rowToBeDeleted)

							//delete column
							checkoutGrid.children.remove (buttonIndex-5)
							checkoutGrid.children.remove (buttonIndex-5)
							checkoutGrid.children.remove (buttonIndex-5)
							checkoutGrid.children.remove (buttonIndex-5)
							checkoutGrid.children.remove (buttonIndex-5)
							checkoutGrid.children.remove (buttonIndex-5)	

							for (elements <- checkoutGrid.children) {
								if (GridPane.getRowIndex(elements) >= rowIndex + 1)
									GridPane.setRowIndex(elements, GridPane.getRowIndex(elements) - 1)
							}						

							//remove from checkoutList
							Checkout.listOfCheckedoutItems.remove (rowToBeDeleted)
							

							if (Checkout.listOfCheckedoutItems.isEmpty) {
								checkoutButton.setVisible (false)
							}
						}						
					}					
					deleteButtons += deleteButton
					GridPane.setConstraints(deleteButtons(itemRow-1), 5, itemRow)
					GridPane.setHalignment(lineAmountLabels(itemRow-1), HPos.CENTER)

					checkoutGrid.getChildren().addAll (idLabels(itemRow-1), nameLabels(itemRow-1), unitPriceLabels(itemRow-1),
														qtyLabels(itemRow-1), lineAmountLabels(itemRow-1), deleteButtons(itemRow-1))
					
					if (checkoutButton.isVisible == false) {
						checkoutButton.setVisible (true)
					}
					itemRow += 1					
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
