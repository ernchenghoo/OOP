package OOP

object mainApp extends App {
  val tiger = new Mammal (true, "tiger", Features(4, 50, 40))
  
  println (tiger.animalFeatures)
}
