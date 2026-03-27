package com.nodag.app.nodes;

/**
 * Тип ноды
 */
public enum NodeType {
    INPUT,          // Входная нода (файл, промпт)
    OUTPUT,         // Выходная нода (результат)
    PROCESSING,     // Обрабатывающая нода (ИИ сервисы)
    CUSTOM          // Пользовательская нода
}
