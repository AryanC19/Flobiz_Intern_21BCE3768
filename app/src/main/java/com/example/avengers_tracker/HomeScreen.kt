package com.example.avengers_tracker

import android.text.Layout
import android.view.Surface
import android.widget.GridLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.avengers_tracker.data.model.ExpenseEntity
import com.example.avengers_tracker.ui.theme.Zinc
import com.example.avengers_tracker.viewmodel.HomeViewModel
import com.example.avengers_tracker.viewmodel.HomeViewModelFactory
import com.example.avengers_tracker.widgets.ExpenseTextView

@Composable
fun HomeScreen() {

    val viewModel: HomeViewModel =
        HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    }) {
                Column {
                    ExpenseTextView(
                        text = "Avengers Tracker",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    ExpenseTextView(
                        text = "Welcome to the Avengers Tracker",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                }

                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            //to store the chanign fields
            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val expense = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)

            CardItem(
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                balance = balance, income = income, expense = expense
            )
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(list) {
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
                list = state.value, viewModel = viewModel
            )

        }

    }
}

@Composable
fun CardItem(
    modifier: Modifier,
    balance: String,
    income: String,
    expense: String,

    ) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                ExpenseTextView(
                    text = "Total Balance",
                    fontSize = 20.sp,
                    //  style = Typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(8.dp))
                ExpenseTextView(
                    text = balance,
                    fontSize = 20.sp,
                    //   style = Typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),

            ) {

            CardRowItem(
                modifier = Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                image = R.drawable.ic_income

            )

            Spacer(modifier = Modifier.size(8.dp))

            CardRowItem(
                modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = expense,
                image = R.drawable.ic_expense
            )

        }

    }
}

@Composable
fun TransactionList(
    modifier: Modifier = Modifier,
    list: List<ExpenseEntity>,

    viewModel: HomeViewModel
) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            Box(modifier = modifier.fillMaxWidth()) {
                ExpenseTextView(
                    text = "Recent Transactions",
                    fontSize = 20.sp,
                )
                ExpenseTextView(
                    text = "See All",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }

        items(list) { item ->
            TransactionItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = viewModel.getItemIcon(item),
                date = item.date.toString(), // Fixed `item.data` to `item.date`
                color = if (item.type == "Income") Color.Green else Color.Red
            )
        }
    }
}


@Composable
fun TransactionItem(
    title: String,
    amount: String,
    icon: Int,
    date: String,
    color: Color,

    ) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(51.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                ExpenseTextView(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.size(6.dp))
                ExpenseTextView(text = date, fontSize = 13.sp, color = Color.Black)
            }
        }
        ExpenseTextView(
            text = amount,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, image: Int) {
    Column(modifier = modifier) {
        Row {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            ExpenseTextView(text = title, fontSize = 16.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseTextView(
            text = amount,
            fontSize = 20.sp,
            color = Color.White,

            )
    }
}

@Composable
@Preview
fun PreviewHomeScreen() {
    HomeScreen()
}