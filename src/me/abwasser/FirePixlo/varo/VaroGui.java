package me.abwasser.FirePixlo.varo;

import me.abwasser.FirePixlo.gui.GUI;
import me.abwasser.FirePixlo.gui.Token;

public class VaroGui {

	public VaroGui() {
		// TODO Auto-generated constructor stub
	}

	public GUI getGUI(String id) {
		GUI gui = new GUI(3, "§4Varo§7-§3Settings");
		
		gui.backgroundFill();
		return gui;
	}

	public GUI getGUI() {
		return getGUI(Token.generateToken(100));
	}
}
