@file:OptIn(ExperimentalCoroutinesApi::class)

package com.dyusov.news.presentation.screen.subsriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyusov.news.domain.entity.Article
import com.dyusov.news.domain.usecase.articles.AddSubscriptionUseCase
import com.dyusov.news.domain.usecase.articles.ClearAllArticlesUseCase
import com.dyusov.news.domain.usecase.articles.DeleteSubscriptionUseCase
import com.dyusov.news.domain.usecase.articles.GetAllSubscriptionsUseCase
import com.dyusov.news.domain.usecase.articles.GetArticlesByTopicsUseCase
import com.dyusov.news.domain.usecase.articles.UpdateSubscribedArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val addSubscriptionUseCase: AddSubscriptionUseCase,
    private val clearAllArticlesUseCase: ClearAllArticlesUseCase,
    private val deleteSubscriptionUseCase: DeleteSubscriptionUseCase,
    private val getAllSubscriptionsUseCase: GetAllSubscriptionsUseCase,
    private val getArticlesByTopicsUseCase: GetArticlesByTopicsUseCase,
    private val updateSubscribedArticlesUseCase: UpdateSubscribedArticlesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SubscriptionState())
    private val _isRefreshing = MutableStateFlow(false)

    val state = _state.asStateFlow()
    val refreshState = _isRefreshing.asStateFlow()

    init {
        observeSubscriptions()
        observeSelectedTopic()
    }

    private fun observeSubscriptions() {
        getAllSubscriptionsUseCase()
            .onEach { subscriptions ->
                _state.update { currentState ->
                    val updatedTopics = subscriptions.associateWith { topic ->
                        // if topic was already shown - set previous state, else - set true
                        currentState.subscriptions[topic] ?: true
                    }
                    currentState.copy(subscriptions = updatedTopics)
                }
            }.launchIn(viewModelScope)
    }

    private fun observeSelectedTopic() {
        state.map { it.selectedTopics }
            .distinctUntilChanged() // call only if items are changed
            .flatMapLatest { topics ->
                getArticlesByTopicsUseCase(topics)
            }
            .onEach { articles ->
                _state.update { currentState ->
                    currentState.copy(articles = articles)
                }
            }.launchIn(viewModelScope)
    }

    fun processCommand(command: SubscriptionsCommand) {
        when (command) {
            SubscriptionsCommand.ClearArticles -> {
                viewModelScope.launch {
                    clearAllArticlesUseCase(state.value.selectedTopics)
                }
            }

            is SubscriptionsCommand.InputTopicSearch -> {
                _state.update { currentState ->
                    currentState.copy(searchQuery = command.topic)
                }
            }

            SubscriptionsCommand.RefreshData -> {
                _isRefreshing.update { true }
                viewModelScope.launch {
                    updateSubscribedArticlesUseCase()
                    _isRefreshing.update { false }
                }
            }

            is SubscriptionsCommand.RemoveSubscription -> {
                viewModelScope.launch {
                    deleteSubscriptionUseCase(command.topic)
                }
            }

            SubscriptionsCommand.Subscribe -> {
                viewModelScope.launch {
                    addSubscriptionUseCase(state.value.searchQuery.trim())
                    // clear search field
                    _state.update { currentState ->
                        currentState.copy(searchQuery = "")
                    }
                }
            }

            is SubscriptionsCommand.ToggleTopicSelection -> {
                _state.update { currentState ->
                    val subscriptions = currentState.subscriptions.toMutableMap()
                    val isSelected = subscriptions[command.topic] ?: false
                    subscriptions[command.topic] = !isSelected
                    currentState.copy(subscriptions = subscriptions)
                }
            }
        }
    }
}

// commands
sealed interface SubscriptionsCommand {

    data class InputTopicSearch(val topic: String) : SubscriptionsCommand

    data object Subscribe : SubscriptionsCommand

    data object RefreshData : SubscriptionsCommand

    data class ToggleTopicSelection(val topic: String) : SubscriptionsCommand

    data object ClearArticles : SubscriptionsCommand

    data class RemoveSubscription(val topic: String) : SubscriptionsCommand

}

// screen state
data class SubscriptionState(
    val searchQuery: String = "",
    val subscriptions: Map<String, Boolean> = mapOf(), // key = topic, value = whether filter is on
    val articles: List<Article> = listOf()
) {
    val selectedTopics: List<String>
        get() = subscriptions.filter { it.value }.map { it.key }

    val subscribeButtonEnabled: Boolean
        get() = searchQuery.isNotBlank()
}