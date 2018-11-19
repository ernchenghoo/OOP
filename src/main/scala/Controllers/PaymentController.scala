package Controllers
import Database.myDBDetails
import Database.CheckoutDatabase
import Models.Checkout
import MainSystem.MainApp
import scalafxml.core.macros.sfxml
import scalafx.scene.control._
import scalafx.scene.layout._
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
	private val paymentButtons: HBox	
	) {
		var itemRow = 1
		var totalPaymentAmount: Double = 0
		
		for (elements <- Checkout.listOfCheckedoutItems){				
			var rowLabel = new Label (itemRow.toString)					
			var idLabel  = new Label(elements.id.value.toString)
			var nameLabel  = new Label(elements.name.value)
			var priceLabel  = new Label(elements.price.value.toString)
			var qtyLabel  = new Label(elements.quantity.value.toString)
			var lineAmountLabel  = new Label(elements.lineAmount.value.toString)
			paymentPane.addRow (itemRow, rowLabel, idLabel, nameLabel, priceLabel, qtyLabel, lineAmountLabel)
			totalPaymentAmount += elements.lineAmount.value
			itemRow += 1
		}

		totalAmount.text.value = totalPaymentAmount.toString

		CheckoutDatabase.UpdateSaleslist()
		
		//initialize id
		var maxid = 0
		for(salesidlist <-CheckoutDatabase.Saleslist){
			if(salesidlist.salesid.getValue() > maxid){
				maxid = salesidlist.salesid.getValue()
			}
		}
		//new id
		maxid = maxid + 1
		var idinputbox:String = maxid.toString()

		def makePayment() {
			var receivedPaymentAmount = receivedAmount.text.value.toDouble

			var salesid = idinputbox.toInt
			var branchid = 1
			var datenow = new Date()
			var formmater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			formmater.setTimeZone(TimeZone.getTimeZone("UTC"))
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
				CheckoutDatabase.addCheckout(salesid,branchid,datestring,total)

				for (elements <- Checkout.listOfCheckedoutItems){								
					var itemid  = elements.id.value
					var itemname  = elements.name.value
					var quantity  = elements.quantity.value
					var price  = elements.price.value
					CheckoutDatabase.addItemsold(salesid, itemid, itemname, quantity, price)
					itemRow += 1
				}

				Checkout.listOfCheckedoutItems.clear
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
				CheckoutDatabase.addCheckout(salesid,branchid,datestring,total)

				for (elements <- Checkout.listOfCheckedoutItems){								
					var itemid  = elements.id.value
					var itemname  = elements.name.value
					var quantity  = elements.quantity.value
					var price  = elements.price.value
					CheckoutDatabase.addItemsold(salesid, itemid, itemname, quantity, price)
					itemRow += 1
				}
				Checkout.listOfCheckedoutItems.clear
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

		




	
	
}