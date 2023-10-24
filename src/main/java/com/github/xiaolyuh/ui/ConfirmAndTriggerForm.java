package com.github.xiaolyuh.ui;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaolyuh.utils.ConfigUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConfirmAndTriggerForm extends DialogWrapper {
    private static int lastSelectIndex = 0;
    private JPanel panel1;
    @SuppressWarnings("rawtypes")
    private JList jlist;
    private JLabel jlabel;

    public ConfirmAndTriggerForm(String txt, Project project) {
        super(project);
        init();
        jlabel.setText(txt);

        JSONObject jsonObject = ConfigUtil.getProjectConfigToFile(project);
        if (jsonObject != null) {
            JSONArray services = jsonObject.getJSONArray("services");
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Object service : services) {
                listModel.addElement((String) service);
            }
            //noinspection unchecked
            jlist.setModel(listModel);
            jlist.setSelectedIndex(lastSelectIndex);
        } else {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            listModel.addElement("缺少服务配置,无法触发流水线!请先在git-flow-project.json文件配置项目服务");
            jlist.setEnabled(false);
        }

    }

    public String getSelectService() {
        if (jlist.isEnabled()) {
            lastSelectIndex = jlist.getSelectedIndex();
            return (String) jlist.getSelectedValue();
        }
        return "";
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return panel1;
    }
}
