package Database
import java.sql.{Connection,DriverManager}
import scala.collection.mutable.Set

class InventoryDB{   

    val myDatabaseDetails = new myDBDetails

    try {
        Class.forName(myDatabaseDetails.driver)
        myDatabaseDetails.connection = DriverManager.getConnection(myDatabaseDetails.url, myDatabaseDetails.username,
            myDatabaseDetails.password)
        println("connection successful")
    }
    catch {
        case e: Exception => e.printStackTrace
    }

    val statement = myDatabaseDetails.connection.createStatement
    val rs = statement.executeQuery("select * from inventory")
    var idSet = Set[String]()
    while (rs.next) {
         var id = rs.getInt("id").toString
         idSet.add(id)
    }

    def checkItemExistence(ID: String): Boolean = {
        idSet.contains(ID)
    }

    myDatabaseDetails.connection.close
}