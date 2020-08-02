package me.abwasser.FirePixlo.minigames;

public enum MinigameLocs {
	_1("minigames_pos_1", 1), 
	_2("minigames_pos_2", 2), 
	_3("minigames_pos_3", 3), 
	_4("minigames_pos_4", 4), 
	_5("minigames_pos_5", 5), 
	_6("minigames_pos_6", 6);
	
	String str;
	int pos;
	
	private MinigameLocs(String str, int pos) {
		this.str = str;
		this.pos = pos;
	}
}
