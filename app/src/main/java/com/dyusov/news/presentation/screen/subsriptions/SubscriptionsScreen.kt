@file:OptIn(ExperimentalMaterial3Api::class)

package com.dyusov.news.presentation.screen.subsriptions

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.dyusov.news.R
import com.dyusov.news.domain.entity.Article
import com.dyusov.news.presentation.theme.ui.CustomIcons
import com.dyusov.news.presentation.utils.formatDate

@Composable
fun SubscriptionsScreen(
    modifier: Modifier = Modifier,
    onNavigateToSettings: () -> Unit,
    viewModel: SubscriptionsViewModel = hiltViewModel()
) {
    // use delegate
    val state by viewModel.state.collectAsState()
    val refreshState by viewModel.refreshState.collectAsState()

    PullToRefreshBox(
        isRefreshing = refreshState,
        onRefresh = {
            viewModel.processCommand(SubscriptionsCommand.RefreshData)
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                SubscriptionsTopBar(
                    onClearArticlesClick = {
                        viewModel.processCommand(SubscriptionsCommand.ClearArticles)
                    },
                    onSettingsClick = onNavigateToSettings
                )
            }
        ) { innerPadding ->
            MainScreen(innerPadding, state, viewModel)
        }
    }
}

@Composable
fun MainScreen(
    innerPadding: PaddingValues,
    state: SubscriptionState,
    viewModel: SubscriptionsViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            Subscriptions(
                query = state.searchQuery,
                subscriptions = state.subscriptions,
                isSubscribeButtonEnabled = state.subscribeButtonEnabled,
                onTopicClick = { topic ->
                    viewModel.processCommand(
                        SubscriptionsCommand.ToggleTopicSelection(
                            topic
                        )
                    )
                },
                onQueryChanged = { searchQuery ->
                    viewModel.processCommand(
                        SubscriptionsCommand.InputTopicSearch(
                            searchQuery
                        )
                    )
                },
                onSubscribeButtonClick = {
                    viewModel.processCommand(SubscriptionsCommand.Subscribe)
                },
                onDeleteSubscription = { topic ->
                    viewModel.processCommand(
                        SubscriptionsCommand.RemoveSubscription(
                            topic
                        )
                    )
                }
            )
        }

        // is articles exist, show divider and label (and if subscriptions is not empty - how special text)
        if (state.articles.isNotEmpty()) {
            item {
                // add divider
                HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
            }
            item {
                Text(
                    text = stringResource(R.string.articles, state.articles.size),
                    fontWeight = FontWeight.Bold
                )
            }
            items(
                items = state.articles,
                key = { it.url }
            ) {
                ArticleCard(article = it)
            }
        } else if (state.subscriptions.isNotEmpty()) {
            item {
                // add divider
                HorizontalDivider()
            }
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.no_articles_for_selected_subscriptions),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SubscriptionsTopBar(
    modifier: Modifier = Modifier,
    onClearArticlesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // experimental function
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                text = stringResource(R.string.subscriptions_title)
            )
        },
        // all elements will be in one row
        actions = {
            // clear articles button
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onClearArticlesClick()
                    }
                    .padding(8.dp),
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.clear_articles)
            )
            // go to settings screen button
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onSettingsClick()
                    }
                    .padding(8.dp),
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings_screen)
            )
        }
    )
}

@Composable
private fun SubscriptionChip(
    modifier: Modifier = Modifier,
    topic: String,
    isSelected: Boolean,
    onSubscriptionClick: (String) -> Unit,
    onDeleteSubscription: (String) -> Unit
) {
    // element that can toggle to be added/removed from some filter
    FilterChip(
        modifier = modifier,
        // whether this item is selected or not
        selected = isSelected,
        onClick = {
            onSubscriptionClick(topic)
        },
        label = {
            Text(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 12.dp),
                fontSize = 18.sp,
                text = topic
            )
        },
        // "remove" icon at the end of the chip
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onDeleteSubscription(topic)
                    },
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.remove_subscription)
            )
        }
    )
}

@Composable
fun SearchBarWithAddButton(
    query: String,
    onQueryChanged: (String) -> Unit,
    isSubscribeButtonEnabled: Boolean,
    onSubscribeButtonClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        textStyle = LocalTextStyle.current.copy(
            fontSize = 20.sp
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        value = query,
        onValueChange = onQueryChanged,
        label = {
            Text(
                fontSize = 16.sp,
                text = stringResource(R.string.what_interests_you)
            )
        },
        // action on keyboard "enter"
        keyboardActions = KeyboardActions(
            onDone = {
                onSubscribeButtonClick()
                focusManager.clearFocus() // hide keyboard
            }
        ),
        singleLine = true,
        trailingIcon = {
            // add subscription icon with animation
            AnimatedVisibility(
                modifier = Modifier.padding(horizontal = 8.dp),
                visible = isSubscribeButtonEnabled,
                enter = scaleIn(), // enter animation - scale in
                exit = scaleOut()  // exit animation - scale out
            ) {
                Button(
                    onClick = onSubscribeButtonClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_subscription)
                    )
                }
            }
        }
    )
}

@Composable
private fun Subscriptions(
    modifier: Modifier = Modifier,
    subscriptions: Map<String, Boolean>,
    query: String,
    isSubscribeButtonEnabled: Boolean,
    onQueryChanged: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    onDeleteSubscription: (String) -> Unit,
    onSubscribeButtonClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        SearchBarWithAddButton(
            query = query,
            onQueryChanged = onQueryChanged,
            isSubscribeButtonEnabled = isSubscribeButtonEnabled,
            onSubscribeButtonClick = onSubscribeButtonClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        // if there are subscriptions, show them below search field, else - text "No subscriptions"
        if (subscriptions.isNotEmpty()) {
            // row with FilterChip elements
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subscriptions.forEach { (topic, isSelected) ->
                    item(key = topic) {
                        SubscriptionChip(
                            modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                            topic = topic,
                            isSelected = isSelected,
                            onSubscriptionClick = onTopicClick,
                            onDeleteSubscription = onDeleteSubscription
                        )
                    }
                }
            }
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.no_subscriptions),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ArticleCard(
    modifier: Modifier = Modifier,
    article: Article
) {
    // card item (with drop shadow)
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // load image if exists
        article.imageUrl?.let { imageUrl ->
            // load image with Coil
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.image_for_article, article.title),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .heightIn(max = 200.dp) // set max height of image
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // row with source and published date
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                fontWeight = FontWeight.Medium,
                text = article.source,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp
            )
            Text(
                text = article.publishedAt.formatDate(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

        // article header
        Text(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp),
            text = article.title,
            maxLines = 2, // header size = 2 rows max
            overflow = TextOverflow.Ellipsis, // if overflow, print "..."
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            lineHeight = 1.3.em
        )

        Spacer(modifier = Modifier.height(4.dp))

        // show article description if exists
        if (article.description.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = article.description,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                lineHeight = 1.3.em
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // row with two buttons - open article and share
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // space between items is 8 dp
        ) {
            // get local context
            val context = LocalContext.current

            // open article in browser button
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    // start new activity with intent
                    val intent = Intent(
                        /* action = */ Intent.ACTION_VIEW,
                        /* uri = */ article.url.toUri()
                    )
                    context.startActivity(intent)
                }
            ) {
                Icon(
                    imageVector = CustomIcons.OpenInNew, // use custom icon
                    contentDescription = stringResource(R.string.read_article)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.read))
            }
            // send article title + url using share button
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    // start new activity with intent
                    val intent = Intent(
                        /* action = */ Intent.ACTION_SEND
                    ).apply {
                        // MIME type
                        type = "text/plain"
                        // title + url
                        putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.url}")
                    }
                    context.startActivity(intent)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(R.string.share_article)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.share))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}