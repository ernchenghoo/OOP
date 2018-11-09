package Models 

import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}

class branch(_branchid: Int, _location:String){
	var branchid = ObjectProperty[Int](_branchid)
	var location = new StringProperty(_location)
}