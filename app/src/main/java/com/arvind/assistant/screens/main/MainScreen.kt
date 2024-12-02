package com.arvind.assistant.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arvind.assistant.db.DBOps
import com.arvind.assistant.navigation.items
import com.arvind.assistant.navigation.BottomNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainNavController: NavHostController,
    dbOps: DBOps
) {
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
                        selected = currentDestination?.hierarchy?.any { it.route == bottomNavigationItem.route } == true,
                        onClick = {
                            selectedItemIndex.intValue = index
                            navController.navigate(bottomNavigationItem.route){
                                popUpTo(navController.graph.startDestinationId){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = {
                            Text(
                                text = bottomNavigationItem.title,
                                color = MaterialTheme.colorScheme.tertiary
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
                                    painter = if(index == selectedItemIndex.intValue){
                                        painterResource(bottomNavigationItem.selectedIcon)
                                    }else{
                                        painterResource(bottomNavigationItem.unSelectedIcon)
                                    },
                                    contentDescription = bottomNavigationItem.title,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(25.dp)
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
            BottomNavController(
                bottomNavController =navController,
                mainNavController = mainNavController,
                dbOps = dbOps
            )
        }
    }
}