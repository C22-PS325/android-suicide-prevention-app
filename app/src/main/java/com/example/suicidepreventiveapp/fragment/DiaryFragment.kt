package com.example.suicidepreventiveapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.suicidepreventiveapp.R
import com.example.suicidepreventiveapp.adapter.CardAdapter
import com.example.suicidepreventiveapp.data.DiaryCard
import com.example.suicidepreventiveapp.databinding.FragmentDiaryBinding

class DiaryFragment : Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private val arrayCard = ArrayList<DiaryCard>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        _binding = FragmentDiaryBinding.inflate(inflater, container, false)

//        val textView: TextView = binding.textNotifications
//        diaryViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDiaryBinding.bind(view)

        arrayCard.addAll(listCards)
        binding.rvCards.apply {
            layoutManager = LinearLayoutManager(activity)
            val listCardAdapter = CardAdapter(arrayCard)
            adapter = listCardAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val listCards: ArrayList<DiaryCard>
    get() {
        val dataTitle = resources.getStringArray(R.array.card_title)
        val dataImage = resources.obtainTypedArray(R.array.card_photo)
        val listCard = ArrayList<DiaryCard>()
        for (i in dataTitle.indices) {
            val card = DiaryCard(dataTitle[i], dataImage.getResourceId(i, -1))
            listCard.add(card)
        }
        return listCard
    }
}