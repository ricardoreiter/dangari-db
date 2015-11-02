package database.utils;

public class Pair<F, S> {

	private final F fst;
	private final S snd;

	public Pair(F fst, S snd) {
		this.fst = fst;
		this.snd = snd;
	}

	public F getFst() {
		return fst;
	}

	public S getSnd() {
		return snd;
	}

}
