// create a map with a few states you have visited. Key is the state code, value is the place you visited.

val statesToLocation = collection.immutable.Map(
  "CO" -> "Rocky Mountains",
  "IL" -> "Sears Tower",
  "AZ" -> "Grand Canyon",
  "OR" -> "Pacific Ocean"
)

// create list of state codes in the order visited
val statesVisited = List("IL", "CO", "AZ", "OR", "NY")

// convert list of state codes to list of places visited
statesVisited.flatMap(stateVisited => statesToLocation.get(stateVisited))

val theLocation = statesToLocation.get("ZZ")
theLocation.isDefined
theLocation.isEmpty
