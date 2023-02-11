package fr.dams4k.cpsdisplay.core.colorpicker.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;

import fr.dams4k.cpsdisplay.core.colorpicker.gui.border.Border;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.border.TextFieldBorder;
import fr.dams4k.cpsdisplay.v1_8.config.ModConfig;

public class TextField extends JTextField implements MouseListener {
    private Border imageBorder;
    private Label label = new Label("");

    private int startX = 0;

    public TextField(float textureScale) {
        setOpaque(false);
        setBorder(null);

        this.imageBorder = new TextFieldBorder(textureScale);
        this.startX = (int) (textureScale*2);

        addMouseListener(this);
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                repaint();
            }
            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyTyped(KeyEvent e) {}
        });
    }

    protected void paintCaret(Graphics g) {
        g.setColor(ModConfig.HexToColor("D0D0D0", 6));
        int x = startX + label.getStringWidth(getText().substring(0, getCaretPosition()));
        int caretHeight = this.label.getFontHeight();
        g.fillRect(x-(int) (label.fontSize/2), getHeight()/2-(caretHeight+8)/2, (int) label.fontSize, caretHeight+8);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.imageBorder.paintBorder(g, this, true);
        int y = this.getHeight()/2 - label.getFontHeight()/2;
        label.paintStringWithShadow(g, this.getText(), startX, y);
        this.paintCaret(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        float txtWidth = startX + this.label.getStringWidth(getText());
        setCaretPosition((int) (x/txtWidth*getText().length()));
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}
