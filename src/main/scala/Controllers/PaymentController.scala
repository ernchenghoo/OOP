package Controllers
import Database.myDBDetails
import Models.Sales
import Models.Itemsold
import Models.Checkout
import Models.Itemstock
import MainSystem.MainApp
import scalafxml.core.macros.sfxml
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.geometry.HPos
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@sfxml
class PaymentController (	
	private val paymentPane: GridPane,
	private val totalAmount: Label,
	private val changeAmount: Label,
	private val changeLabel: Label,
	private val checkoutComplete: Label,
	private val receivedAmount: TextField,
	private val paymentButtons: HBox,
	private val payButton: Button,	
	private val backButton: Button,
	private val paymentTable: TableView [Checkout],
    private val idCol: TableColumn [Checkout, Int],
    private val nameCol: TableColumn [Checkout, String],
    private val priceCol: TableColumn [Checkout, Double],
    private val qtyCol: TableColumn [Checkout, Int],
    private val lineAmountCol: TableColumn [Checkout, Double]

	) {
		var itemRow = 1
		var totalPaymentAmount: Double = 0
		var checkBranch:Int = 0

		for (elements <- Models.Checkout.listOfCheckedoutItems) {
			totalPaymentAmount += elements.lineAmount.value
		}

		totalPaymentAmount = BigDecimal(totalPaymentAmount).setScale(2,BigDecimal.RoundingMode.HALF_UP).toDouble

		totalAmount.text.value = totalPaymentAmount.toString

		paymentTable.items = Models.Checkout.listOfCheckedoutItems
		idCol.cellValueFactory = {_.value.id}
		nameCol.cellValueFactory = {_.value.name}
		priceCol.cellValueFactory = {_.value.price}
		qtyCol.cellValueFactory = {_.value.quantity}
		lineAmountCol.cellValueFactory = {_.value.lineAmount}		

		Sales.UpdateSaleslist()
		
		//initialize id
		var maxid = 0
		for(salesidlist <-Sales.Saleslist){
			if(salesidlist.salesid.getValue() > maxid){
				maxid = salesidlist.salesid.getValue()
			}
		}
		//new id
		maxid = maxid + 1
		var idinputbox:String = maxid.toString()

		def makePayment() = {
			var receivedPaymentAmount = receivedAmount.text.value.toDouble

			var salesid = idinputbox.toInt
			var datenow = new Date()
			var formmater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			// formmater.setTimeZone(TimeZone.getTimeZone("UTC"))
			var datestring = formmater.format(datenow)
			var total = totalPaymentAmount.getValue()
			
			if (receivedPaymentAmount > totalPaymentAmount){
				var changePaymentAmount = receivedPaymentAmount - totalPaymentAmount
				changePaymentAmount = BigDecimal(changePaymentAmount).setScale(2,BigDecimal.RoundingMode.HALF_UP).toDouble
				changeAmount.text.value = changePaymentAmount.toString
				changeAmount.setVisible (true)
				changeLabel.setVisible (true)
				checkoutComplete.setVisible (true)				
				paymentButtons.setVisible (true)
				Sales.addCheckout(salesid,checkBranch,datestring,total)

				for (elements <- Checkout.listOfCheckedoutItems){
					var itemid  = elements.id.value
					var itemname  = elements.name.value
					var quantity  = elements.quantity.value
					var price  = elements.price.value
					Itemsold.addItemsold(salesid, itemid, itemname, quantity, price)

					var checkquantity:Int = Itemstock.CheckItemQuantity(itemid,checkBranch)
					var quantityBalance:Int = checkquantity - quantity
					Itemstock.updateItemQuantity(itemid,checkBranch,quantityBalance)
					itemRow += 1
				}
				completedCheckout()
			}	

			else if (receivedPaymentAmount < totalPaymentAmount){
				val insufficientAmountAlert = new Alert(AlertType.Warning){
		          initOwner(MainApp.stage)
		          title       = "Insufficient Amount"
		          headerText = "Payment amount insufficient"
		          contentText  = "Please pay the correct total amount."
		        }
		        .showAndWait()
			}

			else {
				changeLabel.text.value = checkoutComplete.text.value
				changeLabel.setVisible (true)				
				paymentButtons.setVisible (true)
				Sales.addCheckout(salesid,checkBranch,datestring,total)

				for (elements <- Checkout.listOfCheckedoutItems){								
					var itemid  = elements.id.value
					var itemname  = elements.name.value
					var quantity  = elements.quantity.value
					var price  = elements.price.value
					Itemsold.addItemsold(salesid, itemid, itemname, quantity, price)

					var checkquantity:Int = Itemstock.CheckItemQuantity(itemid,checkBranch)
					var quantityBalance:Int = checkquantity - quantity

					Itemstock.updateItemQuantity(itemid,checkBranch,quantityBalance)
					itemRow += 1
				}
				completedCheckout()			
			}	
		}

		def backToCheckout() {
			MainApp.goToCheckoutMenu
		}

		def moveToMainMenu() {
			MainApp.showMainMenu			
		}

		def checkoutAgain() {
			MainApp.goToCheckoutMenu			
		}

		def completedCheckout () {
			Checkout.listOfCheckedoutItems.clear
			payButton.setDisable (true)
			backButton.setVisible (false)
			receivedAmount.setDisable (true)
		}

		




	
	
}