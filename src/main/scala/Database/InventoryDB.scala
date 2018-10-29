package Database
import java.sql.{Connection,DriverManager}

class InventoryDB{   

    val myDatabaseDetails = new myDBDetails

    def connectToDB() {
        try {
            Class.forName(myDatabaseDetails.driver)
            myDatabaseDetails.connection = DriverManager.getConnection(myDatabaseDetails.url, myDatabaseDetails.username, 
                myDatabaseDetails.password)
            
            val statement = myDatabaseDetails.connection.createStatement
            val rs = statement.executeQuery("select * from inventory")
            while (rs.next) {
                var name = rs.getString("name")
                var id = rs.getInt("id")
                var price = rs.getFloat("price")
                println(name, id, price)
            }
            println("connection successful")
        } 
        catch {
            case e: Exception => e.printStackTrace
        }
        myDatabaseDetails.connection.close
    }   
}