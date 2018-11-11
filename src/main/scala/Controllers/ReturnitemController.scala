package Controllers

import MainSystem.MainApp

import scalafx.scene.layout._
import scalafxml.core.macros.sfxml
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}

@sfxml
class ReturnitemController (val anchorpanetab: AnchorPane) {

	def backtoMainmenu() = {
		MainApp.showPersonOverview()

	}
}
