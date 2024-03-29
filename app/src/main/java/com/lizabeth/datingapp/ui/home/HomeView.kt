package com.lizabeth.datingapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lizabeth.datingapp.R
import com.lizabeth.datingapp.domain.profilecard.entity.CurrentProfile
import com.lizabeth.datingapp.domain.profilecard.entity.NewMatch
import com.lizabeth.datingapp.domain.profilecard.entity.Profile
import com.lizabeth.datingapp.ui.components.*
import com.lizabeth.datingapp.ui.theme.Green1
import com.lizabeth.datingapp.ui.theme.Green2
import com.lizabeth.datingapp.ui.theme.Orange
import com.lizabeth.datingapp.ui.theme.Pink
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun HomeView(
    uiState: HomeUiState,
    navigateToEditProfile: () -> Unit,
    navigateToMatchList: () -> Unit,
    navigateToNewMatch: () -> Unit,
    setLoading: () -> Unit,
    removeLastProfile: () -> Unit,
    fetchProfiles: () -> Unit,
    swipeUser: (Profile, Boolean) -> Unit,
    newMatch: SharedFlow<NewMatch>,
    currentProfile: SharedFlow<CurrentProfile>,
    setMatch: (NewMatch) -> Unit,
    setCurrentProfile: (CurrentProfile) -> Unit
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit, block = {
        newMatch.collect {
            setMatch(it)
            navigateToNewMatch()
        }
    })
    LaunchedEffect(key1 = Unit, block = {
        currentProfile.collect{
            setCurrentProfile(it)
        }
    })
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TopBarIcon(
                    imageVector = Icons.Filled.AccountCircle,
                    onClick = navigateToEditProfile
                )
                Spacer(Modifier.weight(1f))
                TopBarIcon(resId = R.drawable.logo, modifier = Modifier.size(32.dp))
                Spacer(Modifier.weight(1f))
                TopBarIcon(
                    resId = R.drawable.ic_baseline_message_24,
                    onClick = navigateToMatchList
                )
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(uiState){
                is HomeUiState.Error-> {
                    Spacer(Modifier.weight(1f))
                    Text(modifier = Modifier.padding(horizontal = 8.dp),
                        text = uiState.message ?: "", color = Color.Gray, fontSize = 16.sp, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(12.dp))
                    GradientButton(onClick = {
                        scope.launch {
                            delay(200)
                            fetchProfiles()
                        }
                    }) {
                        Text(stringResource(id = R.string.retry))
                    }
                    Spacer(Modifier.weight(1f))
                }
                HomeUiState.Loading -> {
                    Spacer(Modifier.weight(1f))
                    AnimatedGradientLogo(Modifier.fillMaxWidth(.4f))
                    Spacer(Modifier.weight(1f))
                }
                is HomeUiState.Success -> {
                    Spacer(Modifier.weight(1f))
                    Box(Modifier.padding(horizontal = 12.dp)) {
                        Text(
                            text = stringResource(id = R.string.no_more_profiles),
                            color = Color.Gray,
                            fontSize = 20.sp
                        )
                        val localDensity = LocalDensity.current
                        var buttonRowHeightDp by remember { mutableStateOf(0.dp) }

                        val swipeStates = uiState.profileList.map { rememberSwipeableCardState() }
                        uiState.profileList.forEachIndexed { index, profile ->
                            ProfileCardView(profile,
                                modifier = Modifier.swipableCard(
                                    state = swipeStates[index],
                                    onSwiped = {
                                        swipeUser(
                                            profile,
                                            it == SwipingDirection.Right
                                        )
                                        removeLastProfile()
                                    }
                                ),
                                contentModifier = Modifier.padding(bottom = buttonRowHeightDp.plus(8.dp))
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(vertical = 10.dp)
                                .onGloballyPositioned { coordinates ->
                                    buttonRowHeightDp =
                                        with(localDensity) { coordinates.size.height.toDp() }
                                },
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Spacer(Modifier.weight(1f))
                            RoundGradientButton(
                                onClick = {
                                    scope.launch {
                                        swipeStates.last().swipe(SwipingDirection.Left)
                                        removeLastProfile()
                                    }
                                },
                                enabled = swipeStates.isNotEmpty(),
                                imageVector = Icons.Filled.Close, color1 = Pink, color2 = Orange
                            )
                            Spacer(Modifier.weight(.5f))
                            RoundGradientButton(
                                onClick = {
                                    scope.launch {
                                        swipeStates.last().swipe(SwipingDirection.Right)
                                        removeLastProfile()
                                    }
                                },
                                enabled = swipeStates.isNotEmpty(),
                                imageVector = Icons.Filled.Favorite,
                                color1 = Green1,
                                color2 = Green2
                            )
                            Spacer(Modifier.weight(1f))
                        }
                    }

                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}


