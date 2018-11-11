package Database

import java.sql.{Connection,DriverManager}

object ReturnItemDatabase {

	def AddtoItemlist(itemid: Int,itemname: String,itemdesc: String,itemprice: Double) = {

		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		statement.executeUpdate(s"Insert into item Values(${itemid},'${itemname}','${itemdesc}',${itemprice})")	


		//new item added so need to initialize data stock to 0 in itemstock table
		//for all branch stock will have 0 stock of this newitem
		val queryresult = statement.executeQuery("select branchid from branch")
		while (queryresult.next){
			var branchid =  queryresult.getInt("branchid")
			val statement2 = myDBDetails.connection.createStatement
			statement2.executeUpdate(s"Insert into itemstock values(${itemid},${branchid},0)")
		}		

		myDBDetails.connection.close()
	
	}
}