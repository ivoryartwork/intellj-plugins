package com.ivoryartwork.plugin.folivora;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by Yaochao on 2016/7/25.
 */
public class MainAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        VirtualFile currentVF = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        MainDialog dialog = new MainDialog(currentVF);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setSize(600, 400);
        dialog.setVisible(true);
    }
}
