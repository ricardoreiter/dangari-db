package database.gals;

import java.util.LinkedList;
import java.util.List;

import database.command.ICommandExecutor;
import database.command.compiler.AlterDatabaseCommandCompiler;
import database.command.compiler.CreateDatabaseCommandCompiler;
import database.command.compiler.CreateIndexCommandCompiler;
import database.command.compiler.CreateTableCommandCompiler;
import database.command.compiler.DescribeTableCommandCompiler;
import database.command.compiler.DropTableCommandCompiler;
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
            case 70:
                break;
            case 71: // Fim de reconhecimento do comando, irá gerar o executor
                executors.add(compiler.compile());
                compiler = null;
                break;
            case 20:
                compiler = new CreateDatabaseCommandCompiler();
                compiler.accept(action, token);
                break;
            case 21:
                compiler = new CreateTableCommandCompiler();
                compiler.accept(action, token);
                break;
            case 22:
            	compiler = new CreateIndexCommandCompiler();
            	compiler.accept(action, token);
            case 30:
                if (compiler == null) {
                    compiler = new InsertCommandCompiler();
                }
                compiler.accept(action, token);
                break;
            case 40: // Início de reconhecimento do select
                if (compiler == null) {
                    compiler = new SelectCommandCompiler();
                }
                compiler.accept(action, token);
                break;
            case 60:
                compiler = new DropTableCommandCompiler();
                compiler.accept(action, token);
                break;
            case 62:
                compiler = new DescribeTableCommandCompiler();
                compiler.accept(action, token);
                break;
            case 63:
                compiler = new AlterDatabaseCommandCompiler();
                compiler.accept(action, token);
            default:
                if (compiler == null) {
                    throw new SemanticError("Erro, compilador interno ainda não foi iniciado...");
                }
                compiler.accept(action, token);
        }
    }
}
