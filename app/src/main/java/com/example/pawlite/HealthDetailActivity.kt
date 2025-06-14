package com.example.pawlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult

class HealthDetailFragment : Fragment() {

    private lateinit var etVetVisit: EditText
    private lateinit var etWeight: EditText
    private lateinit var etVaccines: EditText

    companion object {
        const val REQUEST_KEY = "health_update_request"
        const val EXTRA_VET_VISIT = "extra_vet_visit"
        const val EXTRA_WEIGHT = "extra_weight"
        const val EXTRA_VACCINES = "extra_vaccines"

        fun newInstance(vetVisit: String, weight: String, vaccines: String): HealthDetailFragment {
            val fragment = HealthDetailFragment()
            fragment.arguments = bundleOf(
                EXTRA_VET_VISIT to vetVisit,
                EXTRA_WEIGHT to weight,
                EXTRA_VACCINES to vaccines
            )
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Gunakan layout yang sama dengan activity sebelumnya
        return inflater.inflate(R.layout.activity_health_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etVetVisit = view.findViewById(R.id.etVetVisit)
        etWeight = view.findViewById(R.id.etWeight)
        etVaccines = view.findViewById(R.id.etVaccines)
        val btnUpdate: Button = view.findViewById(R.id.btn_update_health)
        val backButton: View = view.findViewById(R.id.backButton)

        // Ambil data dari arguments
        arguments?.let {
            etVetVisit.setText(it.getString(EXTRA_VET_VISIT))
            etWeight.setText(it.getString(EXTRA_WEIGHT))
            etVaccines.setText(it.getString(EXTRA_VACCINES))
        }

        btnUpdate.setOnClickListener {
            updateHealthData()
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun updateHealthData() {
        val updatedVetVisit = etVetVisit.text.toString().trim()
        val updatedWeight = etWeight.text.toString().trim()
        val updatedVaccines = etVaccines.text.toString().trim()

        if (updatedVetVisit.isEmpty() || updatedWeight.isEmpty() || updatedVaccines.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Kirim hasil kembali ke JournalFragment menggunakan Fragment Result API
        setFragmentResult(REQUEST_KEY, bundleOf(
            EXTRA_VET_VISIT to updatedVetVisit,
            EXTRA_WEIGHT to updatedWeight,
            EXTRA_VACCINES to updatedVaccines
        ))

        // Kembali ke fragment sebelumnya
        parentFragmentManager.popBackStack()
    }
}