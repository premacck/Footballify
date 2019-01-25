package life.plank.juna.zone.ui.board.fragment.fixture

import life.plank.juna.zone.data.model.board.poll.PollAnswerRequest

interface PollContainer {
    fun onPollSelected(pollAnswerRequest: PollAnswerRequest)
}