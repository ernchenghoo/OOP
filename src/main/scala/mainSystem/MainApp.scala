package MainSystem
import java.sql.{Connection,DriverManager}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}

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

}