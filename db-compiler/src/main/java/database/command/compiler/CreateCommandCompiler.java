package database.command.compiler;

import database.command.CreateCommandExecutor;
import database.command.ICommandExecutor;
import database.gals.Token;

public class CreateCommandCompiler implements ICommandCompiler {

	@Override
	public void accept(int action, Token token) {
		// TODO Auto-generated method stub

	}

	@Override
	public ICommandExecutor compile() {
		// TODO Auto-generated method stub
		return new CreateCommandExecutor();
	}

}
