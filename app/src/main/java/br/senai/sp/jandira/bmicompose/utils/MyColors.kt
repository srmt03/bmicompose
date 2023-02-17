package br.senai.sp.jandira.bmicompose.utils

import androidx.compose.ui.graphics.Color

fun getColorScore (bmi: Double): Color {
    return if (bmi <= 18.5)
        Color(170, 27, 27)
    else if (bmi > 18.5 && bmi < 25.0)
        Color(40, 131, 23)
    else if (bmi >= 25.0 && bmi < 30.0)
        Color(216, 114, 35)
    else
        Color(150, 23, 23)
}