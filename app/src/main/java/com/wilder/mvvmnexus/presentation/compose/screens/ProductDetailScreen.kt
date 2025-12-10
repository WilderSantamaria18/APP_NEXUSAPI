package com.wilder.mvvmnexus.presentation.compose.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.presentation.viewmodel.DetalleProductoViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.FavoritosViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: DetalleProductoViewModel,
    favoritosViewModel: FavoritosViewModel,
    cartViewModel: com.wilder.mvvmnexus.presentation.viewmodel.CartViewModel,
    productoId: Int,
    onBackClick: () -> Unit,
    onNavigateToCart: () -> Unit,
    onAddToCart: (Producto) -> Unit
) {
    val producto by viewModel.producto.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val error by viewModel.error.observeAsState(initial = null)
    val mensajeCarrito by viewModel.mensajeCarrito.observeAsState()
    
    val esFavorito by favoritosViewModel.esFavorito(productoId).collectAsState()
    
    // Cantidad de productos en el carrito
    val cantidadProductos by cartViewModel.cantidadProductos.collectAsState()
    
    // Estado de animación del carrito (Badge)
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

    LaunchedEffect(productoId) {
        viewModel.cargarProducto(productoId)
    }
    
    LaunchedEffect(mensajeCarrito) {
        mensajeCarrito?.let {
            viewModel.limpiarMensajeCarrito()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    producto?.let { prod ->
                        IconButton(
                            onClick = { favoritosViewModel.toggleFavorito(prod) },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                            )
                        ) {
                            Icon(
                                imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                                tint = if (esFavorito) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        IconButton(
                            onClick = onNavigateToCart,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                            )
                        ) {
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
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            if (producto != null) {
                BottomBarActions(
                    price = producto!!.precio,
                    onAddToCart = { onAddToCart(producto!!) }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (error != null) {
                Text(
                    text = error ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                producto?.let { prod ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .background(Color.White)
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = prod.imagen,
                                contentDescription = prod.titulo,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = prod.categoria.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${prod.puntuacion} (${prod.cantidadVotos} reviews)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = prod.titulo,
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Descripción",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = prod.descripcion,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 24.sp
                            )
                            
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarActions(
    price: Double,
    onAddToCart: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(
            durationMillis = 100,
            easing = FastOutSlowInEasing
        ),
        label = "buttonScale"
    )
    
    Surface(
        shadowElevation = 16.dp,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Precio",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$$price",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Button(
                onClick = {
                    isPressed = true
                    onAddToCart()
                    // Reset después de un breve delay
                    scope.launch {
                        delay(150)
                        isPressed = false
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(56.dp)
                    .scale(buttonScale)
            ) {
                Icon(
                    Icons.Default.ShoppingCart, 
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar al carrito")
            }
        }
    }
}
