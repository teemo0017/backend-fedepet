package com.api.crud.common;

import lombok.Getter;

@Getter
public enum ResponseCode {

    // ── Éxito ──────────────────────────────────────────────
    SUCCESS("B0000", "Operación exitosa"),

    // ── Error genérico ─────────────────────────────────────
    GENERIC_ERROR("B1000", "Error inesperado"),

    // ── Usuario ────────────────────────────────────────────
    USER_NOT_FOUND("B2000", "Usuario no encontrado"),
    INVALID_CREDENTIALS("B3000", "Credenciales incorrectas"),

    // ── Clínica ────────────────────────────────────────────
    CLINIC_NOT_FOUND("B4000", "Clínica no encontrada"),
    NO_CLINIC_IN_SESSION("B5000", "No hay clínica asociada a esta sesión"),

    // ── Doctor ─────────────────────────────────────────────
    DOCTOR_NOT_FOUND("B6000", "Doctor no encontrado"),

    // ── Mascotas ───────────────────────────────────────────
    PET_NOT_FOUND("B7000", "Mascota no encontrada"),

    // ── Citas ──────────────────────────────────────────────
    DATE_NOT_FOUND("B8000", "Cita no encontrada"),

    // ── Google Auth ────────────────────────────────────────
    GOOGLE_TOKEN_INVALID("B9000", "Token de Google inválido"),
    GOOGLE_TOKEN_EXPIRED("B10000", "Token de Google expirado"),

    // ── Imágenes ───────────────────────────────────────────
    IMAGE_UPLOAD_ERROR("B11000", "Error al subir la imagen"),

    // ── Seguridad ──────────────────────────────────────────
    UNAUTHORIZED("B12000", "No autorizado"),
    INVALID_REQUEST("B13000", "Solicitud inválida");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
