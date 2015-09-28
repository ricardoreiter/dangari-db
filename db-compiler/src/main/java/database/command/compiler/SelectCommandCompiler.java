package database.command.compiler;

import java.util.LinkedList;
import java.util.List;

import database.command.ICommandExecutor;
import database.command.SelectCommandExecutor;
import database.gals.Token;

public class SelectCommandCompiler implements ICommandCompiler {

	private List<String> selectedFields = new LinkedList<String>(); // Campos selecionados (ex: codigoArea ou tabela.codigoArea)
	private List<String> selectedTables = new LinkedList<String>(); // Tabelas selecionadas
	
	@Override
	public void accept(int action, Token token) {
		System.out.println("SELECT_COMMAND_COMPILER - Ação #" + action + ", Token: " + token);
		switch (action) {
			case 40:
				break;
			case 41:
				break;
			case 42:
				break;
			case 43:
				break;
			case 44:
				break;
			case 45:
				break;
			case 46:
				break;
			case 47:
				break;
			case 48:
				break;
			case 49:
				break;
			case 50:
				break;
		}
	}

	@Override
	public ICommandExecutor compile() {
		for (String field: selectedFields) {
			// verifica se campos existem
		}
		
		for (String table: selectedTables) {
			// verifica se tabelas existem
		}
		
		return new SelectCommandExecutor();
	}

}
