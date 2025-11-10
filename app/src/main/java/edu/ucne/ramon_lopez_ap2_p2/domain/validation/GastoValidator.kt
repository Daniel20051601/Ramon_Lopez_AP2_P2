package edu.ucne.ramon_lopez_ap2_p2.domain.validation

import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class GastoValidator @Inject constructor(){
    fun validateFecha(fecha: String): ValidationResult {
        if (fecha.isBlank()) {
            return ValidationResult(isValid = false, error = "Debe seleccionar una fecha.")
        }
        try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(fecha)
        } catch (e: Exception) {
            return ValidationResult(isValid = false, error = "Formato de fecha inválido.")
        }
        return ValidationResult(isValid = true)
    }

    fun validateSuplidor(suplidor: String): ValidationResult {
        if (suplidor.isBlank()) {
            return ValidationResult(isValid = false, error = "Debe agregar el nombre del suplidor.")
        }
        if (suplidor.length < 3) {
            return ValidationResult(isValid = false, error = "El nombre del suplidor es demasiado corto.")
        }
        if (suplidor.length > 100) {
            return ValidationResult(isValid = false, error = "El nombre del suplidor no puede contener más de 100 caracteres.")
        }
        return ValidationResult(isValid = true)
    }

    fun validateNcf(ncf: String): ValidationResult {
        if (ncf.isBlank()) {
            return ValidationResult(isValid = false, error = "Debe agregar el NCF.")
        }
        if (!ncf.matches(Regex("^[BbEe]\\d{11}$"))) {
            return ValidationResult(isValid = false, error = "El formato del NCF es inválido (debe comenzar con B o E seguido de 11 dígitos).")
        }
        return ValidationResult(isValid = true)
    }

    fun validateItbis(itbis: Double): ValidationResult {
        if (itbis < 0) {
            return ValidationResult(isValid = false, error = "El ITBIS no puede ser negativo.")
        }
        if (itbis > 999999.99) {
            return ValidationResult(isValid = false, error = "El ITBIS no puede ser mayor a 999,999.99.")
        }
        return ValidationResult(isValid = true)
    }

    fun validateMonto(monto: Double): ValidationResult {
        if (monto <= 0) {
            return ValidationResult(isValid = false, error = "El monto debe ser mayor a 0.")
        }
        if (monto > 9999999.99) {
            return ValidationResult(isValid = false, error = "El monto no puede ser mayor a 9,999,999.99.")
        }
        return ValidationResult(isValid = true)
    }
}