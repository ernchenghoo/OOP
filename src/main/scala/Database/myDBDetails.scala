package Database
import java.sql.{Connection,DriverManager}
import Models.Item
import Models.Itemstock
import Models.Stockedithistory
import Models.Branch
import Models.Sales
import Models.Itemsold
import Models.Account._
import Models.Returnitemhistory

trait myDBDetails{
    
    val url = "jdbc:h2:./myDB;"
    val driver = "org.h2.Driver"

    // val url = "jdbc:mysql://localhost:3306/oop?useTimezone=true&serverTimezone=UTC"
    // val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "root"
    var connection:Connection = _

}

object myDBDetails{
        
    val url = "jdbc:h2:./myDB;"
    val driver = "org.h2.Driver"

    // val url = "jdbc:mysql://localhost:3306/oop?useTimezone=true&serverTimezone=UTC"
    // val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "root"
    var connection:Connection = _

    def setupDB() = {
        // check item table if item table not initialized then initialize it
        if(!Item.hasInitialize()){
            Item.initializeTable()
        }

        //check itemstock table if itemstock not initialized then initilaize it
        if(!Itemstock.hasInitialize()){
            Itemstock.initializeTable()
        }

        //check stockedithistory table if stockedithistory not initialized then initilaize it
        if(!Stockedithistory.hasInitialize()){
            Stockedithistory.initializeTable()
        }

        //check Branch table if Branch not initialized then initilaize it
        if(!Branch.hasInitialize()){
            Branch.initializeTable()
        }
        //check Sales table if Sales not initialized then initialize it
        if(!Sales.hasInitialize()){
            Sales.initializeTable()
        }
        //check Itemsold table if Sales not initialized then initialize it
        if(!Itemsold.hasInitialize()){
            Itemsold.initializeTable()
        }


        //check Itemsold table if Sales not initialized then initialize it
        if(!Returnitemhistory.hasInitialize()){
            Returnitemhistory.initializeTable()
        }

        Account.setupAccountTable()


    }

    def createDB() {
        try {
            Class.forName(driver)
            val connection = DriverManager.getConnection(url, username, password)
            val createStatement = connection.createStatement()
            createStatement.executeUpdate("CREATE DATABASE IF NOT EXISTS oop")
        }      

    }
}