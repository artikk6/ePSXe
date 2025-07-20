# Поддержка многодисковых игр в ePSXe

## Обзор

Добавлена поддержка многодисковых игр для PlayStation. Теперь пользователи могут:

1. **Создавать многодисковые игры** с несколькими образами дисков
2. **Переключаться между дисками** во время игры через меню
3. **Автоматически переходить** к следующему диску
4. **Выбирать конкретный диск** из списка доступных

## Основные компоненты

### 1. MultiDiscGame
Класс для хранения информации о многодисковой игре:
- Список всех дисков игры
- Индекс текущего диска
- Методы для управления дисками

### 2. MultiDiscChangeDialog
Диалог для выбора диска в многодисковой игре:
- Показывает список всех дисков
- Отмечает текущий диск
- Позволяет выбрать нужный диск

### 3. Расширенный класс ePSXe
Добавлены методы для работы с многодисковыми играми:
- `createMultiDiscGame()` - создание многодисковой игры
- `changeToNextDisc()` - переход к следующему диску
- `changeToPreviousDisc()` - переход к предыдущему диску
- `isMultiDiscGame()` - проверка, является ли игра многодисковой

## Использование

### Создание многодисковой игры

```java
// Создание игры с автоматическими именами дисков
String[] isoPaths = {
    "/sdcard/games/Game (Disc 1).iso",
    "/sdcard/games/Game (Disc 2).iso",
    "/sdcard/games/Game (Disc 3).iso"
};
int[] slots = {0, 0, 0};

epsxe.createMultiDiscGame("GameName", isoPaths, slots);

// Создание игры с пользовательскими именами дисков
String[] discNames = {
    "Диск 1 - Начало",
    "Диск 2 - Основная часть", 
    "Диск 3 - Финальная часть"
};

epsxe.createMultiDiscGame("GameName", isoPaths, slots, discNames);
```

### Запуск многодисковой игры

```java
if (epsxe.isMultiDiscGame()) {
    MultiDiscGame.DiscInfo firstDisc = epsxe.getMultiDiscGame().getCurrentDisc();
    if (firstDisc != null) {
        epsxe.runIso(firstDisc.getIsoPath(), firstDisc.getSlot());
    }
}
```

### Смена диска во время игры

1. **Через меню**: Выберите "Смена диска" в игровом меню
2. **Программно**: 
   ```java
   epsxe.changeToNextDisc();        // Следующий диск
   epsxe.changeToPreviousDisc();    // Предыдущий диск
   ```

### Получение информации о текущем диске

```java
if (epsxe.isMultiDiscGame()) {
    MultiDiscGame multiDiscGame = epsxe.getMultiDiscGame();
    MultiDiscGame.DiscInfo currentDisc = multiDiscGame.getCurrentDisc();
    
    System.out.println("Текущий диск: " + currentDisc.getDiscName());
    System.out.println("Индекс: " + multiDiscGame.getCurrentDiscIndex() + 
                      " из " + multiDiscGame.getDiscCount());
}
```

## Примеры

### Пример 1: Простая многодисковая игра

```java
// Создаем игру с 2 дисками
String[] isoPaths = {
    "/sdcard/games/Game (Disc 1).iso",
    "/sdcard/games/Game (Disc 2).iso"
};
int[] slots = {0, 0};

epsxe.createMultiDiscGame("MyGame", isoPaths, slots);

// Запускаем первый диск
if (epsxe.isMultiDiscGame()) {
    MultiDiscGame.DiscInfo firstDisc = epsxe.getMultiDiscGame().getCurrentDisc();
    epsxe.runIso(firstDisc.getIsoPath(), firstDisc.getSlot());
}
```

### Пример 2: Игра с пользовательскими именами дисков

```java
String[] isoPaths = {
    "/sdcard/games/RPG (Disc 1).iso",
    "/sdcard/games/RPG (Disc 2).iso",
    "/sdcard/games/RPG (Disc 3).iso"
};
int[] slots = {0, 0, 0};
String[] discNames = {
    "Диск 1 - Пролог",
    "Диск 2 - Основная история",
    "Диск 3 - Эпилог"
};

epsxe.createMultiDiscGame("RPG Game", isoPaths, slots, discNames);
```

## Особенности реализации

### Автоматическое определение типа игры
- Если `multiDiscGame` не установлен или содержит только один диск, используется стандартный диалог смены диска
- Если `multiDiscGame` содержит несколько дисков, используется специальный диалог для многодисковых игр

### Совместимость
- Полная совместимость с существующими однодисковыми играми
- Обратная совместимость - старые игры продолжают работать без изменений

### Безопасность
- Проверки на null и валидность параметров
- Логирование операций для отладки
- Graceful handling ошибок

## Технические детали

### Структура данных
```java
MultiDiscGame {
    String gameName;
    List<DiscInfo> discs;
    int currentDiscIndex;
    
    DiscInfo {
        String isoPath;
        int slot;
        String discName;
    }
}
```

### Нативные вызовы
- Используется существующий нативный метод `changedisc()` для смены диска
- Не требует изменений в нативном коде

### UI/UX
- Диалог показывает список дисков с индикацией текущего
- Простой и интуитивный интерфейс
- Поддержка русского языка

## Заключение

Реализованная функциональность позволяет полноценно эмулировать многодисковые игры PlayStation, предоставляя пользователям удобный способ управления несколькими дисками одной игры. Система полностью интегрирована в существующий код ePSXe и не нарушает работу с обычными играми. 