package com.epsxe.ePSXe;

/**
 * Пример использования многодисковой функциональности
 * 
 * Этот класс демонстрирует, как создать и использовать многодисковую игру
 */
public class MultiDiscExample {
    
    /**
     * Пример создания многодисковой игры с 4 дисками
     */
    public static void createExampleMultiDiscGame(ePSXe epsxe) {
        // Пути к образам дисков
        String[] isoPaths = {
            "/sdcard/games/TestGame (Disc 1).iso",
            "/sdcard/games/TestGame (Disc 2).iso", 
            "/sdcard/games/TestGame (Disc 3).iso",
            "/sdcard/games/TestGame (Disc 4).iso"
        };
        
        // Слоты для каждого диска (обычно 0 для всех)
        int[] slots = {0, 0, 0, 0};
        
        // Пользовательские имена дисков
        String[] discNames = {
            "Диск 1 - Начало игры",
            "Диск 2 - Основная часть", 
            "Диск 3 - Финальная часть",
            "Диск 4 - Бонусный контент"
        };
        
        // Создаем многодисковую игру
        epsxe.createMultiDiscGame("TestGame", isoPaths, slots, discNames);
        
        // Запускаем первый диск
        if (epsxe.isMultiDiscGame()) {
            MultiDiscGame.DiscInfo firstDisc = epsxe.getMultiDiscGame().getCurrentDisc();
            if (firstDisc != null) {
                // Запускаем эмуляцию с первым диском
                epsxe.runIso(firstDisc.getIsoPath(), firstDisc.getSlot());
            }
        }
    }
    
    /**
     * Пример создания многодисковой игры с автоматическими именами дисков
     */
    public static void createSimpleMultiDiscGame(ePSXe epsxe) {
        String[] isoPaths = {
            "/sdcard/games/Game (Disc 1).iso",
            "/sdcard/games/Game (Disc 2).iso"
        };
        
        int[] slots = {0, 0};
        
        // Создаем игру с автоматическими именами дисков
        epsxe.createMultiDiscGame("Game", isoPaths, slots);
        
        // Запускаем первый диск
        if (epsxe.isMultiDiscGame()) {
            MultiDiscGame.DiscInfo firstDisc = epsxe.getMultiDiscGame().getCurrentDisc();
            if (firstDisc != null) {
                epsxe.runIso(firstDisc.getIsoPath(), firstDisc.getSlot());
            }
        }
    }
    
    /**
     * Пример программной смены диска
     */
    public static void changeDiscExample(ePSXe epsxe) {
        if (epsxe.isMultiDiscGame()) {
            // Смена на следующий диск
            epsxe.changeToNextDisc();
            
            // Или смена на предыдущий диск
            // epsxe.changeToPreviousDisc();
        }
    }
    
    /**
     * Пример получения информации о текущем диске
     */
    public static void getCurrentDiscInfo(ePSXe epsxe) {
        if (epsxe.isMultiDiscGame()) {
            MultiDiscGame multiDiscGame = epsxe.getMultiDiscGame();
            MultiDiscGame.DiscInfo currentDisc = multiDiscGame.getCurrentDisc();
            
            if (currentDisc != null) {
                System.out.println("Текущий диск: " + currentDisc.getDiscName());
                System.out.println("Путь к образу: " + currentDisc.getIsoPath());
                System.out.println("Слот: " + currentDisc.getSlot());
                System.out.println("Индекс диска: " + multiDiscGame.getCurrentDiscIndex() + " из " + multiDiscGame.getDiscCount());
            }
        }
    }
} 