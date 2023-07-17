package com.google.ar.sceneform.samples.gltf

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.home_renovation).setOnClickListener {
            activity?.supportFragmentManager?.commit {
                add(
                    R.id.containerFragment,
                    MainFragment::class.java,
                    Bundle()
                ).addToBackStack("HomeFragment")
            }
        }
    }
}