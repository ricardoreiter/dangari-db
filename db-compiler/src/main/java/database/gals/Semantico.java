package database.gals;

import java.util.LinkedList;
import java.util.List;

import database.command.ICommandExecutor;
import database.command.compiler.CreateTableCommandCompiler;
import database.command.compiler.ICommandCompiler;
import database.command.compiler.InsertCommandCompiler;
import database.command.compiler.SelectCommandCompiler;

public class Semantico implements Constants {

    private List<ICommandExecutor> executors = new LinkedList<>();
    private ICommandCompiler compiler;

    public List<ICommandExecutor> getExecutor() {
        return executors;
    }

    public void executeAction(int action, Token token) throws SemanticError {
        System.out.println("Ação #" + action + ", Token: " + token);
        switch (action) {
            case 70: // Fim de reconhecimento do comando, irá gerar o executor
                executors.add(compiler.compile());
                break;
            case 20:
                compiler = new CreateTableCommandCompiler();
            case 30:
                compiler = new InsertCommandCompiler();
            case 40: // Início de reconhecimento do select
                if (compiler == null) {
                    compiler = new SelectCommandCompiler();
                }
            default:
                if (compiler == null) {
                    throw new SemanticError("Erro, compilador interno ainda não foi iniciado...");
                }
                compiler.accept(action, token);
        }
    }
}
