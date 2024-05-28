package com.how.dailyloger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.how.dailyloger.navigation.BottomNavController
import com.how.dailyloger.ui.theme.DailyLogerTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyLogerTheme {
                val selectedItemIndex = rememberSaveable {
                    mutableIntStateOf(0)
                }
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, bottomNavigationItem ->
                                NavigationBarItem(
                                    selected = currentDestination?.hierarchy?.any { it.route == bottomNavigationItem.title } == true,
                                    onClick = {
                                        selectedItemIndex.intValue = index
                                        navController.navigate(bottomNavigationItem.title){
                                            popUpTo(navController.graph.startDestinationId){
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = bottomNavigationItem.title
                                        )
                                    },
                                    icon = {
                                        BadgedBox(
                                            badge ={
                                                if(bottomNavigationItem.badgeCount!=null){
                                                    Text(
                                                        text = bottomNavigationItem.badgeCount.toString()
                                                    )
                                                } else if(bottomNavigationItem.hasNews){
                                                    Badge()
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if(index == selectedItemIndex.intValue){
                                                    bottomNavigationItem.selectedIcon
                                                }else{
                                                    bottomNavigationItem.unSelectedIcon
                                                },
                                                contentDescription = bottomNavigationItem.title
                                            )
                                        }
                                    },
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        BottomNavController(navController =navController)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DailyLogerTheme {
        Greeting("Android")
    }
}