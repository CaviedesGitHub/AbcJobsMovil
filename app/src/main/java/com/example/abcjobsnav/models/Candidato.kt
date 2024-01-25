package com.example.abcjobsnav.models

data class Candidato (
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val documento: String,
    val fecha_nac: String,
    val email: String,
    val phone: String,
    val ciudad: String,
    val direccion: String,
    val imagen: String,
    val id_usuario: Int
)