package Database
import java.sql.{Connection,DriverManager}

trait myDBDetails{
    
    val url = "jdbc:mysql://localhost:3306/oop?useTimezone=true&serverTimezone=UTC"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "rootroot_0619"
    var connection:Connection = _

}

object myDBDetails{
    
    val url = "jdbc:mysql://localhost:3306/oop?useTimezone=true&serverTimezone=UTC"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "rootroot_0619"
    var connection:Connection = _

}