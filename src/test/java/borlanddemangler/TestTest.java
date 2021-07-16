package borlanddemangler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import generic.test.AbstractGenericTest;
import ghidra.framework.Application;
import ghidra.program.database.ProgramDB;
import ghidra.test.ToyProgramBuilder;

class TestTest  extends AbstractGenericTest {

	private ProgramDB program;
	@BeforeEach
	public void setUp() throws Exception {
		ToyProgramBuilder builder = new ToyProgramBuilder("test", true);
		builder.createMemory(".text", "0x01001000", 0x100);
		program = builder.getProgram();
	}

	@Test
	/**
	 * Test all the demangler input
	 */
	public void testDemangler() {
		ClassLoader classLoader = getClass().getClassLoader();
		
		//File file = new File().getFile());
		//String absolutePath = file.getAbsolutePath();
		System.out.println(classLoader.getResource("list.txt").toString());
	}
}
