package ru.netology.task01

data class Chat(
    val id: Long,
    val messages: List<Message>,
    val readMessages: Map<UserId, MessageId> = emptyMap()
)