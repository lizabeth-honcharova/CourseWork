package com.lizabeth.datingapp.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizabeth.datingapp.domain.message.MessageRepository
import com.lizabeth.datingapp.domain.match.entity.Match
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(private val messageRepository: MessageRepository): ViewModel() {
    private val _match = MutableStateFlow(MatchState(null, "Nothing"))
    val match = _match.asStateFlow()

    fun getMessages(matchId: String) = messageRepository.getMessages(matchId)

    fun sendMessage(text: String){
        val matchId = _match.value.selectedMatch?.id ?: return
        viewModelScope.launch {
            try {
                messageRepository.sendMessage(matchId, text)
            }catch (e: Exception){
                //Delete the message from the displayed list
            }
        }
    }

    fun setMatch(match: Match){
        _match.update { it.copy(selectedMatch = match, currentState = match.id) }
        _match.value.selectedMatch?.id?.let { Log.d("IM SET", it) }
    }
}

data class MatchState(
    val selectedMatch: Match?,
    val currentState: String)