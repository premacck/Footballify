package life.plank.juna.zone.view.fragment.board.fixture


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_clap_board.*
import kotlinx.android.synthetic.main.item_dart_board.*
import life.plank.juna.zone.R

class PrematchInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prematch_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clap_button.setOnClickListener {
            //TODO: Replace with actual value of claps
            claps_count.text = (claps_count.text.toString().toInt() + 1).toString()
        }

        throw_dart_button.setOnClickListener {
            darts_count.text = (darts_count.text.toString().toInt() + 1).toString()
        }
    }
}
