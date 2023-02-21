package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface AbstractStorage<T> {

    /**
     * Получение списка всех обобщенных объектов типа T хранящихся в базе (памяти).
     * @return список объектов типа T.
     */
    List<T> getAll();

    /**
     * Получение обобщенного объекта типа T по уникальному идентификатору.
     * @param id уникальный идентификатор объекта типа T.
     * @return сам объект типа T.
     */
    T getById(int id);

    /**
     * Добавление обобщенного объекта типа T в базу (память).
     * @param t объект типа T.
     * @return созданный методом объект типа T хранящийся в базе (памяти).
     */
    T add(T t);

    /**
     * Обновление данных обобщенного объекта типа T в базе (памяти).
     * @param t объект.
     * @return объекта типа T с обновленными данными хранящийся в базе (памяти).
     */
    T update(T t);
}