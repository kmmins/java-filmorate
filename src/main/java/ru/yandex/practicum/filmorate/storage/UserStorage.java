package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    /**
     * Вспомогательный метод (проверка e-mail).
     * @param user проверяется на содержание его почты в базе (памяти).
     * @return булево значение для почты пользователя на содержание её в базе (памяти).
     */
    boolean containsEmail(User user);

    /**
     * Добавление пользователя в базу (память).
     * @param user объект.
     * @return созданного пользователя с уникальным id.
     */
    User addUser(User user);

    /**
     * Вспомогательный метод (проверка id).
     * @param id пользователя для проверки на содержание его в базе (памяти).
     * @return булево значение для пользователя на содержание его в базе (памяти).
     */
    boolean notContainsUser(int id);

    /**
     * Обновление данных пользователя в базе (памяти).
     * @param user объект.
     * @return пользователя с обновленными данными.
     */
    User updUser(User user);

    /**
     * Получение всех пользователей хранящихся в базе (памяти).
     * @return список пользователей.
     */
    List<User> getAllUsers();

    /**
     * Получение пользователя по уникальному идентификатору.
     * @param id уникальный идентификатор пользователя.
     * @return сам объект типа пользователь.
     */
    User getUserById(int id);
}
