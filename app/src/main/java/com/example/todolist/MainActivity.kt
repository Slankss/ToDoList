package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.ui.theme.*

private lateinit var viewModel : JobViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = JobViewModel(this)

        setContent {
            ToDoListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ToDoList()
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ToDoList() {

    val jobList = viewModel.jobList

    ToDoListTheme {
        Column {

            TopCard() {
                viewModel.addJob(it)
            }
            Spacer(modifier = Modifier.height(15.dp))
            BottomCard(jobList)
        }
    }
}

@Composable
fun TopCard(function : (String) -> Unit){

    var job = remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth(),
            elevation = 8.dp,
            shape = RoundedCornerShape(
                topEnd = 0.dp,
                topStart = 0.dp,
                bottomStart = 30.dp,
                bottomEnd = 30.dp
                )
        ) {
        Box(modifier = Modifier .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Top_Card_Start_Color,
                    Top_Card_End_Color
                )
                )
            )
        )
        {
            Column(modifier = Modifier.padding(top = 20.dp,bottom = 15.dp,start = 15.dp,end = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Yapılacak İş",color = Color.White, fontSize = 20.sp)

                Spacer(modifier = Modifier.height(15.dp))

                TextField(value = job.value, onValueChange = {
                    job.value = it
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        cursorColor = Top_Card_Start_Color,
                        //disabledLabelColor = lightBlue,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )

                ///////////////////////////////////////

                Spacer(modifier = Modifier.height(15.dp))

                Button(modifier = Modifier,
                    colors = ButtonDefaults.buttonColors(Button_Color),
                    onClick = {
                        function(job.value)
                        job.value = ""
                    } ){
                    Text(text = "EKLE",color = Color.White, fontSize = 20.sp)
                }

            }
        }


    }

}


@Composable
fun BottomCard(jobList: SnapshotStateList<Job>){

    Card(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(15.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(30.dp
        )
    ){
        Box(modifier = Modifier
            .background(
             brush = Brush.verticalGradient(
                 colors = listOf(
                     Top_Card_End_Color,
                     Top_Card_Start_Color

                 )
             )
            )
        )
        {

            LazyColumn(modifier = Modifier
                .align(Alignment.TopStart)
                .padding(25.dp)

            )
            {
                items(items = jobList,itemContent = {Item(job = it)})

            }
            IconButton(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
                onClick = {
                    viewModel.clearJob()
                }) {
                Icon(painter = painterResource(id = R.drawable.clear_svgrepo_com),
                    contentDescription = "Temizle",
                    tint = Color.White
                    )
            }
        }
    }

}

@Composable
fun Item(job : Job){

    var is_done by remember { mutableStateOf(false)}
    is_done = when(job.job_is_done){
        1 -> true
        else -> {false}
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {},
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
        ){
        Text(text = "${job.job_order})", color = Color.White)
        val special_text_decoration = when(is_done){
            true -> TextDecoration.LineThrough
            false -> TextDecoration.None
        }

        Text(text = job.job,color = Color.White,
            modifier = Modifier.padding(start = 10.dp).weight(2f),
            textDecoration = special_text_decoration
        )

        Checkbox(modifier = Modifier.scale(1.25f),
            checked = is_done , onCheckedChange = {
            is_done = it
            when(it){
                true -> viewModel.changeIsDone(job.job_order,1)
                false -> viewModel.changeIsDone(job.job_order,0)
            }
        })

    }

    Spacer(modifier = Modifier.height(10.dp))
}





