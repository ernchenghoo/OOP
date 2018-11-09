package Database

import Models.branch

import java.sql.{Connection,DriverManager}
import scalafx.collections.ObservableBuffer

object BranchDatabase{
	//for storing branchlist from database
	var Branchlist: ObservableBuffer[branch] = new ObservableBuffer[branch]()

	//for branchlist function
	def UpdateBranchlist() = {
		Branchlist.clear()

		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		val queryresult = statement.executeQuery("select * from branch")		

		while (queryresult.next){
			var branchid = queryresult.getInt("branchid")
			var location = queryresult.getString("location")

			var branchobject = new branch(branchid,location)

			Branchlist += branchobject
		}	

		myDBDetails.connection.close()
	}

	def DeletefromBranchlist(branchid: Int) = {
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		//delete from branch table
		statement.executeUpdate(s"Delete from branch where branchid=${branchid}")	


		//delete from itemstock
		val statement2 = myDBDetails.connection.createStatement
		statement2.executeUpdate(s"Delete from itemstock where branchid=${branchid}")	


		myDBDetails.connection.close()
	}

	def EditfromBranchlist(branchid: Int, location: String) = {
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		statement.executeUpdate(s"Update branch set location='${location}' where branchid='${branchid}'")	

		myDBDetails.connection.close()
		UpdateBranchlist()
	}

	def AddtoBranchlist(branchid: Int, location: String) = {
		Class.forName(myDBDetails.driver)
		myDBDetails.connection = DriverManager.getConnection(myDBDetails.url, myDBDetails.username, myDBDetails.password)
		val statement = myDBDetails.connection.createStatement
		statement.executeUpdate(s"Insert into branch Values(${branchid},'${location}')")	


		//new branch added so need to initialize data stock to 0 in itemstock table
		//all item stock of new branch will have 0 stock 
		val queryresult = statement.executeQuery("select itemid from item")
		while (queryresult.next){
			var itemid =  queryresult.getInt("itemid")
			val statement2 = myDBDetails.connection.createStatement
			statement2.executeUpdate(s"Insert into itemstock values(${itemid},${branchid},0)")
		}		

		myDBDetails.connection.close()
		UpdateBranchlist()
	}

	//branchlist function end
}