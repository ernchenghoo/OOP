package Controllers
import Models.Checkout
import scalafxml.core.macros.sfxml
import scalafx.scene.control._
import scalafx.scene.layout._

@sfxml
class PaymentController (
	private val paymentGrid: GridPane
	) {
		var labelNum = 0
		println (Checkout.listOfCheckedoutItems.size)
		//for (elements <- Checkout.listOfCheckedoutItems)
		//	paymentGrid.addRow ()


	
	
}