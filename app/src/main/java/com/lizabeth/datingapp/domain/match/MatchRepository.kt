package com.lizabeth.datingapp.domain.match

import com.lizabeth.datingapp.domain.match.entity.Match

interface MatchRepository {
    suspend fun getMatches(): List<Match>
}