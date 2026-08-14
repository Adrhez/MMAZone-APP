package com.example.mmazone.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mmazone.screens.aboutUs.AboutUs
import com.example.mmazone.screens.dashboard.Dashboard
import com.example.mmazone.screens.eventDetails.EventDetails
import com.example.mmazone.screens.fighterProfile.FighterProfile
import com.example.mmazone.screens.home.HomeScreen
import com.example.mmazone.screens.home.RegisterScreen
import com.example.mmazone.screens.profile.ProfileScreen
import com.example.mmazone.screens.rankings.Rankings
import com.example.mmazone.screens.settings.Settings
import com.example.mmazone.screens.newsDetails.NewsDetails
import com.example.mmazone.screens.newsDetails.NewsViewModel
import com.example.mmazone.screens.pastEvents.PastEventDetails
import com.example.mmazone.screens.pastEvents.PastEventsMenu
import com.example.mmazone.screens.pastEvents.PastEventsViewModel
import com.google.firebase.auth.FirebaseAuth

object Routes {

    const val HOME = "home"

    const val REGISTER = "register"
    const val DASHBOARD = "dashboard"

    const val PROFILE = "profile"
    const val EVENT_DETAILS = "eventDetails"

    const val FIGHTER_PROFILE = "fighterProfile"

    const val RANKINGS = "rankings"

    const val PAST_EVENTS_MENU = "pastEventsMenu"
    const val PAST_EVENT_DETAILS = "pastEventDetails"

    const val NEWS = "news"

    const val ABOUT_US = "aboutUs"

    const val SETTINGS = "settings"

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val sharedNewsViewModel: NewsViewModel = viewModel()
    val sharedPastEventsViewModel: PastEventsViewModel = viewModel()

    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) Routes.DASHBOARD else Routes.HOME
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onRegisterClick = {navController.navigate(Routes.REGISTER)}
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onLoginClick = {navController.navigate(Routes.HOME)}
            )
        }

        composable(Routes.DASHBOARD) {
            Dashboard(
                onEventClick = { navController.navigate(Routes.EVENT_DETAILS) },
                onFighterClick = { navController.navigate(Routes.FIGHTER_PROFILE) },
                onProfileClick = { navController.navigate(Routes.PROFILE)},
                onRankingsClick = { navController.navigate(Routes.RANKINGS)},
                newsViewModel = sharedNewsViewModel,
                onNewsClick = { articleIndex ->
                    navController.navigate("newsDetails/$articleIndex")
                },
                onAboutClick = { navController.navigate(Routes.ABOUT_US)},
                onSettingsClick = {navController.navigate(Routes.SETTINGS)},
                onPastEventsClick = { navController.navigate(Routes.PAST_EVENTS_MENU) }
            )
        }

        composable(Routes.EVENT_DETAILS) {
            EventDetails(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.RANKINGS) {
            Rankings(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.FIGHTER_PROFILE) {
            FighterProfile(
                fighterId = "topuria",
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("newsDetails/{articleIndex}") { backStackEntry ->
            val indexString = backStackEntry.arguments?.getString("articleIndex") ?: "0"
            val index = indexString.toIntOrNull() ?: 0

            NewsDetails(
                newsIndex = index,
                viewModel = sharedNewsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.PAST_EVENTS_MENU) {
            PastEventsMenu(
                viewModel = sharedPastEventsViewModel,
                onBackClick = { navController.popBackStack() },
                onEventClick = { eventId ->
                    val encodedId = java.net.URLEncoder.encode(eventId, "UTF-8")
                    navController.navigate("${Routes.PAST_EVENT_DETAILS}/$encodedId")
                }
            )
        }

        composable("${Routes.PAST_EVENT_DETAILS}/{eventId}") { backStackEntry ->
            val rawId = backStackEntry.arguments?.getString("eventId") ?: ""
            val eventId = java.net.URLDecoder.decode(rawId, "UTF-8")
            PastEventDetails(
                eventId = eventId,
                viewModel = sharedPastEventsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }


        composable(Routes.ABOUT_US) {
            AboutUs(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            Settings(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Routes.HOME) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
