package MainSystem

import java.sql.{Connection,DriverManager}
import Models.Item
import Models.Branch
import Models.Account._
import Database.myDBDetails
import Controllers._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.stage.{Modality, Stage}
import java.time.LocalDate
import scalafx.scene.image.Image

object MainApp extends JFXApp {
  //initialize table
  // myDBDetails.createDB()  
  myDBDetails.setupDB()
  
  var user: User = new User()
  val rootResource = getClass.getResource("/Views/Shared/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load();
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  
  stage = new PrimaryStage {
    title = "POSsystem"
    icons += new Image(getClass.getResourceAsStream("/Images/logo.png"))
    scene = new Scene {
      root = roots
    }
  }

  goToLoginPage()

  def showMainMenu() = {
    val resource = getClass.getResource("/Views/Shared/mainMenu.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    val control = loader.getController[MainMenuController#Controller]
    user.initialize(control)
    this.roots.setCenter(roots)
  } 


  def goToCheckoutMenu() = {
      val resource = getClass.getResource("/Views/Checkout/CheckoutOverview.fxml")
      val loader = new FXMLLoader(resource, NoDependencyResolver)
      loader.load();
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      val control = loader.getController[CheckoutController#Controller]
      this.roots.setCenter(roots)
      control.initializebranch()
    } 

    def goToPaymentMenu(branchid:Int) = {
      val resource = getClass.getResource("/Views/Checkout/PaymentOverview.fxml")
      val loader = new FXMLLoader(resource, NoDependencyResolver)
      loader.load();
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      val control = loader.getController[PaymentController#Controller]
      this.roots.setCenter(roots)
      control.checkBranch = branchid
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
    control.IteminDialog = item
    control.initializeitemdata()
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
    val control = loader.getController[ReturnitemeditdialogController#Controller]

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

  def showBranchEditDialog(branch:Branch,addoredit: String): Boolean = {
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
    control.branchinDialog = branch
    control.initializedata()
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

  //Account
  def goToLoginPage() = {
      val resource = getClass.getResource("/Views/Account/LoginPage.fxml")
      val loader = new FXMLLoader(resource, NoDependencyResolver)
      loader.load();
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      this.roots.setCenter(roots)
      
    }

  def goToManageAccount() = {
      val resource = getClass.getResource("/Views/Account/ManageAccount.fxml")
      val loader = new FXMLLoader(resource, NoDependencyResolver)
      loader.load();
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      this.roots.setCenter(roots)
      
    }

  def goToAccountPage(selectedIndex: Int, action: String) = {
      val resource = getClass.getResource("/Views/Account/AccountPage.fxml")
      val loader = new FXMLLoader(resource, NoDependencyResolver)
      loader.load();
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      val control = loader.getController[AccountPageController#Controller]
      control.action = action
      control.selectedIndex = selectedIndex
      if(action == "Create")
      {
        control.doubleActionButton.text = "Create Account"
      }
      else
      {
        control.doubleActionButton.text = "Update Account"
        control.loadData()
      }
      
      this.roots.setCenter(roots)
      
    }



}