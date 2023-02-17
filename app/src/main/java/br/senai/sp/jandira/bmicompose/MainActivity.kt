package br.senai.sp.jandira.bmicompose

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.bmicompose.ui.theme.BMIComposeTheme
import br.senai.sp.jandira.bmicompose.utils.bmiCalculate
import br.senai.sp.jandira.bmicompose.utils.getColorScore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMIComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BMICaculator()
                }
            }
        }
    }
}

@Composable
fun BMICaculator() {
    var stateWeight = rememberSaveable() {
        mutableStateOf("")
    }
    var stateHeight by rememberSaveable() {
        mutableStateOf("")
    }
    var expandState by remember {
        mutableStateOf(false)
    }
    var scoreBmiState by remember {
        mutableStateOf(0.0)
    }
    var isWeightError by remember {
        mutableStateOf(false)
    }
    var isHeightError by remember {
        mutableStateOf(false)
    }

    //Objeto que controla a requisicao de  foco (RequestFocus)
    val weightFocusRequester = FocusRequester()

    //CONTENT
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {
        //HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bmi),
                contentDescription = "Logo do App",
                modifier = Modifier
                    .size(128.dp)
            )
            Text(
                text = stringResource(id = R.string.app_title),
                color = MaterialTheme.colors.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight(600),
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
        }
        //FORM
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 32.dp
                ),
            horizontalAlignment = Alignment.Start

        ) {
            Text(
                text = stringResource(id = R.string.weight),
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colors.primaryVariant
            )
            OutlinedTextField(
                value = stateWeight.value,
                onValueChange = {
                    var lastChar =
                        if (it.length == 0) {
                            isWeightError = true
                            it
                        }
                        else {
                            it.get(it.length - 1)
                            isWeightError = false
                        }
                    var newValue = if (lastChar == ',') it.dropLast(1) else it
                    stateWeight.value = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(weightFocusRequester),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.MonitorWeight, contentDescription = "weight")
                },
                trailingIcon = {
                    if (isWeightError) Icon(imageVector = Icons.Default.Warning, contentDescription = "fail")
                },
                isError = isWeightError,
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            if (isWeightError){
                Text(
                    text = stringResource(id = R.string.weight_required),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Red,
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.height),
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = stateHeight,
                onValueChange = {
                    var lastChar =
                        if (it.length == 0) {
                            isHeightError = true
                            it
                        }
                        else {
                            it.get(it.length - 1)
                            isHeightError = false
                        }
                    var newValue = if (lastChar == '.' || lastChar == ',') it.dropLast(1) else it
                    stateHeight = newValue
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Height, contentDescription = "height")
                },
                trailingIcon = {
                   if (isHeightError) Icon(imageVector = Icons.Filled.Info, contentDescription = "fail")
                },
                isError = isHeightError,
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            if (isHeightError) {
                Text(
                    text = stringResource(id = R.string.height_required),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Red,
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                onClick = {
                    isWeightError = stateWeight.value.length == 0
                    isHeightError = stateHeight.length == 0
                    if (isWeightError == false && isHeightError == false)
                        scoreBmiState = bmiCalculate(stateWeight.value.toDouble(), stateHeight.toDouble())
                    expandState = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(Color(34, 140, 34))
            ) {
                Text(
                    text = stringResource(id = R.string.calculate),
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

//      Outra forma de utilização do input
//            OutlinedTextField(
//                value = "",
//                onValueChange = { },
//                modifier = Modifier.fillMaxWidth(),
//                label = {
//                    Text(text = "Digite algo")
//                }
//            )
        }
        //FOOTER
        AnimatedVisibility(
            visible = expandState,
            enter = slideIn(tween(durationMillis = 500)) {
                IntOffset(it.width, 500)
            },
            exit = slideOut(tween(durationMillis = 500)) {
                IntOffset(it.width, 1000)
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                backgroundColor = getColorScore(scoreBmiState)
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.title_score),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = String.format("%.2f", scoreBmiState),
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Congratulations! Your weight is ideal!",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(400),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                expandState = false
                                stateWeight.value = ""
                                stateHeight = ""
                                weightFocusRequester.requestFocus()
                            },
                            colors = ButtonDefaults.buttonColors(Color(137, 119, 248))
                        ) {
                            Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "refresh")
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = stringResource(id = R.string.reset))
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(Color(137, 119, 248))
                        ) {
                            Icon(imageVector = Icons.Rounded.Share, contentDescription = "share")
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = stringResource(id = R.string.share))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BMIPreview() {
    BMICaculator()
}


