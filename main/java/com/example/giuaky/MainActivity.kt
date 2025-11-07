package com.example.giuaky
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.giuaky.ui.theme.GiuaKyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiuaKyTheme {
                val nav = rememberNavController()
                val vm: PhoneViewModel = viewModel()
                AppNav(nav, vm)
            }
        }
    }
}

@Composable
private fun AppNav(nav: NavHostController, vm: PhoneViewModel) {
    val loggedIn by vm.loggedIn.collectAsState()
    val start = if (loggedIn) "list" else "login"

    NavHost(navController = nav, startDestination = start) {
        composable("login") { LoginScreen(
            onLogin = { u, p ->
                vm.login(u, p)
                if (vm.loggedIn.value) nav.navigate("list") { popUpTo("login") { inclusive = true } }
            }
        ) }
        composable("list") { PhoneListScreen(vm = vm, onAdd = { nav.navigate("add") }) }
        composable("add") { AddPhoneScreen(vm = vm, onDone = { nav.popBackStack() }) }
    }
}
