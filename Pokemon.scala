case class Stat(hp: Int, attack: Int, defense: Int, spAttack: Int, spDefense: Int, Speed: Int)
{
	
}

case class Breeding(eggGroup: String, malePercentage: Double, femalePercentage: Double, eggCycles: Int)
{

}

trait PokemonCharacteristic
	val name: String
	val pokemonType: Iterable[String] 
	val species: String 
	val evolution: String 
	val baseStat: Stat 
	val breeding: Breeding 	
}

//Method 1
class Pokemon(var givenName: String, var height: Double, var weight: Double, var ability: Iterable[String],
 val gender: String, var currentStat: Stat)
{

}

class Bulbasaur(a: String, b: Double, c: Double, d: Iterable[String], e: String, f: Stat) extends Pokemon(a, b, c, d, e, f)
{

}

//Method 2
abstract class Pokemon()
{
	var givenName: String
 	var height: Double
  	var weight: Double
   	var ability: Iterable[String],
 	val gender: String
  	var currentStat: Stat
}


class Bulbasaur( var givenName: String, var height: Double, var weight: Double, var ability: Iterable[String],
val gender: String, var currentStat: Stat) extends Pokemon()
{

}




object Bulbasaur extends PokemonCharacteristic
{
	val name: String = "Bulbasaur"
	val pokemonType: Iterable[String] = Array("Grass", "Poison")
	val species: String = "Seed Pokemon"
	val evolution: String = "Ivysaur"
	val baseStat: Stat = new Stat(40, 49, 49, 69, 45)
	val breeding: Breeding = new Breeding("Seed Pokemon", 87.5, 12,5, 20)
}

val skill: Iterable[String] = Array("Overgrow", "Chlorophyll")

val a = new Bulbasaur("MyBaby", 0.71, 6.9, skill, "male", Bulbasaur.baseStat)

