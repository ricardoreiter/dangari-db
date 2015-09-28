package database.command;

public interface IBuffer {

	Number getNumber(int field);
	
	Character getCharacter(int field);
	
	String getString(int field);
	
}
