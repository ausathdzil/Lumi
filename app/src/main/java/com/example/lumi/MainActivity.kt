package com.example.lumi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lumi.ui.LumiHomeScreen
import com.example.lumi.ui.LumiProfileScreen
import com.example.lumi.ui.theme.LumiTheme

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
    Home(title = R.string.app_name),
    Profile(title = R.string.profile)
}

@Composable
fun LumiApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = LumiScreen.valueOf(
        backStackEntry?.destination?.route ?: LumiScreen.Home.name
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { LumiTopAppBar(currentScreen) },
        bottomBar = { LumiNavigationBar(navController) },
        floatingActionButton = {  }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LumiScreen.Home.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = LumiScreen.Home.name) {
                LumiHomeScreen(modifier = Modifier.fillMaxSize())
            }
            composable(route = LumiScreen.Profile.name) {
                LumiProfileScreen(modifier = Modifier.fillMaxSize())
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
        title = {
            Text(
                text = stringResource(currentScreen.title),
                fontWeight = FontWeight.SemiBold
            )
        },
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
                                LumiScreen.Profile -> Icons.Filled.Person
                            }
                        } else {
                            when (screen) {
                                LumiScreen.Home -> Icons.Outlined.Home
                                LumiScreen.Profile -> Icons.Outlined.Person
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
