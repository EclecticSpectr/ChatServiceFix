package ru.netology.task01

data class Message(
    val id: MessageId,
    val text: String,
    val authorId: UserId
)