package database.teste;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AppTest.class,
				CompilerTest.class, 
				DatabaseStorageTest.class, 
				DefinitionTest.class,
				FileManagerTest.class,
				IntegrationTest.class, 
				JoinUtilsTest.class })
public class AllTests {

}
