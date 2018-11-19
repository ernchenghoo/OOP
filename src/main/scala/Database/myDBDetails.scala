package Database
import java.sql.{Connection,DriverManager}

trait myDBDetails{
    
    val url = "jdbc:mysql://localhost:3306/oop?useTimezone=true&serverTimezone=UTC"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "root"
    var connection:Connection = _

}

object myDBDetails{
    
    val url = "jdbc:mysql://localhost:3306/oop?useTimezone=true&serverTimezone=UTC"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "root"
    var connection:Connection = _

}