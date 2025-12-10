package com.wilder.mvvmnexus.presentation.compose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wilder.mvvmnexus.domain.model.EstadoAuth
import com.wilder.mvvmnexus.presentation.viewmodel.AuthViewModel
import com.wilder.mvvmnexus.presentation.compose.components.NexusButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onBackClick: () -> Unit
) {
    val authState by viewModel.estadoAuth.collectAsState(initial = null)
    val usuario = (authState as? EstadoAuth.Autenticado)?.usuario
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                
                // Avatar
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (usuario?.fotoUrl.isNullOrEmpty()) {
                        Text(
                            text = usuario?.nombreCompleto?.firstOrNull()?.uppercase()?.toString() ?: "U",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    } else {
                        AsyncImage(
                            model = usuario?.fotoUrl,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Información
                ProfileItem(
                    icon = Icons.Default.Person,
                    label = "Nombre Completo",
                    value = usuario?.nombreCompleto ?: "No disponible"
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                ProfileItem(
                    icon = Icons.Default.Email,
                    label = "Correo Electrónico",
                    value = usuario?.email ?: "No disponible"
                )

                if (usuario?.proveedor == "firebase") {
                    Spacer(modifier = Modifier.height(24.dp))
                    NexusButton(
                        text = "Restablecer Contraseña",
                        onClick = { showResetDialog = true },
                        isLoading = false
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Información guardada localmente",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Restablecer Contraseña") },
            text = { Text("¿Deseas enviar un correo para restablecer tu contraseña a ${usuario?.email}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        usuario?.email?.let { viewModel.restablecerPassword(it) }
                        showResetDialog = false
                    }
                ) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), MaterialTheme.shapes.medium)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
