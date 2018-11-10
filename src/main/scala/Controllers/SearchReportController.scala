package Controllers

import MainSystem.MainApp

import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.stage.Stage
import scalafx.scene.control.Alert.AlertType //contail all the implicits to change javafx classes to scalafx classes as necessary


@sfxml
class SearchReportController () {	

	var  dialogStage : Stage  = null
	var  okClicked = false

	def backtoMainmenu() = {
		MainApp.showPersonOverview()
	}

	def handleReport() = {
		val okClicked: Boolean = MainApp.showReport()
	}
}	