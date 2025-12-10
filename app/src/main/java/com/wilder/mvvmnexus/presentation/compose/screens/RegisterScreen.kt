package com.wilder.mvvmnexus.presentation.compose.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wilder.mvvmnexus.R
import com.wilder.mvvmnexus.domain.model.EstadoAuth
import com.wilder.mvvmnexus.presentation.compose.components.NexusButton
import com.wilder.mvvmnexus.presentation.compose.components.NexusTextField
import com.wilder.mvvmnexus.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    val context = LocalContext.current
    val authState: EstadoAuth? by viewModel.estadoAuth.collectAsState(initial = null)
    
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        when (val state = authState) {
            is EstadoAuth.Autenticado -> {
                isLoading = false
                onRegisterSuccess()
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Text(
                text = "Únete a Nexus hoy mismo",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 8.dp, bottom = 32.dp)
            )

            // Formulario
            NexusTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nombre completo",
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

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
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            NexusTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirmar contraseña",
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = confirmPassword.isNotEmpty() && password != confirmPassword
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de Registro
            NexusButton(
                text = "Registrarse",
                onClick = { viewModel.registrarUsuario(email, password, confirmPassword) },
                isLoading = isLoading,
                enabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Separador
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                Text(
                    text = "O regístrate con",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Google
            OutlinedButton(
                onClick = onGoogleSignInClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_logo),
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
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
