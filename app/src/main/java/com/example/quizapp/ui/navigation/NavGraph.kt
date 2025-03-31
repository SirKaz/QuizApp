package com.example.quizapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quizapp.QuizApplication
import com.example.quizapp.ui.screens.addquestion.AddEditQuestionScreen
import com.example.quizapp.ui.screens.addquestion.AddEditQuestionViewModel
import com.example.quizapp.ui.screens.addeditbank.AddEditBankScreen
import com.example.quizapp.ui.screens.addeditbank.AddEditBankViewModel
import com.example.quizapp.ui.screens.banklist.QuestionBankListScreen
import com.example.quizapp.ui.screens.banklist.QuestionBankListViewModel
import com.example.quizapp.ui.screens.questionbank.QuestionBankScreen
import com.example.quizapp.ui.screens.questionbank.QuestionBankViewModel
import com.example.quizapp.ui.screens.quiz.QuizScreen
import com.example.quizapp.ui.screens.quiz.QuizViewModel

sealed class Screen(val route: String) {
    data object BankList : Screen("bank_list")
    data object AddBank : Screen("add_bank")

    data class EditBank(val bankId: Int? = null) : Screen(
        route = "edit_bank/{bankId}"
    ) {
        companion object {
            fun createRoute(bankId: Int) = "edit_bank/$bankId"
        }
    }

    data class QuestionList(val bankId: Int? = null) : Screen(
        route = "bank/{bankId}/questions"
    ) {
        companion object {
            fun createRoute(bankId: Int) = "bank/$bankId/questions"
        }
    }

    data class AddQuestion(val bankId: Int? = null) : Screen(
        route = "bank/{bankId}/add_question"
    ) {
        companion object {
            fun createRoute(bankId: Int) = "bank/$bankId/add_question"
        }
    }

    data class EditQuestion(val bankId: Int? = null, val questionId: Int? = null) : Screen(
        route = "bank/{bankId}/question/{questionId}"
    ) {
        companion object {
            fun createRoute(bankId: Int, questionId: Int) = "bank/$bankId/question/$questionId"
        }
    }

    data class Quiz(val bankId: Int? = null) : Screen(
        route = "bank/{bankId}/quiz"
    ) {
        companion object {
            fun createRoute(bankId: Int) = "bank/$bankId/quiz"
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    val application = navController.context.applicationContext as QuizApplication
    val appContainer = application.appContainer

    NavHost(
        navController = navController,
        startDestination = Screen.BankList.route
    ) {
        // Bank List Screen (Main Screen)
        composable(Screen.BankList.route) {
            val viewModel = remember {
                QuestionBankListViewModel.Factory(appContainer.questionBankRepository)
                    .create(QuestionBankListViewModel::class.java)
            }

            QuestionBankListScreen(
                viewModel = viewModel,
                onNavigateToAddBank = {
                    navController.navigate(Screen.AddBank.route)
                },
                onNavigateToEditBank = { bankId ->
                    navController.navigate(Screen.EditBank.createRoute(bankId))
                },
                onNavigateToQuestions = { bankId ->
                    navController.navigate(Screen.QuestionList.createRoute(bankId))
                },
                onNavigateToQuiz = { bankId ->
                    navController.navigate(Screen.Quiz.createRoute(bankId))
                }
            )
        }

        // Add Bank Screen
        composable(Screen.AddBank.route) {
            val viewModel = remember {
                AddEditBankViewModel.Factory(appContainer.questionBankRepository)
                    .create(AddEditBankViewModel::class.java)
            }

            AddEditBankScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Edit Bank Screen
        composable(
            route = Screen.EditBank().route,
            arguments = listOf(
                navArgument("bankId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val bankId = backStackEntry.arguments?.getInt("bankId") ?: return@composable
            val viewModel = remember {
                AddEditBankViewModel.Factory(appContainer.questionBankRepository, bankId)
                    .create(AddEditBankViewModel::class.java)
            }

            AddEditBankScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Question List Screen (for specific bank)
        composable(
            route = Screen.QuestionList().route,
            arguments = listOf(
                navArgument("bankId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val bankId = backStackEntry.arguments?.getInt("bankId") ?: return@composable
            val viewModel = remember {
                QuestionBankViewModel.Factory(
                    repository = appContainer.questionRepository,
                    bankId = bankId
                ).create(QuestionBankViewModel::class.java)
            }

            QuestionBankScreen(
                viewModel = viewModel,
                onNavigateToAddQuestion = {
                    navController.navigate(Screen.AddQuestion.createRoute(bankId))
                },
                onNavigateToEditQuestion = { questionId ->
                    navController.navigate(Screen.EditQuestion.createRoute(bankId, questionId))
                },
                onNavigateToQuiz = {
                    navController.navigate(Screen.Quiz.createRoute(bankId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Add Question Screen
        composable(
            route = Screen.AddQuestion().route,
            arguments = listOf(
                navArgument("bankId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val bankId = backStackEntry.arguments?.getInt("bankId") ?: return@composable
            val viewModel = remember {
                AddEditQuestionViewModel.Factory(
                    repository = appContainer.questionRepository,
                    bankId = bankId
                ).create(AddEditQuestionViewModel::class.java)
            }

            AddEditQuestionScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Edit Question Screen
        composable(
            route = Screen.EditQuestion().route,
            arguments = listOf(
                navArgument("bankId") { type = NavType.IntType },
                navArgument("questionId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val bankId = backStackEntry.arguments?.getInt("bankId") ?: return@composable
            val questionId = backStackEntry.arguments?.getInt("questionId") ?: return@composable
            val viewModel = remember {
                AddEditQuestionViewModel.Factory(
                    repository = appContainer.questionRepository,
                    bankId = bankId,
                    questionId = questionId
                ).create(AddEditQuestionViewModel::class.java)
            }

            AddEditQuestionScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Quiz Screen
        composable(
            route = Screen.Quiz().route,
            arguments = listOf(
                navArgument("bankId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val bankId = backStackEntry.arguments?.getInt("bankId") ?: return@composable
            val viewModel = remember {
                QuizViewModel.Factory(
                    repository = appContainer.questionRepository,
                    bankId = bankId
                ).create(QuizViewModel::class.java)
            }

            QuizScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}