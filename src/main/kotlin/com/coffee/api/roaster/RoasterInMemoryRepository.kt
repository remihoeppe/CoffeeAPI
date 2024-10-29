package com.coffee.api.roaster


class RoasterInMemoryRepository : RoasterRepository {
    private var roasters = mutableListOf(
        Roaster("Monmouth Coffee Company", "https://www.monmouthcoffee.co.uk/", "123 Street"),
        Roaster("Square Mile Coffee Roasters", "https://shop.squaremilecoffee.com/", "123 Street"),
        Roaster("Skylark Coffee", "https://skylark.coffee/", "123 Street"),
        Roaster("Grindsmith", "https://grindsmith.com/", "123 Street"),
        Roaster("Curve Coffee", "https://www.curveroasters.co.uk/", "123 Street"),
    )

    override fun allRoasters(): List<Roaster> = roasters

    override fun roasterByName(name: String) = roasters.find {
        it.name.equals(name, ignoreCase = true)
    }

    override fun roasterById(id: String): Roaster? {
        TODO("Not yet implemented")
    }

    override fun addRoaster(newRoaster: Roaster) {
        if (roasterByName(newRoaster.name) != null) {
            throw IllegalStateException("This roaster already exists")
        } else {
            roasters.add(newRoaster)
        }
    }

    override fun removeRoaster(name: String): Boolean {
        return roasters.removeIf { it.name == name }
    }
}
