package OOP

class Animal (val species: String, var animalFeatures: Features) {

}

case class Features (val numLegs: Int, var weight: Double, var height: Double)

class Bird (val canFly: Boolean, val _species: String, val hasFeathers: Boolean, var _animalFeatures: Features) extends 
            Animal (_species, _animalFeatures) with Wings {
  
  
}

class Mammal (val canSwim: Boolean, val _species: String, var _animalFeatures: Features) extends Animal (_species, _animalFeatures) {

}

trait Wings {
  val hasFeathers: Boolean
  var noFeathers: Boolean = !hasFeathers
  

}


