package lessons.race;

public class Clock implements IClock {

	private int hours;
	private int minutes;
	private int seconds;

	public Clock() {
		this(0, 0, 0);
	}

	public Clock(int hours, int minutes, int seconds) {
		assert 0 <= hours && hours < 24;
		assert 0 <= minutes && minutes < 60;
		assert 0 <= seconds && seconds < 60;

		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	public Clock(Clock c) {
		this.hours = c.hours;
		this.minutes = c.minutes;
		this.seconds = c.seconds;
	}

	public void reset() {
		this.hours = 0;
		this.minutes = 0;
		this.seconds = 0;
	}

	public int getHours() {
		return this.hours;
	}

	public int getMinutes() {
		return this.minutes;
	}

	public int getSeconds() {
		return this.seconds;
	}

	public int inSeconds() {
		return this.hours*3600+this.minutes*60+this.seconds;
	}
	
	public void increment(int nbh, int nbm, int nbs) {
		this.incrementSeconds(nbh * 3600 + nbm * 60 + nbs);
	}

	public void incrementSeconds(int nbs) {
		int s = this.seconds + nbs;
		int m = this.minutes + s / 60;
		this.seconds = s % 60;
		this.hours = this.hours + m / 60;
		this.minutes = m % 60;
	}

	public void incrementMinutes(int nbm) {
		int m = this.minutes + nbm;
		this.hours = this.hours + m / 60;
		this.minutes = m % 60;
	}

	public void incrementHours(int nbh) {
		this.hours = this.hours + nbh;
	}

	public IClock differenceBetween(IClock other) {
		Clock res = new Clock();

		int otherTotalSeconds = other.inSeconds();
		int totalSeconds = this.inSeconds();

		int diffSeconds;
		if (totalSeconds > otherTotalSeconds) {
			diffSeconds = totalSeconds - otherTotalSeconds;
		} else {
			diffSeconds = otherTotalSeconds - totalSeconds;
		}

		res.hours = diffSeconds / 3600;
		diffSeconds = diffSeconds % 3600;
		res.minutes = diffSeconds / 60;
		res.seconds = diffSeconds % 60;

		return res;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || this.getClass() != other.getClass())
			return false;

		Clock otherClock = (Clock) other;
		return (this.hours == otherClock.hours 
				&& this.minutes == otherClock.minutes 
				&& this.seconds == otherClock.seconds);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hours;
		result = prime * result + minutes;
		result = prime * result + seconds;
		return result;
	}

	@Override
	public String toString() {
		return this.hours + "h " + this.minutes + "mn " + this.seconds + "s";
	}
}

