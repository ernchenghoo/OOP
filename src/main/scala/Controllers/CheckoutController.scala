package Controllers
import Models.Checkout
import MainSystem.MainApp
import Models.Itemstock
import Models.Branch
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
    private val branchDropdown: ChoiceBox[String],   
    private val checkoutButton: Button,
    private val deleteLineButton: Button,    
    private val checkoutTable: TableView [Checkout],
    private val idCol: TableColumn [Checkout, Int],
    private val nameCol: TableColumn [Checkout, String],
    private val priceCol: TableColumn [Checkout, Double],
    private val qtyCol: TableColumn [Checkout, Int],
    private val lineAmountCol: TableColumn [Checkout, Double],    
	) {
		
		checkoutTable.items = Checkout.listOfCheckedoutItems
		idCol.cellValueFactory = {_.value.id}
		nameCol.cellValueFactory = {_.value.name}
		priceCol.cellValueFactory = {_.value.price}
		qtyCol.cellValueFactory = {_.value.quantity}
		lineAmountCol.cellValueFactory = {_.value.lineAmount}

		if (Checkout.listOfCheckedoutItems.nonEmpty) {
			checkoutButton.setVisible (true)
		}

		def initializebranch() = {
		//initialize branch choose
			branchDropdown.getItems().add("Select Branch");
			branchDropdown.setValue("Select Branch")
			for(branch <- Branch.getAllBranchs){
				branchDropdown.getItems().add(branch.location.getValue());
			}
		}
				
		
		def moveToMainMenu() = {			
			MainApp.showMainMenu()
		}		
		
		def moveToPayment() {	
			var checkBranch = Branch.CheckBranchId(branchDropdown.getValue())			
			MainApp.goToPaymentMenu(checkBranch)
		}

		def checkInputValues(): String = {
			if (searchItemID.text.value.isEmpty || quantityField.text.value.isEmpty || 
				branchDropdown.getValue() == "Select Branch") {				
				return "Incomplete"
			}
			else {
				try {
					searchItemID.text.value.toInt
					quantityField.text.value
				}
				catch {
					case e : NumberFormatException => {
						return "Invalid"									
					}					
				}
				return "Pass"
			}
		}
		def outOfStockAlert () {
			val insufficientStockAlert = new Alert(AlertType.Warning){
		        initOwner(MainApp.stage)
		        title       = "Out of Stock"
		        headerText = "This item is out of stock"
		        contentText  = "Please restock this item"
			}
			.showAndWait()
		}	

		def checkoutItem () {
			checkInputValues() match {
				case "Incomplete" => {
					val emptyAlert = new Alert(AlertType.Warning){
			        initOwner(MainApp.stage)
			        title       = "No Selection"
			        headerText = "Empty Field"
			        contentText  = "Input value incomplete."
					}
					.showAndWait()
				}
				case "Invalid" => {
					val emptyAlert = new Alert(AlertType.Warning){
			        initOwner(MainApp.stage)
			        title       = "Value Error"
			        headerText = "Incorrect value"
			        contentText  = "Please enter integers only."
					}
					.showAndWait()
				}
				case "Pass" => {
					//check item exist
					var itemExist = Checkout.verifyCheckoutItem (searchItemID.text.value.toInt)		
					
					itemExist match {
						case true => {//item exist	
							var checkBranchId = Branch.CheckBranchId(branchDropdown.getValue())				
							var checkStock:Int = Itemstock.CheckItemQuantity(searchItemID.text.value.toInt,checkBranchId)
							var duplicate = Checkout.checkDuplicate (searchItemID.text.value.toInt)
							var enoughStock = Checkout.checkStock (searchItemID.text.value.toInt, quantityField.text.value.toInt,
											checkBranchId)

							duplicate match {
								case true => {
									if (enoughStock) {
										Checkout.updateLineItem(searchItemID.text.value.toInt, quantityField.text.value.toInt)
									}
									else {
										outOfStockAlert()
									}
								}
								case false => {
									if (enoughStock) {
										Checkout.addCheckoutItem (searchItemID.text.value.toInt, quantityField.text.value.toInt)
										if (checkoutButton.isVisible == false){
											checkoutButton.setVisible (true)
										}
									}
									else {
										outOfStockAlert()
									}
								}
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

		def deleteLineItem () {
			if(checkoutTable.selectionModel().selectedItem.value != null) {
				Checkout.listOfCheckedoutItems.remove (checkoutTable.selectionModel().selectedIndex.value)
				if (Checkout.listOfCheckedoutItems.isEmpty){
					checkoutButton.setVisible (false)
				}
				
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
