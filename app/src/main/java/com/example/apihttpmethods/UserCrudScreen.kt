package com.example.apihttpmethods

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCrudScreen(viewModel: UserViewModel = viewModel()) {
    val usersState by viewModel.usersState.collectAsState()
    val actionState by viewModel.actionState.collectAsState()

    var nameInput by remember { mutableStateOf("morpheus") }
    var jobInput by remember { mutableStateOf("leader") }
    var selectedUserId by remember { mutableIntStateOf(2) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("HTTP CRUD Tester (ReqRes API)") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- Input Fields ---
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = jobInput,
                onValueChange = { jobInput = it },
                label = { Text("Job Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Target User ID for PUT / PATCH / DELETE: $selectedUserId",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            // --- HTTP Action Buttons ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.createUser(nameInput, jobInput) },
                    modifier = Modifier.weight(1f)
                ) { Text("POST") }

                Button(
                    onClick = { viewModel.updateUser(selectedUserId, nameInput, jobInput) },
                    modifier = Modifier.weight(1f)
                ) { Text("PUT") }

                Button(
                    onClick = { viewModel.patchUser(selectedUserId, jobInput) },
                    modifier = Modifier.weight(1f)
                ) { Text("PATCH") }

                Button(
                    onClick = { viewModel.deleteUser(selectedUserId) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) { Text("DEL") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Action Response Console ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Response Log:", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    when (val state = actionState) {
                        is Resource.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        is Resource.Success -> Text(state.data, color = MaterialTheme.colorScheme.primary)
                        is Resource.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
                        null -> Text("Tap any HTTP action button above to test execution.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            // --- GET Result List ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Remote Users (GET)", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { viewModel.fetchUsers() }) {
                    Text("🔄")
                }
            }

            when (val state = usersState) {
                is Resource.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
                is Resource.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.data) { user ->
                            val isSelected = selectedUserId == user.id
                            OutlinedCard(
                                onClick = {
                                    selectedUserId = user.id
                                    nameInput = "${user.firstName} ${user.lastName}"
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = if (isSelected) {
                                    CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                                } else {
                                    CardDefaults.outlinedCardColors()
                                }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("#${user.id} - ${user.firstName} ${user.lastName}", style = MaterialTheme.typography.titleSmall)
                                        Text(user.email, style = MaterialTheme.typography.bodySmall)
                                    }
                                    if (isSelected) {
                                        Text("SELECTED", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}