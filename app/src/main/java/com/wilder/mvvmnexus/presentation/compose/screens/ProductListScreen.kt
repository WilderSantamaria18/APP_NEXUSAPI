package com.wilder.mvvmnexus.presentation.compose.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wilder.mvvmnexus.presentation.viewmodel.MainViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.ThemeViewModel
import com.wilder.mvvmnexus.presentation.compose.components.ProductItem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.scale
import com.wilder.mvvmnexus.domain.model.EstadoAuth
import com.wilder.mvvmnexus.domain.model.Usuario
import com.wilder.mvvmnexus.presentation.compose.components.AppDrawer
import com.wilder.mvvmnexus.presentation.viewmodel.AuthViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: MainViewModel,
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel,
    themeViewModel: ThemeViewModel,
    onProductClick: (Int) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val productos by viewModel.productos.observeAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val error by viewModel.error.observeAsState(initial = null)
    
    // Estado del usuario para el Drawer
    val authState by authViewModel.estadoAuth.collectAsState(initial = null)
    val usuario = (authState as? EstadoAuth.Autenticado)?.usuario

    // Cantidad de productos en el carrito
    val cantidadProductos by cartViewModel.cantidadProductos.collectAsState()
    
    // Estado de animación del carrito
    var animateCart by remember { mutableStateOf(false) }
    var previousCount by remember { mutableStateOf(0) }
    
    // Detectar cuando se agrega un producto al carrito
    LaunchedEffect(cantidadProductos) {
        if (cantidadProductos > previousCount && previousCount > 0) {
            animateCart = true
        }
        previousCount = cantidadProductos
    }
    
    // Animación de escala con rebote
    val scale by animateFloatAsState(
        targetValue = if (animateCart) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = {
            animateCart = false
        },
        label = "cartBadgeScale"
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                usuario = usuario,
                onNavigateTo = { ruta ->
                    if (ruta == "perfil") {
                        onNavigateToProfile()
                    } else if (ruta == "favoritos") {
                        onNavigateToFavorites()
                    } else if (ruta == "carrito") {
                        onNavigateToCart()
                    } else if (ruta == "configuracion") {
                        onNavigateToSettings()
                    }
                },
                onLogout = {
                    authViewModel.cerrarSesion()
                    onLogout()
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Nexus Store", style = MaterialTheme.typography.titleLarge) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToCart) {
                            BadgedBox(
                                badge = {
                                    if (cantidadProductos > 0) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.error,
                                            contentColor = MaterialTheme.colorScheme.onError,
                                            modifier = Modifier.scale(scale)
                                        ) {
                                            Text(
                                                text = if (cantidadProductos > 99) "99+" else cantidadProductos.toString(),
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    error != null -> {
                        Text(
                            text = error ?: "Error desconocido",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(productos) { producto ->
                                ProductItem(
                                    producto = producto,
                                    onClick = { onProductClick(producto.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
