package Controllers

import MainSystem.MainApp
import Database.BranchDatabase

import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafx.scene.control.{ChoiceBox,Label,DatePicker, Alert}
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
		
	
}	