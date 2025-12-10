package com.wilder.mvvmnexus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilder.mvvmnexus.domain.model.*
import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val casosUsoAuth: CasosUsoAuth,
    private val casosUsoCarrito: CasosUsoCarrito
) : ViewModel() {
    
    private val _estadoAuth = MutableStateFlow<EstadoAuth>(EstadoAuth.Cargando)
    val estadoAuth: StateFlow<EstadoAuth> = _estadoAuth.asStateFlow()
    
    private val _resultadoLogin = MutableStateFlow<ResultadoAuth<Usuario>?>(null)
    val resultadoLogin: StateFlow<ResultadoAuth<Usuario>?> = _resultadoLogin.asStateFlow()
    
    private val _resultadoRegistro = MutableStateFlow<ResultadoAuth<Usuario>?>(null)
    val resultadoRegistro: StateFlow<ResultadoAuth<Usuario>?> = _resultadoRegistro.asStateFlow()
    
    private val _resultadoGoogle = MutableStateFlow<ResultadoAuth<Usuario>?>(null)
    val resultadoGoogle: StateFlow<ResultadoAuth<Usuario>?> = _resultadoGoogle.asStateFlow()
    
    private val _mensajeInfo = MutableStateFlow<String?>(null)
    val mensajeInfo: StateFlow<String?> = _mensajeInfo.asStateFlow()
    
    init {
        observarEstadoAutenticacion()
        verificarUsuarioActual()
    }
    
    fun iniciarSesionConEmail(email: String, password: String) {
        viewModelScope.launch {
            _resultadoLogin.value = ResultadoAuth.Cargando
            
            val resultado = casosUsoAuth.iniciarSesionEmail(email, password)
            if (resultado is ResultadoAuth.Exito) {
                casosUsoAuth.guardarUsuarioLocal(resultado.data)
                sincronizarCarrito(resultado.data.uid)
            }
            _resultadoLogin.value = resultado
            
            when (resultado) {
                is ResultadoAuth.Exito -> {
                    _estadoAuth.value = EstadoAuth.Autenticado(resultado.data)
                    mostrarMensaje("¡Bienvenido ${resultado.data.obtenerNombreMostrar()}!")
                }
                is ResultadoAuth.Error -> {
                    _estadoAuth.value = EstadoAuth.Error(resultado.mensaje)
                }
                is ResultadoAuth.Cargando -> { }
            }
        }
    }
    
    fun registrarUsuario(email: String, password: String, confirmarPassword: String) {
        viewModelScope.launch {
            _resultadoRegistro.value = ResultadoAuth.Cargando
            
            val resultado = casosUsoAuth.registrarUsuario(email, password, confirmarPassword)
            if (resultado is ResultadoAuth.Exito) {
                casosUsoAuth.guardarUsuarioLocal(resultado.data)
            }
            _resultadoRegistro.value = resultado
            
            when (resultado) {
                is ResultadoAuth.Exito -> {
                    _estadoAuth.value = EstadoAuth.Autenticado(resultado.data)
                    mostrarMensaje("¡Cuenta creada exitosamente!")
                }
                is ResultadoAuth.Error -> {
                    _estadoAuth.value = EstadoAuth.Error(resultado.mensaje)
                }
                is ResultadoAuth.Cargando -> { }
            }
        }
    }
    
    fun iniciarSesionConGoogle(idToken: String) {
        viewModelScope.launch {
            _resultadoGoogle.value = ResultadoAuth.Cargando
            
            val resultado = casosUsoAuth.iniciarSesionGoogle(idToken)
            if (resultado is ResultadoAuth.Exito) {
                casosUsoAuth.guardarUsuarioLocal(resultado.data)
                sincronizarCarrito(resultado.data.uid)
            }
            _resultadoGoogle.value = resultado
            
            when (resultado) {
                is ResultadoAuth.Exito -> {
                    _estadoAuth.value = EstadoAuth.Autenticado(resultado.data)
                    mostrarMensaje("¡Bienvenido con Google ${resultado.data.obtenerNombreMostrar()}!")
                }
                is ResultadoAuth.Error -> {
                    _estadoAuth.value = EstadoAuth.Error(resultado.mensaje)
                }
                is ResultadoAuth.Cargando -> { }
            }
        }
    }
    
    fun cerrarSesion() {
        viewModelScope.launch {
            val resultado = casosUsoAuth.cerrarSesion()
            when (resultado) {
                is ResultadoAuth.Exito -> {
                    _estadoAuth.value = EstadoAuth.NoAutenticado
                    limpiarResultados()
                    mostrarMensaje("Sesión cerrada correctamente")
                }
                is ResultadoAuth.Error -> {
                    mostrarMensaje("Error al cerrar sesión: ${resultado.mensaje}")
                }
                is ResultadoAuth.Cargando -> { }
            }
        }
    }
    
    fun restablecerPassword(email: String) {
        viewModelScope.launch {
            val resultado = casosUsoAuth.restablecerPassword(email)
            when (resultado) {
                is ResultadoAuth.Exito -> {
                    mostrarMensaje("Email de restablecimiento enviado a $email")
                }
                is ResultadoAuth.Error -> {
                    mostrarMensaje("Error: ${resultado.mensaje}")
                }
                is ResultadoAuth.Cargando -> { }
            }
        }
    }
    

    
    private fun observarEstadoAutenticacion() {
        viewModelScope.launch {
            casosUsoAuth.observarEstadoAuth().collect { usuario ->
                _estadoAuth.value = if (usuario != null) {
                    EstadoAuth.Autenticado(usuario)
                } else {
                    EstadoAuth.NoAutenticado
                }
            }
        }
    }
    
    private fun verificarUsuarioActual() {
        viewModelScope.launch {
            val usuario = casosUsoAuth.obtenerUsuarioActual()
            _estadoAuth.value = if (usuario != null) {
                sincronizarCarrito(usuario.uid)
                EstadoAuth.Autenticado(usuario)
            } else {
                EstadoAuth.NoAutenticado
            }
        }
    }

    private fun sincronizarCarrito(usuarioId: String) {
        viewModelScope.launch {
            try {
                casosUsoCarrito.sincronizarCarrito(usuarioId)
            } catch (e: Exception) {
                // Silenciosamente fallar
            }
        }
    }
    
    fun limpiarResultados() {
        _resultadoLogin.value = null
        _resultadoRegistro.value = null
        _resultadoGoogle.value = null
        _mensajeInfo.value = null
    }
    
    private fun mostrarMensaje(mensaje: String) {
        _mensajeInfo.value = mensaje
    }
    
    fun limpiarMensaje() {
        _mensajeInfo.value = null
    }
}