package Controllers

import MainSystem.MainApp
import Database.BranchDatabase
import Database.CheckoutDatabase
import Models.itemsold

import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.scene.control.{ChoiceBox,Label,DatePicker, Alert,TableColumn,TableView,TableCell}
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.stage.Stage
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary
import java.util.Date
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.sql.Date
import java.time.LocalDate


@sfxml
class ReportController (
	 val fromDatelabel : Label,
     val toDatelabel : Label,  
     val branchdropdownlabel : Label,
     val totalSaleslabel : Label,
     val reportTableView: TableView[itemsold],
     val itemIdColumn: TableColumn[itemsold, Int],
     val itemNameColumn: TableColumn[itemsold, String],
     val quantityColumn: TableColumn[itemsold, Int],
     val priceColumn: TableColumn[itemsold, Double]
	) {
		var fromDate:LocalDate = null
		var toDate:LocalDate = null

		def from_date() = {
			var date = java.sql.Date.valueOf(fromDate)
			var formmater = new SimpleDateFormat("dd-MM-yyyy")
			//formmater.setTimeZone(TimeZone.getTimeZone("UTC"))
			var datestring = formmater.format(date)
			fromDatelabel.setText(datestring)
		}
		
		def to_date() = {
			var date = java.sql.Date.valueOf(toDate)
			var formmater = new SimpleDateFormat("dd-MM-yyyy")
			//formmater.setTimeZone(TimeZone.getTimeZone("UTC"))
			var datestring = formmater.format(date)
			toDatelabel.setText(datestring)
		}

		def setbranch(branch:String) = {
			branchdropdownlabel.setText(branch)
		}



		def getReportlist(branch:String) = {
			var date = java.sql.Date.valueOf(fromDate)
			var formmater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			// formmater.setTimeZone(TimeZone.getTimeZone("UTC"))
			var datestring = formmater.format(date)

			var date1 = java.sql.Date.valueOf(toDate)
			var formmater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			// formmater1.setTimeZone(TimeZone.getTimeZone("UTC"))
			var datestring1 = formmater.format(date1)

			var branch_id = BranchDatabase.CheckBranchId(branch)
			var totalSales = CheckoutDatabase.UpdateReportlist(datestring,datestring1,branch_id)
			
			totalSaleslabel.setText(totalSales.toString())
			//show to tableview
			reportTableView.items = CheckoutDatabase.Reportlist

			// initialize columns's cell values
		  	itemIdColumn.cellValueFactory = {_.value.itemid}
		  	itemNameColumn.cellValueFactory = {_.value.itemname}
		  	quantityColumn.cellValueFactory = {_.value.quantity}
		  	priceColumn.cellValueFactory = {_.value.price}
		}
		
	
}	