package com.example.abcjobsnav.models

data class Evaluacion (
    val id: Int,
    val id_cand: Int,
    val candidato: String,
    val idPerfilProy: Int,
    val anno: Int,
    val strmes: String,
    val mes: Int,
    val calificacion: String,
    val valoracion: Int,
    val nota: String
)
