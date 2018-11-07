package MainSystem

import Models.Item
import Controllers.ItemeditdialogController

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.stage.{Modality, Stage}


object MainApp extends JFXApp {
  
  val rootResource = getClass.getResource("/Views/Shared/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load();
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  
  stage = new PrimaryStage {
    title = "AddressApp"
    scene = new Scene {
      root = roots
    }
  }
  def showPersonOverview() = {
    val resource = getClass.getResource("/Views/Shared/mainMenu.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  } 
  showPersonOverview()

  def goToCheckoutMenu() = {
      val resource = getClass.getResource("/Views/Checkout/CheckoutOverview.fxml")
      val loader = new FXMLLoader(resource, NoDependencyResolver)
      loader.load();
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      this.roots.setCenter(roots)
    } 

  //Inventory
  def goToInventoryMain() = {
    val resource = getClass.getResource("/Views/Inventory/Inventorymain.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.FlowPane]
    this.roots.setCenter(roots)
  }

  def showItemEditDialog(item: Item): Boolean = {
    val resource = getClass.getResource("/Views/Inventory/Itemeditdialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[ItemeditdialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.initializeitemdata(item)
    dialog.showAndWait()

    control.okClicked
  } 
}