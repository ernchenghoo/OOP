package Database

import java.sql.{Connection,DriverManager}
import scalafx.collections.ObservableBuffer

object BranchDatabase{
	//for storing branchlist from database
	

	def CheckBranchId(location:String): Int = {

		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		val queryresult = statement.executeQuery(s"select * from branch where location =('${location}')")		
		var idfound:Int = 0

		while (queryresult.next){
			idfound=queryresult.getInt("branchid")			
		}	

		myDBDetails.connection.close()
		return idfound
	}
}