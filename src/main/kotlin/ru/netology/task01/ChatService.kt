package ru.netology.task01

class ChatServise {

    private var chatId = 0L
    private var messageId = 0L
    private var chats: List<Chat> = emptyList()

    fun createMessage(authorId: UserId, text: String, chatId: Long = -1L): Message {
        val message = Message(MessageId(messageId++), text, authorId)

        if (chats.none { it.id == chatId }) {
            chats = chats + Chat(this.chatId++, listOf(message))
            return message
        }

        chats = chats.map { chat ->
            if (chat.id == chatId) {
                chat.copy(messages = chat.messages + message)
            } else {
                chat
            }
        }
        return message
    }

    fun editMessage(messageId: MessageId, text: String) {
        chats = chats.map { chat ->
            chat.copy(
                messages = chat.messages.map { message ->
                    if (message.id == messageId) {
                        message.copy(text = text)
                    } else {
                        message
                    }
                }
            )
        }
    }

    fun remoteMessage(messageId: MessageId) {
        chats = chats.map { chat ->
            chat.copy(messages = chat.messages.filter { it.id != messageId })
        }
    }

    fun getUnreadCount(chatId: Long, userId: UserId): Int {
        val chatIndex = chats.indexOfFirst { it.id == chatId }
            .takeIf { it >= 0 } ?: return 0

        val targetChat = chats[chatIndex]
        val readMessageId = targetChat.readMessages[userId]
        val readMessages = targetChat.messages.takeWhile {
            it.id != readMessageId
        }
        return readMessages.filterNot {
            it.authorId == userId
        }.size
    }

    fun readMessage(chatId: Long, userId: UserId, messageId: MessageId) {
        val index = chats.indexOfFirst { it.id == chatId }
            .takeIf { it >= 0 } ?: return

        val targetChat = chats[index]
        val newReadMessages = targetChat.readMessages.toMutableMap()
            .apply {
                put(userId, messageId)
            }
        chats = chats.toMutableList()
            .apply {
                set(index, targetChat.copy(readMessages = newReadMessages))
            }
    }

    fun removeChat(chatId: Long) {
        chats = chats.filter { it.id != chatId }
    }

    fun getMessages(chatId: Long): List<Message> =
        chats
            .sortedByDescending { it.id }
            .filter {
                it.id == chatId
            }
            .map { it.messages }
            .flatten()

    fun getChats(): List<Chat> {
        return chats
    }

    fun getLatestMessagesFromChats(): List<String> {
        val latestMessages = chats.map { chat ->
            chat.messages.lastOrNull()?.text ?: "нет сообщений"
        }
        return latestMessages
    }

    fun getMessagesFromChat(chatId: Long, userId: UserId, count: Int): List<Message> {
        val chatIndex = chats.indexOfFirst { it.id == chatId }
        if (chatIndex == -1) {
            return emptyList()
        }
        val targetChat = chats[chatIndex]
        val readMessageId = targetChat.readMessages[userId]
        val readMessages = targetChat.messages.takeWhile {
            it.id != readMessageId
        }
        val messages = targetChat.messages.takeLast(count)
        val newReadMessages = targetChat.readMessages.toMutableMap()
            .apply {
                put(userId, messages.lastOrNull()?.id ?: MessageId(0))
            }
        chats = chats.toMutableList()
            .apply {
                set(chatIndex, targetChat.copy(readMessages = newReadMessages))
            }
        return messages
    }
}