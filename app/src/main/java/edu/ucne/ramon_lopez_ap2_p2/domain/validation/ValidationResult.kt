package edu.ucne.ramon_lopez_ap2_p2.domain.validation

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)
