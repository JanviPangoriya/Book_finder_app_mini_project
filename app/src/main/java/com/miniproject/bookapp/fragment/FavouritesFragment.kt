package com.miniproject.bookapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.miniproject.bookapp.R

class FavouritesFragment : Fragment() {

    lateinit var rlNoFavContent: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_favourites, container, false)

        rlNoFavContent = view.findViewById(R.id.rlNoFavContent)
        rlNoFavContent.visibility = View.VISIBLE

        return view
    }

}