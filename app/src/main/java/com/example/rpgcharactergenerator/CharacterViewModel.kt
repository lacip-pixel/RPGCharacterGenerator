package com.example.rpgcharactergenerator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CharacterViewModel : ViewModel() {

    // spinner options from the project sheet
    val classOptions = listOf(
        "Select a Class",
        "Barbarian",
        "Cleric",
        "Rogue",
        "Ranger",
        "Wizard"
    )

val speciesOptions = listOf(
    "Human",
    "Dwarf",
    "High Elf",
    "Orc",
    "Goblin"
)

private val _characterName = MutableLiveData("")
val characterName: LiveData<String> = _characterName

private val _selectedClassIndex = MutableLiveData(0)
val selectedClassIndex: LiveData<Int> = _selectedClassIndex

private val _selectedSpeciesIndex = MutableLiveData(0)
val selectedSpeciesIndex: LiveData<Int> = _selectedSpeciesIndex

private val _gender = MutableLiveData("Male")
val gender: LiveData<String> = _gender

// assignment says default is 25 each for 100 total
private val _strength = MutableLiveData(25)
val strength: LiveData<Int> = _strength

private val _intelligence = MutableLiveData(25)
val intelligence: LiveData<Int> = _intelligence

private val _dexterity = MutableLiveData(25)
val dexterity: LiveData<Int> = _dexterity

private val _wisdom = MutableLiveData(25)
val wisdom: LiveData<Int> = _wisdom

private val _pointsSpent = MutableLiveData(100)
val pointsSpent: LiveData<Int> = _pointsSpent

private val _pointsRemaining = MutableLiveData(0)
val pointsRemaining: LiveData<Int> = _pointsRemaining

private val _portraitResId = MutableLiveData(R.drawable.ic_default_portrait)
val portraitResId: LiveData<Int> = _portraitResId

fun setName(name: String) {
    _characterName.value = name
}

fun setCharacterClass(index: Int) {
    _selectedClassIndex.value = index
    updatePortrait(index)
}

fun setSpecies(index: Int) {
    _selectedSpeciesIndex.value = index
}

fun setGender(selectedGender: String) {
    _gender.value = selectedGender
    updatePortrait(selectedClassIndex.value ?: 0)
}

fun updateStrength(newValue: Int) {
    updateAttribute(_strength, newValue)
}

fun updateIntelligence(newValue: Int) {
    updateAttribute(_intelligence, newValue)
}

fun updateDexterity(newValue: Int) {
    updateAttribute(_dexterity, newValue)
}

fun updateWisdom(newValue: Int) {
    updateAttribute(_wisdom, newValue)
}

private fun updateAttribute(attribute: MutableLiveData<Int>, requestedValue: Int) {
    val oldValue = attribute.value ?: 0
    val currentTotal = calculateTotal()
    val difference = requestedValue - oldValue

    // if user is trying to increase and there are not enough points left,
    // cap the value instead of letting it go over 100 total
    if (difference > 0 && difference > (_pointsRemaining.value ?: 0)) {
        val allowedIncrease = _pointsRemaining.value ?: 0
        attribute.value = oldValue + allowedIncrease
    } else {
        attribute.value = requestedValue.coerceIn(0, 100)
    }

    recalculatePoints()
}

private fun calculateTotal(): Int {
    return (_strength.value ?: 0) +
            (_intelligence.value ?: 0) +
            (_dexterity.value ?: 0) +
            (_wisdom.value ?: 0)
}

private fun recalculatePoints() {
    val total = calculateTotal()
    _pointsSpent.value = total
    _pointsRemaining.value = 100 - total
}

private fun updatePortrait(classIndex: Int) {
    _portraitResId.value = when (classIndex) {
        1 -> if (_gender.value == "Female") R.drawable.girl_barbarian else if (_gender.value == "Male") R.drawable.barbarian_portrait else R.drawable.other_barbarian
        2 -> if (_gender.value == "Female") R.drawable.cleric_portrait else if (_gender.value == "Male") R.drawable.male_cleric else R.drawable.other_cleric
        3 -> if (_gender.value == "Female") R.drawable.female_rouge else if (_gender.value == "Male") R.drawable.rogue_portrait else R.drawable.other_rouge
        4 -> if (_gender.value == "Female") R.drawable.female_ranger else if (_gender.value == "Male") R.drawable.ranger_portrait else R.drawable.other_ranger
        5 -> if (_gender.value == "Female") R.drawable.female_wizard else if (_gender.value == "Male") R.drawable.wizard_portrait else R.drawable.other_wizard
        else -> R.drawable.ic_default_portrait
    }
}
}