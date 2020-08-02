package me.abwasser.FirePixlo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatHelper {
	public String msg;
	public HashMap<Character, String> map;

	public ChatHelper(String msg) {
		this.msg = msg;
		map = new HashMap<>();
	}

	public void addColorCode(char code, String hex) {
		map.put(code, hex);
	}

	public BaseComponent[] convert() {
		if (map.isEmpty()) {
			return new BaseComponent[] { new TextComponent(msg) };
		}
		List<BaseComponent> ret = new ArrayList<BaseComponent>();
		String[] list = msg.split(Pattern.quote("$"));
		for (String str : list) {
			if (str.length() == 0) {
				ret.add(new TextComponent(str));
				continue;
			}
			char code = str.toCharArray()[0];
			TextComponent comp = new TextComponent(str.substring(1));
			comp.setColor(net.md_5.bungee.api.ChatColor.of(map.get(code)));
			ret.add(comp);
		}
		return ret.toArray(new BaseComponent[ret.size()]);
	}

}
