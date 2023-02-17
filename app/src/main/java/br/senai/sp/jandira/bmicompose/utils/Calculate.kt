package br.senai.sp.jandira.bmicompose.utils

import kotlin.math.pow

fun bmiCalculate(weight: Double, height: Double) = weight / (height / 100).pow(2)