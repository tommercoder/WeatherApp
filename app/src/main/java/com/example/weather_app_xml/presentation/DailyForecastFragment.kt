package com.example.weather_app_xml.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.weather_app_xml.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DailyForecastFragment : DialogFragment() {
    //private lateinit var binding: DatailedDailyForecastBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create and return the dialog here
        val inflater = LayoutInflater.from(context)
        val customLayout = inflater.inflate(R.layout.datailed_daily_forecast, null)
        //binding = DatailedDailyForecastBinding.inflate(inflater, container, false)
       // val view = binding.root

        val dialog = Dialog(requireContext())
        dialog.setContentView(customLayout)

        return dialog
    }
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//
//       // binding.maxTemperature.text = "R.string.forecast_text"
//        return view
//    }
}