import java.io.Serializable;

public enum MessageType implements Serializable {
    TEXT,               // Текстовое сообщение
    EMOJI,              // Эмодзи
    IMAGE,              // Изображение
    FILE,               // Файл
    PRIVATE_MESSAGE,    // Приватное сообщение
    SEARCH_REQUEST,     // Запрос поиска
    SEARCH_RESULT,      // Результат поиска
    USER_LIST,          // Список пользователей
    STATUS_UPDATE,      // Обновление статуса
    LOGIN,              // Вход в систему
    LOGOUT              // Выход из системы
}