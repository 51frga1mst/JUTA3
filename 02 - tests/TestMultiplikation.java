import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestMultiplikation {
	
	private static SourceFileReader reader = new SourceFileReader();
	private static String sourcePath = null;

	// to be injected by the Test Runner
	public static void setSourcePath(String sourcePath) {
		TestMultiplikation.sourcePath = sourcePath;
	}
	
	// to be called by the Test Runner
	public static String getGroup() {
		return "B";
	}
	
	// to be called by the Test Runner
	public static String getNumber() {
		return "2";
	}
	
	private boolean sourceAvailable;
	private int recursion, exception, main;
	
	@Before
	public void analyzeSource(){
		String sourceFilename = reader.filename(sourcePath, Multiplikation.class);
		sourceAvailable = reader.exists(sourceFilename);
		int[] occurences = reader.contains(sourceFilename, "mult(", "Exception", "main");
		recursion = occurences[0];
		exception = occurences[1];
		main = occurences[2];
	}
	
	//	public static int mult(int a, int b){
//		if(b < 0)
//			throw new RuntimeException("b muss grР РЋРІР‚В Р В Р вЂЎer 0 sein");
//		
//		if(b == 0)
//			return 0;
//		else
//			return a + mult(a, b - 1);
//	}

	@Test
	public void testForRecursion() {
		if(!sourceAvailable)
			return; // no source check possible
		if(recursion <= (1 + main))
			fail("recursion expected");
	}

	@Test
	public void testForException() {
		if(!sourceAvailable)
			return; // no source check possible
		if(exception == 0)
			fail("exception expected");
	}

	private void test(int a, int b){
		this.testForRecursion(); // all tests will fail without recursion!
		Output.redirect();
		assertEquals(a * b, Multiplikation.mult(a, b));
		Output.getOutput();
	}

	@Test(timeout=1000) public void testMult00() { test(0, 0); }
	@Test(timeout=1000) public void testMult01() { test(0, 1); }
	@Test(timeout=1000) public void testMult10() { test(1, 0); }
	@Test(timeout=1000) public void testMult11() { test(1, 1); }
	
	@Test(timeout=1000) public void testMult20() { test(2, 0); }
	@Test(timeout=1000) public void testMult21() { test(2, 1); }
	@Test(timeout=1000) public void testMult02() { test(0, 2); }
	@Test(timeout=1000) public void testMult12() { test(1, 2); }
	@Test(timeout=1000) public void testMult22() { test(2, 2); }
	
	@Test(timeout=1000) public void testMult_10() { test(-1, 0); }
	@Test(timeout=1000) public void testMult_20() { test(-2, 0); }
	@Test(timeout=1000) public void testMult_11() { test(-1, 1); }
	@Test(timeout=1000) public void testMult_21() { test(-2, 1); }
	@Test(timeout=1000) public void testMult_12() { test(-1, 2); }
	@Test(timeout=1000) public void testMult_22() { test(-2, 2); }
	
	@Test(timeout=1000) public void testMult34() { test(3, 4); }
	@Test(timeout=1000) public void testMult43() { test(4, 3); }
	@Test(timeout=1000) public void testMult_34() { test(-3, 4); }
	@Test(timeout=1000) public void testMult_4_3() { test(-4, 3); }
	
	@Test(timeout=1000) public void testMult1216() { test(12, 16); }
	@Test(timeout=1000) public void testMult2050() { test(20, 50); }
	@Test(timeout=1000) public void testMult5100() { test(5, 100); }
	@Test(timeout=1000) public void testMult1317() { test(13, 17); }
	
	@Test(timeout=1000, expected=Exception.class) public void testMult0_1() { test(0, -1); }
	@Test(timeout=1000, expected=Exception.class) public void testMult0_10() { test(0, -10); }
	@Test(timeout=1000, expected=Exception.class) public void testMult1_1() { test(1, -1); }
	@Test(timeout=1000, expected=Exception.class) public void testMult5_10() { test(5, -10); }
	@Test(timeout=1000, expected=Exception.class) public void testMult_1_1() { test(-1, -1); }

}
