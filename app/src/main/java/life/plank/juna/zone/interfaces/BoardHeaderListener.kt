package life.plank.juna.zone.interfaces

interface BoardHeaderListener {

    fun onMatchTimeStateChange() = Unit

    fun onShareClick()
}