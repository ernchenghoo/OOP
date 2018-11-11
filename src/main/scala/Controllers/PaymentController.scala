package Controllers
import Database.myDBDetails
import Models.Checkout
import MainSystem.MainApp
import scalafxml.core.macros.sfxml
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType

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
			var idLabel  = new Label(elements.id.toString)
			var nameLabel  = new Label(elements.name.toString)
			var priceLabel  = new Label(elements.price.toString)
			var qtyLabel  = new Label(elements.quantity.toString)
			var lineAmountLabel  = new Label(elements.lineAmount.toString)
			paymentPane.addRow (itemRow, rowLabel, idLabel, nameLabel, priceLabel, qtyLabel, lineAmountLabel)
			totalPaymentAmount += elements.lineAmount
			itemRow += 1
		}

		totalAmount.text.value = totalPaymentAmount.toString

		def makePayment() {
			var receivedPaymentAmount = receivedAmount.text.value.toDouble
			
			if (receivedPaymentAmount > totalPaymentAmount){
				var changePaymentAmount = receivedPaymentAmount - totalPaymentAmount
				changePaymentAmount = BigDecimal(changePaymentAmount).setScale(2,BigDecimal.RoundingMode.HALF_UP).toDouble
				changeAmount.text.value = changePaymentAmount.toString
				changeAmount.setVisible (true)
				changeLabel.setVisible (true)
				checkoutComplete.setVisible (true)				
				paymentButtons.setVisible (true)
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

			}
			
		}

		def moveToMainMenu() {
			MainApp.showMainMenu
			Checkout.listOfCheckedoutItems.clear
		}

		def checkoutAgain() {
			MainApp.goToCheckoutMenu
			Checkout.listOfCheckedoutItems.clear	
		}

		




	
	
}