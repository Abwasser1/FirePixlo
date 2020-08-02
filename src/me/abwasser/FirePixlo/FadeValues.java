package me.abwasser.FirePixlo;

public enum FadeValues {

	BLOODY_NIGHT(1, 700000000, FadeValues.MIDNIGHT),
	BLACK_WHITE(1, 700000000, FadeValues.NOON);
	
	public static final int SUNRISE = 0;
	public static final int NOON = 6000;
	public static final int SUNSET = 12000;
	public static final int MIDNIGHT = 18000;
	
	public long value;
	public long time;
	public int timeForEffect;

	private FadeValues(long value, long time, int timeForEffect) {
		this.value = value;
		this.time = time;
		this.timeForEffect = timeForEffect;
	}
}
