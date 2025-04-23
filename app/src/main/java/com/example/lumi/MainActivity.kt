package com.example.lumi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lumi.ui.theme.LumiTheme
import kotlinx.coroutines.launch

object AppDestinations {
    const val HOME_ROUTE = "home"
    const val PROFILE_ROUTE = "profile"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LumiTheme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    AnimatedVisibility(
                        visible = currentRoute == AppDestinations.HOME_ROUTE,
                        enter = slideInHorizontally(
                            initialOffsetX = { it }, animationSpec = tween(durationMillis = 300)
                        ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = slideOutHorizontally(
                            targetOffsetX = { it }, animationSpec = tween(durationMillis = 300)
                        ) + fadeOut(animationSpec = tween(durationMillis = 300))
                    ) {
                        HomeTopAppBar()
                    }
                    AnimatedVisibility(
                        visible = currentRoute == AppDestinations.PROFILE_ROUTE,
                        enter = slideInHorizontally(
                            initialOffsetX = { it }, animationSpec = tween(durationMillis = 300)
                        ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = slideOutHorizontally(
                            targetOffsetX = { it }, animationSpec = tween(durationMillis = 300)
                        ) + fadeOut(animationSpec = tween(durationMillis = 300))
                    ) {
                        ProfileTopAppBar()
                    }
                }, bottomBar = {
                    BottomBar(
                        navController = navController, currentRoute = currentRoute
                    )
                }, floatingActionButton = {
                    if (currentRoute == AppDestinations.HOME_ROUTE) {
                        CreateTaskButton()
                    }
                }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = AppDestinations.HOME_ROUTE,
                        modifier = Modifier.padding(innerPadding),
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it }, animationSpec = tween(durationMillis = 300)
                            ) + fadeIn(animationSpec = tween(durationMillis = 300))
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it }, animationSpec = tween(durationMillis = 300)
                            ) + fadeOut(animationSpec = tween(durationMillis = 300))
                        },
                        popEnterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it }, animationSpec = tween(durationMillis = 300)
                            ) + fadeIn(animationSpec = tween(durationMillis = 300))
                        },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it }, animationSpec = tween(durationMillis = 300)
                            ) + fadeOut(animationSpec = tween(durationMillis = 300))
                        }) {
                        composable(AppDestinations.HOME_ROUTE) {
                            HomeScreen(modifier = Modifier)
                        }
                        composable(AppDestinations.PROFILE_ROUTE) {
                            ProfileScreen(modifier = Modifier)
                        }
                    }
                }
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun BottomBar(
    navController: NavHostController,
    currentRoute: String?,
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
) {
    val items = listOf(
        BottomNavItem("Home", AppDestinations.HOME_ROUTE, Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem(
            "Profile",
            AppDestinations.PROFILE_ROUTE,
            Icons.Filled.AccountCircle,
            Icons.Outlined.AccountCircle
        )
    )

    NavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        windowInsets = windowInsets,
    ) {
        items.forEach { screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selected) screen.selectedIcon else screen.unselectedIcon,
                        contentDescription = screen.label
                    )
                },
                label = { Text(screen.label) },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        modifier = Modifier.padding(horizontal = 12.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                "Lumi", maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle, contentDescription = "Profile"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Good Morning,",
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(text = "Anya Taylor Joy!", fontWeight = FontWeight.Bold)
            }
        }

        SearchBar()

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AccordionItem(
                title = "To Do", items = listOf("Task 1", "Task 2"), icon = Icons.Default.Info
            )
            AccordionItem(
                title = "In Progress",
                items = listOf("Task 3", "Task 4", "Task 5"),
                icon = Icons.Default.Build
            )
            AccordionItem(
                title = "Completed", items = listOf("Task 6"), icon = Icons.Default.CheckCircle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskButton() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    ExtendedFloatingActionButton(
        onClick = { showBottomSheet = true },
        icon = { Icon(Icons.Filled.Create, contentDescription = "Create") },
        text = { Text("Create Task") })

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }, sheetState = sheetState
        ) {
            CreateTaskForm(onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskForm(onClick: () -> Unit = {}) {
    var date by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf("High") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Create To Do",
            style = MaterialTheme.typography.headlineSmall,
        )

        TextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date") },
            placeholder = { Text("DD/MM/YYYY") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            placeholder = { Text("Enter title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Enter description") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Column {
            Text(text = "Priority Level", style = MaterialTheme.typography.labelLarge)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                val priorities = listOf("High", "Medium", "Low")
                val colors = listOf(
                    Color.Red.copy(alpha = 0.2f),
                    Color.Yellow.copy(alpha = 0.2f),
                    Color.Green.copy(alpha = 0.2f)
                )
                val textColors = listOf(Color.Red, Color(0xFFFFA500), Color(0xFF28A745))

                priorities.forEachIndexed { i, priority ->
                    FilterChip(
                        selected = selectedPriority == priority,
                        onClick = { selectedPriority = priority },
                        label = { Text(priority, color = textColors[i]) },
                        leadingIcon = {
                            if (selectedPriority == priority) {
                                Icon(
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = textColors[i]
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = colors[i],
                            selectedContainerColor = colors[i],
                            labelColor = textColors[i],
                            iconColor = textColors[i],
                        ),
                        border = null
                    )
                }
            }
        }

        StatusDropdown(status = status, onStatusChange = { status = it })

        Button(
            onClick = onClick, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Create")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusDropdown(status: String, onStatusChange: (String) -> Unit) {
    val options = listOf("To Do", "In Progress", "Done")
    val icons = listOf(Icons.Default.Info, Icons.Default.Build, Icons.Default.CheckCircle)
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        TextField(
            value = status,
            onValueChange = {},
            readOnly = true,
            label = { Text("Status") },
            placeholder = { Text("Select Status") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
        )

        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { selectionOption ->
                DropdownMenuItem(text = { Text(selectionOption) }, leadingIcon = {
                    Icon(
                        icons[options.indexOf(selectionOption)],
                        contentDescription = selectionOption
                    )
                }, onClick = {
                    onStatusChange(selectionOption)
                    expanded = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    val inputField = @Composable {
        SearchBarDefaults.InputField(
            query = query,
            onQueryChange = { query = it },
            onSearch = {},
            expanded = false,
            onExpandedChange = {},
            placeholder = { Text("Search task") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        )
    }

    DockedSearchBar(
        modifier = modifier.fillMaxWidth(),
        inputField = inputField,
        expanded = false,
        onExpandedChange = {},
    ) { }
}

@Composable
fun AccordionItem(
    title: String, items: List<String>, icon: ImageVector, initiallyExpanded: Boolean = false
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = { expanded = !expanded }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Row {
                    Text(
                        text = items.size.toString(), color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    items.forEachIndexed { index, item ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = item)
                                IconButton(onClick = { /* TODO */ }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                                }
                            }
                            if (index != items.lastIndex) {
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBar() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        modifier = Modifier.padding(horizontal = 12.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                "Profile", maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Settings, contentDescription = "Settings"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = "Anya Taylor Joy", fontWeight = FontWeight.Bold
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = "Tasks Overview", style = MaterialTheme.typography.labelLarge)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TaskCard(
                    modifier = Modifier.weight(1f),
                    title = "Created",
                    number = 8,
                    icon = Icons.Default.Info
                )
                TaskCard(
                    modifier = Modifier.weight(1f),
                    title = "Completed",
                    number = 2,
                    icon = Icons.Default.CheckCircle
                )
            }
        }

        ProfileForm()
    }
}

@Preview
@Composable
fun ProfilePreview() {
    LumiTheme {
        ProfileScreen()
    }
}

@Composable
fun ProfileForm() {
    var name by remember { mutableStateOf("Anya Taylor Joy") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Edit Profile",
            style = MaterialTheme.typography.labelLarge,
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            placeholder = { Text("Enter name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)) {
            Text("Save")
        }
    }
}

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    title: String,
    number: Number,
    icon: ImageVector,
) {
    OutlinedCard(modifier = modifier.height(80.dp)) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
            )
            Column {
                Text(text = number.toString(), fontWeight = FontWeight.SemiBold)
                Text(text = title, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}