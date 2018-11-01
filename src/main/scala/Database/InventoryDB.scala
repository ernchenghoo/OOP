package Database
import java.sql.{Connection,DriverManager}
import Models.Inventory
import scala.collection.mutable.MutableList

class InventoryDB{   

    try {
        Class.forName(myDBDetails.driver)
        myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username,
            myDBDetails.password)
        println("connection successful")
    }
    catch {
        case e: Exception => e.printStackTrace
    }

    val statement = myDBDetails.connection.createStatement
    val rs = statement.executeQuery("select * from inventory")
    var inventoryList: MutableList[Inventory] = MutableList()

    while (rs.next) {
        inventoryList += new Inventory (rs.getString("name"), rs.getInt("id"), rs.getDouble("price"))
    }

    def checkItemExistence(ID: String): Boolean = {
        inventoryList.filter(_.id.toString == ID).nonEmpty
    }

    myDBDetails.connection.close
}