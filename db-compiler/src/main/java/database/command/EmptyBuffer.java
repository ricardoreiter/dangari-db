package database.command;

/**
 * Esse buffer é o retornado de qualquer comando que não seja um select
 * @author Ricardo Reiter
 *
 */
public class EmptyBuffer implements IBuffer {

	@Override
	public Number getNumber(int field) {
		return null;
	}

	@Override
	public Character getCharacter(int field) {
		return null;
	}

	@Override
	public String getString(int field) {
		return null;
	}


}
