package me.abwasser.FirePixlo;

import java.io.File;
import java.util.HashMap;

import it.unimi.dsi.fastutil.Hash;

public class PluginRule {

	public static HashMap<Rules, String> hashMap = new HashMap<>();

	public static void write(Rules rule, String value) {
		YamlHelper helper = new YamlHelper(new File("FirePixlo/pluginRule.yml"));
		helper.write("Rule." + rule.name(), value);
		hashMap.put(rule, value);
	}

	public static String read(Rules rule) {
		if (hashMap.containsKey(rule)) {
			return hashMap.get(rule);
		}
		YamlHelper helper = new YamlHelper(new File("FirePixlo/pluginRule.yml"));
		String str = (helper.readString("Rule." + rule.name()) == null) ? rule.defauld
				: helper.readString("Rule." + rule.name());
		hashMap.put(rule, str);
		return str;
	}

	public static void write(Rules rule, Boolean value) {
		write(rule, (value ? "true" : "false"));
	}

	public static Boolean readBoolean(Rules rule) {
		return Boolean.parseBoolean(read(rule));
	}

	public static enum Rules {
		DO_PERFMON("false", "true", "false"),
		SMOOTHER_RELOAD("false", "true", "false"),
		REDUCE_DEBUG_INFO("true", "true", "false"),
		VERBOSE_DEBUG_OUTPUT("true", "true", "false"),
		BUILD_MODE("false", "true", "false");

		public String defauld;
		public String[] options;

		Rules(String defauld, String... options) {
			this.defauld = defauld;
			this.options = options;
		}
	}
}
