package com.epsxe.ePSXe;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для хранения информации о многодисковой игре
 */
public class MultiDiscGame {
    private String gameName;
    private List<DiscInfo> discs;
    private int currentDiscIndex;

    public MultiDiscGame(String gameName) {
        this.gameName = gameName;
        this.discs = new ArrayList<>();
        this.currentDiscIndex = 0;
    }

    public void addDisc(String isoPath, int slot) {
        discs.add(new DiscInfo(isoPath, slot));
    }

    public void addDisc(String isoPath, int slot, String discName) {
        discs.add(new DiscInfo(isoPath, slot, discName));
    }

    public String getGameName() {
        return gameName;
    }

    public List<DiscInfo> getDiscs() {
        return discs;
    }

    public int getCurrentDiscIndex() {
        return currentDiscIndex;
    }

    public void setCurrentDiscIndex(int index) {
        if (index >= 0 && index < discs.size()) {
            this.currentDiscIndex = index;
        }
    }

    public DiscInfo getCurrentDisc() {
        if (currentDiscIndex >= 0 && currentDiscIndex < discs.size()) {
            return discs.get(currentDiscIndex);
        }
        return null;
    }

    public DiscInfo getDisc(int index) {
        if (index >= 0 && index < discs.size()) {
            return discs.get(index);
        }
        return null;
    }

    public int getDiscCount() {
        return discs.size();
    }

    public boolean isMultiDisc() {
        return discs.size() > 1;
    }

    /**
     * Класс для хранения информации об одном диске
     */
    public static class DiscInfo {
        private String isoPath;
        private int slot;
        private String discName;

        public DiscInfo(String isoPath, int slot) {
            this.isoPath = isoPath;
            this.slot = slot;
            this.discName = "Disc " + (slot + 1);
        }

        public DiscInfo(String isoPath, int slot, String discName) {
            this.isoPath = isoPath;
            this.slot = slot;
            this.discName = discName;
        }

        public String getIsoPath() {
            return isoPath;
        }

        public int getSlot() {
            return slot;
        }

        public String getDiscName() {
            return discName;
        }

        public void setDiscName(String discName) {
            this.discName = discName;
        }
    }
} 