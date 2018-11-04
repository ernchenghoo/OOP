package Controllers
import Database.myDBDetails
import MainSystem.MainApp
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.event._
import scalafx.beans.property.{StringProperty}
import javafx.{scene => jfxs}
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import java.sql.{Connection,DriverManager}
import scalafx.scene.control.Alert.AlertType


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
		var itemRow: Int = 1					

		def moveToMainMenu() = {
			MainApp.showPersonOverview()
			myDBDetails.connection.close
		}
		//create new column in grid pane and add labels containing all item information
		def addCheckoutItem (item: Array[Any]) {
			checkoutGrid.addRow(itemRow)
			var lineAmount: Double = item(2).toString.toDouble * quantityField.text.value.toDouble			
			checkoutGrid.add (new Label (item(1).toString),0,itemRow)
			checkoutGrid.add (new Label(item(0).toString),1,itemRow)
			checkoutGrid.add (new Label(item(2).toString),2,itemRow)
			checkoutGrid.add (new Label(quantityField.text.value.toString),3,itemRow)
			checkoutGrid.add (new Label(lineAmount.toString),4,itemRow)	
			itemRow += 1		
		}
		
		def checkoutItem = {	
			//query for comparing item in database			
			val queryValue = statement.executeQuery("select * from inventory where id = '"+ searchItemID.text.value+ "'" )			
			
			//if item found in database
		    if (queryValue.next && quantityField.text.value.nonEmpty ) {
				var checkoutItem = Array (queryValue.getString("name"), queryValue.getInt("id"), queryValue.getDouble("price"))
				addCheckoutItem(checkoutItem)
		    }
		    //if either textfield is empty
		    else if (searchItemID.text.value.isEmpty || quantityField.text.value.isEmpty ){
		        val emptyAlert = new Alert(AlertType.Warning){
		          initOwner(MainApp.stage)
		          title       = "No Selection"
		          headerText = "Empty Field"
		          contentText  = "Input value incomplete"
		        }.showAndWait()
		    }	
		    else {
				  val NotExistAlert = new Alert(AlertType.Warning){
		          initOwner(MainApp.stage)
		          title       = "Not Found"
		          headerText = "Item Not Found"
		          contentText  = "Item does not exist"
		        }.showAndWait()
		    }	

		}		
}	
