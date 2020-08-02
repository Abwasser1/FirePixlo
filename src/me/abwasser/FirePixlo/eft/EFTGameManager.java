package me.abwasser.FirePixlo.eft;

public class EFTGameManager {

	public Status status;

	public EFTGameManager() {
		status = Status.WAIT;
	}

	public void start() {
		status = Status.IN_GAME;
	}

	public enum Status {
		WAIT, IN_GAME;
	}

	public static String[] waitSlides = { "eft_wait_2", "eft_wait_3", "eft_wait_5", "eft_wait_6", "eft_wait_7",
			"eft_wait_8", "eft_wait_9", "eft_wait_10", "eft_wait_11", "eft_wait_12","eft_wait_13"};
}
