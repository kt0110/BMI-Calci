package com.aa.android.books

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aa.android.books.model.Doc
import com.aa.android.books.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookSearchScreen()
        }
    }

    @Composable
    fun BookSearchScreen(viewModel: MainViewModel = hiltViewModel()) {
        val uiState by viewModel.uiState.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            var searchTerm by remember { mutableStateOf("") }

            TextField(
                value = searchTerm,
                onValueChange = {
                    searchTerm = it
                    if (it.length > 3) viewModel.searchBooks(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter Search Term") },
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                is MainViewModel.UiState.Loading -> CircularProgressIndicator()
                is MainViewModel.UiState.Success -> BookList(books = (uiState as MainViewModel.UiState.Success).books)
                is MainViewModel.UiState.Error -> Text(uiState as MainViewModel.UiState.Error)
                MainViewModel.UiState.Idle -> {}
            }
        }
    }
}

@Composable
fun BookList(books: List<Doc>) {
    LazyColumn {
        items(books) { book ->
            BookItem(book)
            Divider()
        }
    }
}

@Composable
fun BookItem(book: Doc) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(book.title, style = MaterialTheme.typography.h6)
        Text(book.anuthorName?.joinToString() ?: "No Author")
        Text(book.firstPublishYear?.toString() ?: "No Year")
    }
}
