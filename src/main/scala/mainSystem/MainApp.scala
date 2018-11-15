package MainSystem

import java.sql.{Connection,DriverManager}
import Models.Item
import Models.branch
import Controllers.ItemeditdialogController
import Controllers.StockeditdialogController
import Controllers.BrancheditdialogController
import Controllers.SearchReportController
import Controllers.ReportController
import Controllers.ReturnitemController

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.stage.{Modality, Stage}
import java.time.LocalDate

object MainApp extends JFXApp {
  
  val rootResource = getClass.getResource("/Views/Shared/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load();
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  
  stage = new PrimaryStage {
    title = "POSsystem"
    scene = new Scene {
      root = roots
    }
  }
  def showMainMenu() = {
    val resource = getClass.getResource("/Views/Shared/mainMenu.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  } 
  showMainMenu()

  def goToCheckoutMenu() = {
      val resource = getClass.getResource("/Views/Checkout/CheckoutOverview.fxml")
      val loader = new FXMLLoader(resource, NoDependencyResolver)
      loader.load();
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      this.roots.setCenter(roots)
    } 

    def goToPaymentMenu() = {
      val resource = getClass.getResource("/Views/Checkout/PaymentOverview.fxml")
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
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showItemEditDialog(item: Item,addoredit: String): Boolean = {
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
    control.addoredit = addoredit
    control.initializeitemdata(item)
    dialog.showAndWait()

    control.okClicked
  } 

  def showStockEditDialog(addorminus: String): Boolean = {
    val resource = getClass.getResource("/Views/Inventory/Stockeditdialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[StockeditdialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.addorminus = addorminus
    control.initializedata()
    dialog.showAndWait()

    control.okClicked
  } 
  //Inventory end

  //Return Item
   def goToReturnItem() = {
    val resource = getClass.getResource("/Views/Return_Item/ReturnitemControl.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

    def showReturnItemDialog(addorminus: String): Boolean = {
    val resource = getClass.getResource("/Views/Return_Item/Returnitemeditdialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[StockeditdialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.addorminus = addorminus
    control.initializedata()
    dialog.showAndWait()

    control.okClicked
  } 
  //end Return Item

  //branch
  def goToBranchMain() = {
    val resource = getClass.getResource("/Views/Branch/Branchmain.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showBranchEditDialog(branch:branch,addoredit: String): Boolean = {
    val resource = getClass.getResource("/Views/Branch/Brancheditdialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[BrancheditdialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }

    control.dialogStage = dialog
    control.addoredit = addoredit
    control.initializedata(branch)
    dialog.showAndWait()

    control.okClicked
  } 
  //branch end

  //Report
  def goToSearchReport() = {
    val resource = getClass.getResource("/Views/Tracking_Report/SearchReport.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    val control = loader.getController[SearchReportController#Controller]
    this.roots.setCenter(roots)
    control.initializebranch()
  }

  def showReport(fromDate1:LocalDate,toDate1:LocalDate,branchdropdown1:String) = {
    val resource= getClass.getResource("/Views/Tracking_Report/Report.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val root2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[ReportController#Controller]

    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = root2
      }
    }
    control.fromDate = fromDate1
    control.toDate = toDate1
    control.from_date()
    control.to_date()
    control.setbranch(branchdropdown1)
    control.getReportlist(branchdropdown1)
    dialog.showAndWait()
  
  }


}