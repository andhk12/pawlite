package com.example.pawlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HealthDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_VET_VISIT = "extra_vet_visit"
        const val EXTRA_WEIGHT = "extra_weight"
        const val EXTRA_VACCINES = "extra_vaccines"
    }

    private lateinit var etVetVisit: EditText
    private lateinit var etWeight: EditText
    private lateinit var etVaccines: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_detail)

        etVetVisit = findViewById(R.id.etVetVisit)
        etWeight = findViewById(R.id.etWeight)
        etVaccines = findViewById(R.id.etVaccines)
        val btnUpdate: Button = findViewById(R.id.btn_update_health)

        // Ambil data dari Intent dan tampilkan
        val vetVisit = intent.getStringExtra(EXTRA_VET_VISIT)
        val weight = intent.getStringExtra(EXTRA_WEIGHT)
        val vaccines = intent.getStringExtra(EXTRA_VACCINES)

        etVetVisit.setText(vetVisit)
        etWeight.setText(weight)
        etVaccines.setText(vaccines)

        btnUpdate.setOnClickListener {
            updateHealthData()
        }
    }

    private fun updateHealthData() {
        val updatedVetVisit = etVetVisit.text.toString().trim()
        val updatedWeight = etWeight.text.toString().trim()
        val updatedVaccines = etVaccines.text.toString().trim()

        if (updatedVetVisit.isEmpty() || updatedWeight.isEmpty() || updatedVaccines.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val resultIntent = Intent().apply {
            putExtra(EXTRA_VET_VISIT, updatedVetVisit)
            putExtra(EXTRA_WEIGHT, updatedWeight)
            putExtra(EXTRA_VACCINES, updatedVaccines)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}