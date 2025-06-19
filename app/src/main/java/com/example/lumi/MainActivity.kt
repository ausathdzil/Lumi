package com.example.lumi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lumi.data.model.StatusType
import com.example.lumi.ui.LumiHomeScreen
import com.example.lumi.ui.LumiProfileScreen
import com.example.lumi.ui.LumiViewModel
import com.example.lumi.ui.theme.LumiTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LumiTheme {
                LumiApp()
            }
        }
    }
}

enum class LumiScreen(@StringRes val title: Int) {
    Home(title = R.string.home),
    Profile(title = R.string.profile)
}

@Composable
fun LumiApp(lumiViewModel: LumiViewModel = viewModel()) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = LumiScreen.valueOf(
        backStackEntry?.destination?.route ?: LumiScreen.Home.name
    )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { LumiTopAppBar(currentScreen) },
        bottomBar = { LumiNavigationBar(navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        val uiState by lumiViewModel.uiState.collectAsState()
        val snackbarMessage = stringResource(R.string.name_updated)

        NavHost(
            navController = navController,
            startDestination = LumiScreen.Home.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            composable(route = LumiScreen.Home.name) {
                LumiHomeScreen(
                    name = uiState.user.name,
                    taskList = uiState.tasks,
                    onAddTask = lumiViewModel::addTask,
                    onUpdateTask = lumiViewModel::updateTask,
                    onDeleteTask = lumiViewModel::deleteTask,
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = LumiScreen.Profile.name) {
                LumiProfileScreen(
                    user = uiState.user,
                    taskList = uiState.tasks.filter { it.status == StatusType.COMPLETED },
                    onUpdateUser = lumiViewModel::updateUser,
                    onNameUpdated = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = snackbarMessage,
                                withDismissAction = true
                            )
                        }
                    },
                    onDeleteTask = lumiViewModel::deleteTask,
                    onDeleteAllCompletedTask = lumiViewModel::deleteAllCompleted,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LumiTopAppBar(
    currentScreen: LumiScreen,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier
    )
}

@Composable
fun LumiNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val destinations = LumiScreen.entries
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedItemIndex = remember(currentRoute) {
        destinations.indexOfFirst { it.name == currentRoute }.coerceAtLeast(0)
    }

    NavigationBar(modifier = modifier) {
        destinations.forEachIndexed { index, screen ->
            val isSelected = selectedItemIndex == index
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) {
                            when (screen) {
                                LumiScreen.Home -> Icons.Filled.Home
                                LumiScreen.Profile -> Icons.Filled.AccountCircle
                            }
                        } else {
                            when (screen) {
                                LumiScreen.Home -> Icons.Outlined.Home
                                LumiScreen.Profile -> Icons.Outlined.AccountCircle
                            }
                        },
                        contentDescription = stringResource(screen.title)
                    )
                },
                label = { Text(stringResource(screen.title)) },
                selected = isSelected,
                onClick = {
                    if (navController.currentDestination?.route != screen.name) {
                        navController.navigate(screen.name) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
