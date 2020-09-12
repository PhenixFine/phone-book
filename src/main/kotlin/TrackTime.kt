class TrackTime() {
    private var totalTime = 0L
    private var holdTime = 0L
    private val getTime = { System.currentTimeMillis() }

    fun start() {
        holdTime = getTime()
    }

    fun stop() {
        totalTime += getTime() - holdTime
    }

    fun elapsed() = totalTime

    fun resetTime() {
        totalTime = 0
    }
}