package br.com.vinibelo.trabalhoapicompose.ui.theme.car

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.common.SharedCarsViewModel
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.details.FormCarScreen
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.list.ListCarsScreen

private object Screens {
    const val LIST_CARS = "listCars"
    const val FORM_CAR = "formCar"
}

object Arguments {
    const val CAR_ID = "carId"
}

private object Routes {
    const val LIST_CARS = Screens.LIST_CARS
    const val FORM_CAR = "${Screens.FORM_CAR}?${Arguments.CAR_ID}={${Arguments.CAR_ID}}"
}

@Composable
fun AppCars(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screens.LIST_CARS
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        val sharedCarsViewModel = SharedCarsViewModel()
        composable(route = Routes.LIST_CARS) {
            ListCarsScreen(
                sharedCarsViewModel = sharedCarsViewModel,
                onCarPressed = { car ->
                    navController.navigate("${Screens.FORM_CAR}?${Arguments.CAR_ID}=${car.id}")
                }
            )
        }
        composable(
            route = Routes.FORM_CAR,
            arguments = listOf(
                navArgument(name = Arguments.CAR_ID) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            FormCarScreen(
                sharedCarsViewModel = sharedCarsViewModel,
                onReturnPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}