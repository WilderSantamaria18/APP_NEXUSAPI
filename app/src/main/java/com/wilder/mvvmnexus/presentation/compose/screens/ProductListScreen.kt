package com.wilder.mvvmnexus.presentation.compose.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.scale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import com.wilder.mvvmnexus.presentation.viewmodel.MainViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.ThemeViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.AuthViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.CartViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.FavoritosViewModel
import com.wilder.mvvmnexus.presentation.compose.components.ProductItem
import com.wilder.mvvmnexus.presentation.compose.components.AppDrawer
import com.wilder.mvvmnexus.domain.model.EstadoAuth
import com.wilder.mvvmnexus.domain.model.Usuario
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: MainViewModel,
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel,
    favoritosViewModel: com.wilder.mvvmnexus.presentation.viewmodel.FavoritosViewModel,
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
    
    val authState by authViewModel.estadoAuth.collectAsState(initial = null)
    val usuario = (authState as? EstadoAuth.Autenticado)?.usuario

    val cantidadProductos by cartViewModel.cantidadProductos.collectAsState()
    val favoritos by favoritosViewModel.favoritos.collectAsState()
    
    var animateCart by remember { mutableStateOf(false) }
    var previousCount by remember { mutableStateOf(0) }

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var sortOption by remember { mutableStateOf("Default") } 

    val filteredProducts = remember(productos, favoritos, selectedCategory, sortOption) {
        var list = if (selectedCategory != null) {
            productos.filter { it.categoria == selectedCategory }
        } else {
            productos
        }

        when (sortOption) {
            "PriceAsc" -> list.sortedBy { it.precio }
            "PriceDesc" -> list.sortedByDescending { it.precio }
            "Favorites" -> {
                val favoriteIds = favoritos.map { it.productoId }.toSet()
                list.sortedByDescending { it.id in favoriteIds }
            }
            else -> list
        }
    }

    val categories = remember(productos) {
        productos.map { it.categoria }.distinct()
    }
    
    LaunchedEffect(cantidadProductos) {
        if (cantidadProductos > previousCount && previousCount > 0) {
            animateCart = true
        }
        previousCount = cantidadProductos
    }
    
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
                        Column {
                            androidx.compose.foundation.lazy.LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                item {
                                    FilterChip(
                                        selected = sortOption == "Default",
                                        onClick = { sortOption = "Default" },
                                        label = { Text("Todo") }
                                    )
                                }
                                item {
                                    FilterChip(
                                        selected = sortOption == "Favorites",
                                        onClick = { sortOption = "Favorites" },
                                        label = { Text("Favoritos") }
                                    )
                                }
                                item {
                                    FilterChip(
                                        selected = sortOption == "PriceDesc",
                                        onClick = { sortOption = "PriceDesc" },
                                        label = { Text("Precio Max") }
                                    )
                                }
                                item {
                                    FilterChip(
                                        selected = sortOption == "PriceAsc",
                                        onClick = { sortOption = "PriceAsc" },
                                        label = { Text("Precio Min") }
                                    )
                                }
                                items(categories) { category ->
                                    FilterChip(
                                        selected = selectedCategory == category,
                                        onClick = { 
                                            selectedCategory = if (selectedCategory == category) null else category 
                                        },
                                        label = { Text(category) }
                                    )
                                }
                            }

                            LazyColumn(
                                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(filteredProducts) { producto ->
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
}
