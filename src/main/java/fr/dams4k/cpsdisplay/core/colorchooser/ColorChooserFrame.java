package fr.dams4k.cpsdisplay.core.colorchooser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;

import fr.dams4k.cpsdisplay.core.colorchooser.panels.ImagePanel;
import fr.dams4k.cpsdisplay.core.colorchooser.panels.selectors.HSelectorPanel;
import fr.dams4k.cpsdisplay.core.colorchooser.panels.selectors.SBSelectorPanel;
import fr.dams4k.cpsdisplay.core.colorchooser.panels.slides.SliderPanel;

public class ColorChooserFrame extends JFrame {
    private Dimension baseDimension = new Dimension(480, 480);
    private ImagePanel backgroundImagePanel = new ImagePanel("assets/minecraft/textures/gui/options_background.png", true, 4, 0.75f);
    private SBSelectorPanel SBColor = new SBSelectorPanel();
    private HSelectorPanel HColor = new HSelectorPanel();

    private SliderPanel redSlider = new SliderPanel();

    public ColorChooserFrame() {
        HColor.addListener(SBColor);

        backgroundImagePanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1f;

        c.insets = new Insets(25, 25, 25, 25);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.85;
        c.insets.right = 5;

        backgroundImagePanel.add(SBColor, c);

        c.weightx = 0.15;
        c.insets.right = 25;
        c.insets.left = 5;
        backgroundImagePanel.add(HColor, c);

        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 0.2;
        c.insets = new Insets(25, 25, 25, 25);

        backgroundImagePanel.add(redSlider, c);

        add(backgroundImagePanel);

        setSize(baseDimension);
        setMinimumSize(baseDimension);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Color Chooser");
        setVisible(true);
    }
}
