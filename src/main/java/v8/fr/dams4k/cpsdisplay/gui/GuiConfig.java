package fr.dams4k.cpsdisplay.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.dams4k.cpsdisplay.config.ModConfig;
import fr.dams4k.cpsdisplay.enums.MouseModeEnum;
import fr.dams4k.cpsdisplay.gui.buttons.ModColorButton;
import fr.dams4k.cpsdisplay.gui.buttons.ModSlider;
import fr.dams4k.cpsdisplay.gui.buttons.ModSliderMainPoint;
import fr.dams4k.cpsdisplay.gui.buttons.ModTextField;
import fr.dams4k.cpsdisplay.gui.buttons.ModToggleButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;


public class GuiConfig extends ModScreen {
	public enum GuiButtons {
		SHOW_TEXT(0),
		SCALE_TEXT(1),
		COLOR_TEXT(2),
        SHADOW_TEXT(3),
		MODE_TEXT(4),
		TEXT(5),

		COLOR_BACKGROUND(10),
		MARGIN_BACKGROUND_LABEL(11),
		MARGIN_BACKGROUND_FIELD(11),

		SHOW_RAINBOW(20),
		SPEED_RAINBOW(21);

		public final int id;

		GuiButtons(int id) {
			this.id = id;
		}

		public int getY(int y) {
			return y + (this.id % 10) * 25;
		}
	}

	// Text
	private ModToggleButton showTextToggle;
	private ModSlider scaleTextSlider;
	private GuiButton modeTextButton;
    private ModToggleButton showTextShadowToggle;
	private GuiTextField textField;
	private ModColorButton colorTextButton;

	private MouseModeEnum mouseModeSelected;

	// Background
	private ModColorButton colorBackgroundButton;
	private GuiLabel marginBackgroundLabel;
	private ModTextField marginBackgroundField;

	// Rainbow
	private ModToggleButton showRainbowToggle;
	private ModSlider speedRainbowSlider;
	
	private int diffX = 0;
	private int diffY = 0;

	@Override
	public void initGui() {
		super.initGui();
		MinecraftForge.EVENT_BUS.register(this);

		this.addTextButtons(width / 2 - 152, 10 + top);
		this.addBackgroundButtons(width / 2 + 2, 10 + top);
		this.addRainbowButtons(width / 2 + 2, GuiButtons.MARGIN_BACKGROUND_FIELD.getY(10 + top) + 25);

        List<Integer> backgroundPositions = GuiOverlay.getBackgroundPositions(0, 0, true);
        int x = backgroundPositions.get(2) > width-100 - 10 ? 0 : width-100;
        int Y = backgroundPositions.get(3) > height-20 - 10 ? 0 : height-20;
        this.buttonList.add(new GuiButton(-1, x, Y, 100, 20, "Updater Config"));

		updateButtons();
	}

	public void addTextButtons(int x, int y) {
		mouseModeSelected = MouseModeEnum.getByText(ModConfig.text);

		showTextToggle = new ModToggleButton(
			GuiButtons.SHOW_TEXT.id, x, GuiButtons.SHOW_TEXT.getY(y), 150, 20,
			I18n.format("cpsdisplay.button.show_text", new Object[0]), "", ModConfig.showText
		);

		colorTextButton = new ModColorButton(
			GuiButtons.COLOR_TEXT.id, x, GuiButtons.COLOR_TEXT.getY(y), 150, 20,
			I18n.format("cpsdisplay.button.color_text", new Object[0]), false
		);
		colorTextButton.setColor(ModConfig.getTextColor());
		
		scaleTextSlider = new ModSlider(
			GuiButtons.SCALE_TEXT.id, x, GuiButtons.SCALE_TEXT.getY(y), 150, 20,
			I18n.format("cpsdisplay.slider.scale_text", new Object[0]),
			0.1f * 100, 4 * 100, 0.01f, (float) (ModConfig.scaleText * 100), 10
		);

		for (int i = 50; i < 400; i+=50) {
			scaleTextSlider.addMainPoint(new ModSliderMainPoint(i, 4f));
		}

        showTextShadowToggle = new ModToggleButton(
            GuiButtons.SHADOW_TEXT.id, x, GuiButtons.SHADOW_TEXT.getY(y), 150, 20,
            "Shadow: ", "", ModConfig.showTextShadow
        );

		modeTextButton = new GuiButton(GuiButtons.MODE_TEXT.id, x, GuiButtons.MODE_TEXT.getY(y), 150, 20, "");
		updateMouseModeButton();

		textField = new GuiTextField(GuiButtons.TEXT.id, fontRendererObj, x, GuiButtons.TEXT.getY(y), 150, 20);
		textField.setMaxStringLength(999);
		textField.setText(ModConfig.text);
		
		buttonList.add(showTextToggle);
		buttonList.add(scaleTextSlider);
        buttonList.add(showTextShadowToggle);
		buttonList.add(modeTextButton);
		buttonList.add(colorTextButton);

		textFieldList.add(textField);
	}

	public void addBackgroundButtons(int x, int y) {
		colorBackgroundButton = new ModColorButton(
			GuiButtons.COLOR_BACKGROUND.id, x, GuiButtons.COLOR_BACKGROUND.getY(y), 150, 20,
			I18n.format("cpsdisplay.button.color_background", new Object[0]), true
		);
		colorBackgroundButton.setColor(ModConfig.getBackgroundColor());

        String labelString = I18n.format("cpsdisplay.button.margin_background", new Object[0]);
        int stringWidth = fontRendererObj.getStringWidth(labelString);
		marginBackgroundLabel = new GuiLabel(fontRendererObj, GuiButtons.MARGIN_BACKGROUND_LABEL.id, x+5, GuiButtons.MARGIN_BACKGROUND_LABEL.getY(y), stringWidth, 20, 0xffffff);
		marginBackgroundLabel.func_175202_a(labelString);

		marginBackgroundField = new ModTextField(GuiButtons.MARGIN_BACKGROUND_FIELD.id, fontRendererObj, x+5 + stringWidth + 10, GuiButtons.MARGIN_BACKGROUND_FIELD.getY(y), 150 - 5 - stringWidth - 10, 20);
		marginBackgroundField.setMaxStringLength(2);
		marginBackgroundField.setText(Integer.toString(ModConfig.marginBackground));
		marginBackgroundField.letters = false;
		marginBackgroundField.punctuation = false;
		marginBackgroundField.anythings = false;
		marginBackgroundField.placeHolder = "§oxx";

		buttonList.add(colorBackgroundButton);
		labelList.add(marginBackgroundLabel);
		textFieldList.add(marginBackgroundField);
	}

	public void addRainbowButtons(int x, int y) {
		showRainbowToggle = new ModToggleButton(
            GuiButtons.SHOW_RAINBOW.id, x, GuiButtons.SHOW_TEXT.getY(y), 150, 20,
            I18n.format("cpsdisplay.button.show_rainbow", new Object[0]), "", ModConfig.showRainbow
        );

		speedRainbowSlider = new ModSlider(
			GuiButtons.SPEED_RAINBOW.id, x, GuiButtons.SPEED_RAINBOW.getY(y), 150, 20,
			I18n.format("cpsdisplay.slider.speed_rainbow", new Object[0]), 0.1f, 3f, 0.1f, 0.5f, 10
		);

		buttonList.add(showRainbowToggle);
		buttonList.add(speedRainbowSlider);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (GuiTextField field : this.textFieldList) {
			field.textboxKeyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (GuiTextField field : this.textFieldList) {
			field.mouseClicked(mouseX, mouseY, mouseButton);
		}
		
		int[] textPosition = ModConfig.getTextPosition();

		diffX = textPosition[0] - mouseX;
		diffY = textPosition[1] - mouseY;
		
		if (GuiOverlay.positionInOverlay(mouseX, mouseY)) {
			mc.displayGuiScreen(new MoveOverlayGui(diffX, diffY));
		} else {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void updateScreen() {
		for (GuiTextField field : this.textFieldList) {
			field.updateCursorCounter();
		}
		super.updateScreen();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground();

		for (GuiTextField field : this.textFieldList) {
			field.drawTextBox();
		}
		super.drawScreen(mouseX, mouseY, partialTicks);

		if (GuiOverlay.positionInOverlay(mouseX, mouseY)) {
			ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(0, 0, true);
			
			new GuiOverlay(Minecraft.getMinecraft(), 0, 0, ModConfig.getBackgroundColor());
			
			drawVerticalLine(positions.get(0), positions.get(1), positions.get(3), Color.RED.getRGB());
			drawVerticalLine(positions.get(2), positions.get(1), positions.get(3), Color.RED.getRGB());
			drawHorizontalLine(positions.get(0), positions.get(2), positions.get(1), Color.RED.getRGB());
			drawHorizontalLine(positions.get(0), positions.get(2), positions.get(3), Color.RED.getRGB());
		} else {
			new GuiOverlay(Minecraft.getMinecraft(), 0, 0);
		}

		updateConfig();
	}
	public void drawBackground() {
		ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(0, 0, true);

		int color = -1072689136;
		int margin = (int) (this.height / 10 * ModConfig.scaleText);
		// TOP
		this.drawGradientRect(0, 0, this.width, positions.get(1)-margin, color, color);
		// BOTTOM
		this.drawGradientRect(0, positions.get(3)+margin, this.width, this.height, color, color);
		// LEFT
		this.drawGradientRect(0, positions.get(1)-margin, positions.get(0)-margin, positions.get(3)+margin, color, color);
		// RIGHT
		this.drawGradientRect(positions.get(2)+margin, positions.get(1)-margin, this.width, positions.get(3)+margin, color, color);
	}
	
	public void updateConfig() {
		if (this.textField != null) ModConfig.text = textField.getText();
		if (this.colorTextButton != null) ModConfig.setTextColor(colorTextButton.getColor());
		if (this.showTextToggle != null) ModConfig.showText = showTextToggle.getValue();
		if (this.scaleTextSlider != null) ModConfig.scaleText = scaleTextSlider.getValue() / 100d;
        if (this.showTextShadowToggle != null) ModConfig.showTextShadow = showTextShadowToggle.getValue();

		if (this.colorBackgroundButton != null) ModConfig.setBackgroundColor(this.colorBackgroundButton.getColor());
		
		if (this.marginBackgroundField != null) {
			String sMargin = marginBackgroundField.getText();
			int margin = sMargin.isEmpty() ? 0 : Integer.valueOf(sMargin);
			ModConfig.marginBackground = margin;
		}

		if (this.showRainbowToggle != null) ModConfig.showRainbow = showRainbowToggle.getValue();
		if (this.speedRainbowSlider != null) ModConfig.speedRainbow = speedRainbowSlider.getValue();

		updateButtons();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		updateConfig();
		ModConfig.syncConfig(false);

		if (button.id == GuiButtons.MODE_TEXT.id) {
			mouseModeSelected = MouseModeEnum.getById(mouseModeSelected.getId() + 1);
			updateMouseModeButton();

			if (mouseModeSelected != MouseModeEnum.CUSTOM) {
				ModConfig.text = I18n.format(mouseModeSelected.getText(), new Object[0]);
			}
			textField.setText(ModConfig.text);
		} else if (button.id == -1) {
            mc.displayGuiScreen(new VersionConfig(this));
        }
	}
	
	public void updateMouseModeButton() {
		if (modeTextButton == null) return;
		modeTextButton.displayString = I18n.format("cpsdisplay.button.display_mode", new Object[0]) + mouseModeSelected.getName();
	}

	public void updateButtons() {
		// Display mode
		if (textField != null) {
			if (mouseModeSelected == MouseModeEnum.CUSTOM) {
				textField.setVisible(true);
			} else {
				textField.setVisible(false);
			}
		}	
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		for (GuiButton button : this.buttonList) {
			if (button instanceof ModColorButton) {
				ModColorButton colorButton = (ModColorButton) button;
				colorButton.killColorPicker();
			}
		}
		ModConfig.syncConfig(false);
	}
}
