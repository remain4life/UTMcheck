package utmcheck;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;

public class ColorPane extends JTextPane {

    //simplify appending regular text
    public void append(String s) {
        append(Color.BLACK,s,false);
    }

    public void append(Color c, String s) {
        append(c,s,false);
    }

    public void append(Color c, String s, Boolean bold) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet as = sc.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.Foreground, c);
        AttributeSet asBold = sc.addAttribute(as, StyleConstants.Bold, true);

        int len = getDocument().getLength();

        setCaretPosition(len);
        if (bold) {
            setCharacterAttributes(asBold, true);
        } else {
            setCharacterAttributes(as, true);
        }
        replaceSelection(s);
    }

}
