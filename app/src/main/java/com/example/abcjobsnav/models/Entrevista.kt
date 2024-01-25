package com.example.abcjobsnav.models

data class Entrevista (
    val id: Int,
    val id_cand: Int,
    val id_perfil: Int,
    val idPerfilProy: Int,
    val candidato: String,
    val nom_empresa: String,
    val nom_proyecto: String,
    val nom_perfil: String,
    val contacto: String,
    val Num: Int,
    val cuando: String,
    val calificacion: String,
    val anotaciones: String,
    val valoracion: Int
)
