package com.epsxe.ePSXe.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.epsxe.ePSXe.IsoFileSelected;
import com.epsxe.ePSXe.OptionCD;
import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.cdArrayAdapter;
import com.epsxe.ePSXe.jni.libepsxe;
import com.epsxe.ePSXe.pbpFile;
import com.epsxe.ePSXe.util.DialogUtil;
import com.epsxe.ePSXe.util.PSXUtil;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class ChangediscDialog {
    /* JADX INFO: Access modifiers changed from: private */
    public static List<OptionCD> fillCD(Context c, File f) {
        File[] dirs = f.listFiles();
        ArrayList arrayList = new ArrayList();
        List<OptionCD> dir = new ArrayList<>();
        try {
            for (File ff : dirs) {
                if (ff.isDirectory()) {
                    dir.add(new OptionCD(ff.getName(), "Folder", ff.getAbsolutePath(), 0));
                } else {
                    Log.e("folder", "File " + ff.getName());
                    if (PSXUtil.isIsoExtension(ff.getName())) {
                        long msize = ff.length() / 1048576;
                        if (msize > 2) {
                            if (ff.getName().toLowerCase().endsWith(".pbp")) {
                                pbpFile mfile = new pbpFile(ff.getAbsolutePath(), ff.getName());
                                int n = mfile.getNumFiles();
                                for (int i = 0; i < n; i++) {
                                    arrayList.add(new OptionCD(mfile.getFileName(i + 1), c.getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + (msize / n) + " Mbytes", ff.getAbsolutePath(), i));
                                }
                            } else {
                                arrayList.add(new OptionCD(ff.getName(), c.getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + msize + " Mbytes", ff.getAbsolutePath(), 0));
                            }
                        } else {
                            arrayList.add(new OptionCD(ff.getName(), c.getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + ff.length() + " Bytes", ff.getAbsolutePath(), 0));
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        Collections.sort(dir);
        Collections.sort(arrayList);
        dir.addAll(arrayList);
        if (!f.getAbsolutePath().equalsIgnoreCase("/")) {
            dir.add(0, new OptionCD("..", "Parent Directory", f.getParent(), 0));
        }
        return dir;
    }

    public static void showChangediscDialog(final Context cont, final libepsxe e, String path, final IsoFileSelected mIsoName) {
        File currentCDDir = new File(path);
        final List<OptionCD> cdlist = fillCD(cont, currentCDDir);
        final cdArrayAdapter cdadapter = new cdArrayAdapter(cont, R.layout.file_view, cdlist);
        Log.e("changedisc", MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + path);
        ListView mListView = new ListView(cont);
        mListView.setAdapter((ListAdapter) cdadapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setView(mListView);
        final AlertDialog mAlert = builder.create();
        mAlert.show();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.dialog.ChangediscDialog.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                OptionCD o = cdadapter.getItem(position);
                if (o.getData().equalsIgnoreCase("folder") || o.getData().equalsIgnoreCase("parent directory")) {
                    File currentDir = new File(o.getPath());
                    Log.e("cd", "changepath " + o.getPath());
                    cdlist.clear();
                    cdlist.addAll(ChangediscDialog.fillCD(cont, currentDir));
                    cdadapter.notifyDataSetChanged();
                    return;
                }
                if (!o.getPath().equalsIgnoreCase("folder")) {
                    Log.e("cd", "state: " + o.getPath());
                    Log.e("cd", "which: " + position);
                    e.changedisc(o.getPath(), o.getSlot());
                    mIsoName.setmIsoName(o.getPath());
                    mIsoName.setmIsoSlot(o.getSlot());
                    DialogUtil.closeDialog(mAlert);
                }
            }
        });
    }
}
