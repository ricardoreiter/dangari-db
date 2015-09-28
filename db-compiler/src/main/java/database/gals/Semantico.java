package database.gals;

import database.command.ICommandExecutor;
import database.command.compiler.ICommandCompiler;
import database.command.compiler.SelectCommandCompiler;

public class Semantico implements Constants {

	private ICommandExecutor executor;
	private ICommandCompiler compiler;
	
	public ICommandExecutor getExecutor() {
		return executor;
	}
	
	public void executeAction(int action, Token token) throws SemanticError {
		System.out.println("Ação #" + action + ", Token: " + token);
		switch(action) {
			case 70: // Fim de reconhecimento do comando, irá gerar o executor
				executor = compiler.compile();
				break;
			case 40: // Início de reconhecimento do select
				compiler = new SelectCommandCompiler();
			default:
				if (compiler == null) {
					throw new SemanticError("Erro, compilador interno ainda não foi iniciado...");
				}
				compiler.accept(action, token);
		}
	}
}
