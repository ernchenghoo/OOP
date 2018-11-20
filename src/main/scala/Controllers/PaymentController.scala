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
	private val payButton: Button	

	) {
		var itemRow = 1
		var totalPaymentAmount: Double = 0
		
		for (elements <- Checkout.listOfCheckedoutItems){				
			var rowLabel = new Label (itemRow.toString)	
			//GridPane.setHalignment (rowLabel, HPos.Center)
			var idLabel  = new Label(elements.id.value.toString)
			//GridPane.setHalignment (idLabel, HPos.Center)
			var nameLabel  = new Label(elements.name.value)
			//GridPane.setHalignment (nameLabel, HPos.Center)
			var priceLabel  = new Label(elements.price.value.toString)
			//GridPane.setHalignment (priceLabel, HPos.Center)
			var qtyLabel  = new Label(elements.quantity.value.toString)
			//GridPane.setHalignment (qtyLabel, HPos.Center)
			var lineAmountLabel  = new Label(elements.lineAmount.value.toString)
			//GridPane.setHalignment (lineAmountLabel, HPos.Center)
			paymentPane.addRow (itemRow, rowLabel, idLabel, nameLabel, priceLabel, qtyLabel, lineAmountLabel)
			  
			totalPaymentAmount += elements.lineAmount.value
			itemRow += 1
		}

		totalAmount.text.value = totalPaymentAmount.toString

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
				Sales.addCheckout(salesid,branchid,datestring,total)

				for (elements <- Checkout.listOfCheckedoutItems){
					var itemid  = elements.id.value
					var itemname  = elements.name.value
					var quantity  = elements.quantity.value
					var price  = elements.price.value
					Itemsold.addItemsold(salesid, itemid, itemname, quantity, price)

					var checkquantity:Int = Itemstock.CheckItemQuantity(itemid)
					var quantityBalance:Int = checkquantity - quantity
					var branchid:Int = 1
					Itemstock.updateItemQuantity(itemid,branchid,quantityBalance)
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
				Sales.addCheckout(salesid,branchid,datestring,total)

				for (elements <- Checkout.listOfCheckedoutItems){								
					var itemid  = elements.id.value
					var itemname  = elements.name.value
					var quantity  = elements.quantity.value
					var price  = elements.price.value
					Itemsold.addItemsold(salesid, itemid, itemname, quantity, price)

					var checkquantity:Int = Itemstock.CheckItemQuantity(itemid)
					var quantityBalance:Int = checkquantity - quantity
					var branchid:Int = 1

					Itemstock.updateItemQuantity(itemid,branchid,quantityBalance)
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
			receivedAmount.setDisable (true)
		}

		




	
	
}