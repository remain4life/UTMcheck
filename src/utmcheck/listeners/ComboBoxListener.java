package utmcheck.listeners;

import utmcheck.utils.RegionStringUtil;
import utmcheck.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComboBoxListener implements ActionListener{
    private View view;

    public ComboBoxListener(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox comboBox = (JComboBox) e.getSource();
        String selectedItem = (String) (comboBox).getSelectedItem();
        //setting new region to controller
        view.getController().setRegion(RegionStringUtil.getMainRegionsFromString(selectedItem));
    }
}
