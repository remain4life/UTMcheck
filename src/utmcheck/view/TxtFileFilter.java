package utmcheck.view;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class TxtFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
    }

    @Override
    public String getDescription() {
        return "Текстовые файлы со списком магазинов (*.txt)";
    }
}
