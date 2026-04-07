package com.example.rpgcharactergenerator

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.rpgcharactergenerator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // using viewModels so the data stays around better during rotation
    private val viewModel: CharacterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClassSpinner()
        setupSpeciesSpinner()
        setupGenderRadioGroup()
        setupNameInput()
        setupSeekBars()
        observeViewModel()
    }

    private fun setupClassSpinner() {
        val classAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            viewModel.classOptions
        )
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerClass.adapter = classAdapter

        binding.spinnerClass.setSelection(viewModel.selectedClassIndex.value ?: 0)

        binding.spinnerClass.setOnItemSelectedListener(
            SimpleItemSelectedListener { position ->
                viewModel.setCharacterClass(position)
            }
        )
    }

    private fun setupSpeciesSpinner() {
        val speciesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            viewModel.speciesOptions
        )
        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSpecies.adapter = speciesAdapter

        binding.spinnerSpecies.setSelection(viewModel.selectedSpeciesIndex.value ?: 0)

        binding.spinnerSpecies.setOnItemSelectedListener(
            SimpleItemSelectedListener { position ->
                viewModel.setSpecies(position)
            }
        )
    }

private fun setupGenderRadioGroup() {
    binding.radioGroupGender.setOnCheckedChangeListener { _, checkedId ->
        when (checkedId) {
            R.id.radioMale -> viewModel.setGender("Male")
            R.id.radioFemale -> viewModel.setGender("Female")
            R.id.radioOther -> viewModel.setGender("Other")
        }
    }
}

private fun setupNameInput() {
    binding.editTextName.doAfterTextChanged { editable ->
        viewModel.setName(editable.toString())
    }
}

private fun setupSeekBars() {
    // max of 100 because the assignment says the attributes can range from 0 to 100
    binding.seekStrength.max = 100
    binding.seekIntelligence.max = 100
    binding.seekDexterity.max = 100
    binding.seekWisdom.max = 100

    binding.seekStrength.setOnSeekBarChangeListener(
        AttributeSeekBarListener { newValue ->
            viewModel.updateStrength(newValue)
        }
    )

    binding.seekIntelligence.setOnSeekBarChangeListener(
        AttributeSeekBarListener { newValue ->
            viewModel.updateIntelligence(newValue)
        }
    )

    binding.seekDexterity.setOnSeekBarChangeListener(
        AttributeSeekBarListener { newValue ->
            viewModel.updateDexterity(newValue)
        }
    )

    binding.seekWisdom.setOnSeekBarChangeListener(
        AttributeSeekBarListener { newValue ->
            viewModel.updateWisdom(newValue)
        }
    )
}

private fun observeViewModel() {
    viewModel.characterName.observe(this) {
        if (binding.editTextName.text.toString() != it) {
            binding.editTextName.setText(it)
            binding.editTextName.setSelection(it.length)
        }
    }

    viewModel.selectedClassIndex.observe(this) { index ->
        if (binding.spinnerClass.selectedItemPosition != index) {
            binding.spinnerClass.setSelection(index)
        }
    }

    viewModel.selectedSpeciesIndex.observe(this) { index ->
        if (binding.spinnerSpecies.selectedItemPosition != index) {
            binding.spinnerSpecies.setSelection(index)
        }
    }

    viewModel.gender.observe(this) { selectedGender ->
        when (selectedGender) {
            "Male" -> binding.radioMale.isChecked = true
            "Female" -> binding.radioFemale.isChecked = true
            "Other" -> binding.radioOther.isChecked = true
        }
    }

    viewModel.strength.observe(this) {
        binding.seekStrength.progress = it
        binding.textStrengthValue.text = it.toString()
    }

    viewModel.intelligence.observe(this) {
        binding.seekIntelligence.progress = it
        binding.textIntelligenceValue.text = it.toString()
    }

    viewModel.dexterity.observe(this) {
        binding.seekDexterity.progress = it
        binding.textDexterityValue.text = it.toString()
    }

    viewModel.wisdom.observe(this) {
        binding.seekWisdom.progress = it
        binding.textWisdomValue.text = it.toString()
    }

    viewModel.pointsSpent.observe(this) {
        binding.textPointsSpent.text = getString(R.string.points_spent_format, it)
    }

    viewModel.pointsRemaining.observe(this) {
        binding.textPointsRemaining.text = getString(R.string.points_remaining_format, it)
    }

    viewModel.portraitResId.observe(this) {
        binding.imagePortrait.setImageResource(it)
    }
}
}
