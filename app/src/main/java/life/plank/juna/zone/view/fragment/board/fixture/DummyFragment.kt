package life.plank.juna.zone.view.fragment.board.fixture


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import life.plank.juna.zone.R

class DummyFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dummy, container, false)
    }


}
