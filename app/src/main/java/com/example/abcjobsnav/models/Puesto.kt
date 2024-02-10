package com.example.abcjobsnav.models

data class Puesto (
    val Num: Int,
    val id: Int,
    val nom_proyecto: String,
    val nom_perfil: String,
    val id_perfil: Int,
    val id_cand: Int,
    val candidato: String,
    val fecha_inicio: String,
    val fecha_asig: String,
    val imagen: String
)

