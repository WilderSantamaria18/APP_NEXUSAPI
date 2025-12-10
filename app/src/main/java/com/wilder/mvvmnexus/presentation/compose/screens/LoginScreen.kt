package com.wilder.mvvmnexus.presentation.compose.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.wilder.mvvmnexus.R
import com.wilder.mvvmnexus.domain.model.EstadoAuth
import com.wilder.mvvmnexus.presentation.compose.components.NexusButton
import com.wilder.mvvmnexus.presentation.compose.components.NexusTextField
import com.wilder.mvvmnexus.presentation.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    val context = LocalContext.current
    val authState: EstadoAuth? by viewModel.estadoAuth.collectAsState(initial = null)
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }

    // Efecto para manejar el estado de autenticación
    LaunchedEffect(authState) {
        when (val state = authState) {
            is EstadoAuth.Autenticado -> {
                isLoading = false
                onLoginSuccess()
            }
            is EstadoAuth.Error -> {
                isLoading = false
                Toast.makeText(context, state.mensaje, Toast.LENGTH_LONG).show()
            }
            is EstadoAuth.Cargando -> {
                isLoading = true
            }
            else -> { isLoading = false }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo o Título Minimalista
        Text(
            text = "NexusApi",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Bienvenido de nuevo",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
        )

        // Formulario
        NexusTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo electrónico",
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        NexusTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    androidx.compose.material.icons.Icons.Filled.Visibility
                else
                    androidx.compose.material.icons.Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Olvidé mi contraseña
        Text(
            text = "¿Olvidaste tu contraseña?",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { 
                    resetEmail = email 
                    showResetDialog = true 
                }
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de Login
        NexusButton(
            text = "Iniciar Sesión",
            onClick = { viewModel.iniciarSesionConEmail(email, password) },
            isLoading = isLoading,
            enabled = email.isNotEmpty() && password.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Separador
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
            Text(
                text = "O continúa con",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Google (Estilo Outline para contraste)
        OutlinedButton(
            onClick = onGoogleSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google_logo), // Asegúrate de tener este recurso o usa un icono vectorial
                contentDescription = "Google",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Google",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Pestaña de Registro
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿No tienes cuenta? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Regístrate",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onNavigateToRegister)
            )
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Restablecer Contraseña") },
            text = {
                Column {
                    Text("Ingresa tu correo electrónico para recibir un enlace de restablecimiento.")
                    Spacer(modifier = Modifier.height(16.dp))
                    NexusTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it },
                        label = "Correo electrónico"
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.restablecerPassword(resetEmail)
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
