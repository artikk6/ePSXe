package com.epsxe.ePSXe.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.epsxe.ePSXe.MultiDiscGame;
import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.jni.libepsxe;
import com.epsxe.ePSXe.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Диалог для смены диска в многодисковой игре
 */
public final class MultiDiscChangeDialog {
    
    public static void showMultiDiscChangeDialog(final Context cont, final libepsxe e, final MultiDiscGame multiDiscGame) {
        if (multiDiscGame == null || !multiDiscGame.isMultiDisc()) {
            Log.e("MultiDiscChangeDialog", "Not a multi-disc game or game is null");
            return;
        }

        // Создаем список дисков для отображения
        List<String> discList = new ArrayList<>();
        List<MultiDiscGame.DiscInfo> discs = multiDiscGame.getDiscs();
        
        for (int i = 0; i < discs.size(); i++) {
            MultiDiscGame.DiscInfo disc = discs.get(i);
            String discText = disc.getDiscName();
            
            // Добавляем индикатор текущего диска
            if (i == multiDiscGame.getCurrentDiscIndex()) {
                discText += " (Текущий)";
            }
            
            discList.add(discText);
        }

        // Создаем адаптер для списка
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(cont, 
            android.R.layout.simple_list_item_1, discList);

        // Создаем ListView
        ListView listView = new ListView(cont);
        listView.setAdapter(adapter);

        // Создаем диалог
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setTitle("Выберите диск для смены");
        builder.setView(listView);
        final AlertDialog dialog = builder.create();

        // Обработчик выбора диска
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < discs.size()) {
                    MultiDiscGame.DiscInfo selectedDisc = discs.get(position);
                    
                    // Проверяем, что выбран не текущий диск
                    if (position != multiDiscGame.getCurrentDiscIndex()) {
                        Log.e("MultiDiscChangeDialog", "Changing to disc: " + selectedDisc.getDiscName() + 
                              " (path: " + selectedDisc.getIsoPath() + ", slot: " + selectedDisc.getSlot() + ")");
                        
                        // Вызываем нативный метод смены диска
                        e.changedisc(selectedDisc.getIsoPath(), selectedDisc.getSlot());
                        
                        // Обновляем индекс текущего диска
                        multiDiscGame.setCurrentDiscIndex(position);
                        
                        Log.e("MultiDiscChangeDialog", "Disc changed successfully");
                    } else {
                        Log.e("MultiDiscChangeDialog", "Selected disc is already current");
                    }
                    
                    // Закрываем диалог
                    DialogUtil.closeDialog(dialog);
                }
            }
        });

        // Показываем диалог
        dialog.show();
    }
} 