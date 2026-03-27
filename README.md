# NodAG - ИИ Нодовый Агрегатор

🤖 **NodAG** - это Android-приложение для создания воркфлоу из ИИ сервисов через систему нод.

## 📋 Описание

NodAG позволяет визуально составлять цепочки из различных ИИ-сервисов (GPT, Claude, Gemini и др.) с помощью системы нод. Каждая нода представляет собой отдельный шаг в обработке данных.

## ✨ Функции

### Встроенные ноды
- 📥 **Input Prompt** - ввод текстового промпта
- 📥 **Input File** - загрузка файла
- 📤 **Output** - вывод результата
- 🤖 **GPT-4** - подключение к OpenAI API
- 🤖 **Claude** - подключение к Anthropic API
- 🤖 **Gemini** - подключение к Google AI API

### Кастомные ноды
- Создание пользовательских нод с любым API endpoint
- Настройка входов и выходов
- Сохранение API ключей
- HTTP метод (GET/POST)
- Шаблонизация тела запроса

## 🏗️ Архитектура

```
app/src/main/java/com/nodag/app/
├── nodes/           # Базовые классы нод
│   ├── BaseNode.kt        # Базовый класс ноды
│   ├── BuiltInNodes.kt    # Встроенные ноды
│   └── CustomNode.kt      # Кастомные ноды
├── workflow/        # Управление воркфлоу
│   └── WorkflowManager.kt # Менеджер воркфлоу
└── ui/              # UI компоненты
    ├── MainActivity.kt
    ├── WorkflowEditorActivity.kt
    └── CreateNodeActivity.kt
```

## 🚀 Быстрый старт

### Требования
- Android Studio Hedgehog (2023.1.1) или новее
- JDK 17
- Android SDK 34

### Сборка
```bash
./gradlew assembleDebug
```

### Тесты
```bash
./gradlew test
```

## 📦 Структура проекта

```
NodAG/
├── app/                      # Модуль приложения
│   ├── src/
│   │   ├── main/            # Исходный код
│   │   └── test/            # Юнит-тесты
│   └── build.gradle
├── design/                   # Макеты интерфейса (PIL)
│   ├── generate_mockups.py
│   ├── main_interface.png
│   ├── custom_node_dialog.png
│   └── node_palette.png
├── screenshots/              # Скриншоты приложения
├── assets/                   # Ресурсы приложения
├── docs/                     # Документация
├── .github/workflows/        # GitHub Actions
└── build.gradle              # Корневой build.gradle
```

## 🎨 Дизайн

Макеты интерфейса сгенерированы с помощью Python + PIL. Для перегенерации:

```bash
cd design
python generate_mockups.py
```

## 🔧 Технологии

- **Kotlin** - основной язык разработки
- **AndroidX** - современные Android компоненты
- **Material Design** - UI компоненты
- **Retrofit** - HTTP клиент для API запросов
- **Coroutines** - асинхронное программирование
- **JUnit** - тестирование

## 📝 Лицензия

MIT License

## 👥 Авторы

Создано для объединения ИИ сервисов в единые воркфлоу.

## 📸 Скриншоты

Скриншоты будут добавлены в папку `screenshots/`

---

**Статус:** В разработке 🚧
