package com.example.schoolcontrol.ui.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.schoolcontrol.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RankingFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_ranking, container, false)
        MainScope().launch {
            // Could load ranking and populate UI
        }
        return v
    }
}
