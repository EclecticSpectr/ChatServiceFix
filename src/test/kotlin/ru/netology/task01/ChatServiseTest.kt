package ru.netology.task01
import org.junit.Assert.*
import org.junit.Test

class ChatServiseTest {
    private val chatServise = ChatServise()

    @Test
    fun createMessage_newMessage() {
        val authorId = UserId(0L)
        val text = "Nest text"
        val message = Message(MessageId(0L), text, authorId)
        val chatId = 0L
        val expected = listOf(message)

        chatServise.createMessage(authorId, text)
        val result = chatServise.getMessages(chatId)

        assertEquals(expected, result)
    }

    @Test
    fun getUnreadCountEmptyChats_zero() {
        val authorId = UserId(0L)
        val chatId = 0L
        val expected = 0

        val result = chatServise.getUnreadCount(chatId, authorId)

        assertEquals(expected, result)
    }

    @Test
    fun getUnreadCountEmptyChats_unreadCount() {
        val authorId = UserId(0L)
        val secondAuthorId = UserId(1L)
        val text = "Test text"
        val chatId = 0L
        val readMessageId = MessageId(10L)
        val expected = 4

        chatServise.createMessage(authorId, text)
        repeat(10) {
            chatServise.createMessage(authorId, text, chatId)
            chatServise.createMessage(secondAuthorId, text, chatId)
        }
        chatServise.readMessage(chatId, authorId, readMessageId)
        val result = chatServise.getUnreadCount(chatId, authorId)

        assertEquals(expected, result)
    }
}