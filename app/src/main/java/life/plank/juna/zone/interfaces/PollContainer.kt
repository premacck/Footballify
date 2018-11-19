package life.plank.juna.zone.interfaces

import life.plank.juna.zone.data.model.poll.PollAnswerRequest

interface PollContainer {
    fun onPollSelected(pollAnswerRequest: PollAnswerRequest)
}