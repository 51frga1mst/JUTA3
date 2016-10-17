import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestDivision {
	
	private static SourceFileReader reader = new SourceFileReader();
	private static String sourcePath = null;

	// to be injected by the Test Runner
	public static void setSourcePath(String sourcePath) {
		TestDivision.sourcePath = sourcePath;
	}
	
	// to be called by the Test Runner
	public static String getGroup() {
		return "A";
	}
	
	// to be called by the Test Runner
	public static String getNumber() {
		return "2";
	}
	
	private boolean sourceAvailable;
	private int recursion, exception, main;
	
	@Before
	public void analyzeSource(){
		String sourceFilename = reader.filename(sourcePath, Division.class);
		sourceAvailable = reader.exists(sourceFilename);
		int[] occurences = reader.contains(sourceFilename, "div(", "Exception", "main");
		recursion = occurences[0];
		exception = occurences[1];
		main = occurences[2];
	}

	
	//	public static int div(int a, int b){
//		if(a < 0 || b <= 0)
//			throw new RuntimeException("a muss grР РЋРІР‚В Р В Р вЂЎer gleich und b  grР РЋРІР‚В Р В Р вЂЎer 0 sein");
//		
//		if(a < b)
//			return 0;
//		else
//			return 1 + div(a - b, b);
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
		if(b == 0)
			Division.div(a, b);
		else
			assertEquals(a / b, Division.div(a, b));
		Output.getOutput();
	}

	@Test(timeout=1000, expected=Exception.class) public void testDiv00() { test(0, 0); }
	@Test(timeout=1000) public void testDiv01() { test(0, 1); }
	@Test(timeout=1000, expected=Exception.class) public void testDiv10() { test(1, 0); }
	@Test(timeout=1000) public void testDiv11() { test(1, 1); }
	
	@Test(timeout=1000, expected=Exception.class) public void testDiv20() { test(2, 0); }
	@Test(timeout=1000) public void testDiv21() { test(2, 1); }
	@Test(timeout=1000) public void testDiv02() { test(0, 2); }
	@Test(timeout=1000) public void testDiv12() { test(1, 2); }
	@Test(timeout=1000) public void testDiv22() { test(2, 2); }
	
	@Test(timeout=1000, expected=Exception.class) public void testDiv_10() { test(-1, 0); }
	@Test(timeout=1000, expected=Exception.class) public void testDiv_20() { test(-2, 0); }
	@Test(timeout=1000, expected=Exception.class) public void testDiv_11() { test(-1, 1); }
	@Test(timeout=1000) public void testDiv10020() { test(100, 20); }
	@Test(timeout=1000) public void testDiv246() { test(24, 6); }
	@Test(timeout=1000) public void testDiv426() { test(42, 6); }
	
	@Test(timeout=1000) public void testDiv34() { test(3, 4); }
	@Test(timeout=1000) public void testDiv43() { test(4, 3); }
	@Test(timeout=1000, expected=Exception.class) public void testDiv_34() { test(-3, 4); }
	@Test(timeout=1000, expected=Exception.class) public void testDiv_4_3() { test(-4, 3); }
	
	@Test(timeout=1000) public void testDiv1216() { test(12, 16); }
	@Test(timeout=1000) public void testDiv2050() { test(20, 50); }
	@Test(timeout=1000) public void testDiv5100() { test(5, 100); }
	@Test(timeout=1000) public void testDiv1317() { test(13, 17); }
	
	@Test(timeout=1000, expected=Exception.class) public void testDiv0_1() { test(0, -1); }
	@Test(timeout=1000, expected=Exception.class) public void testDiv0_10() { test(0, -10); }
	@Test(timeout=1000, expected=Exception.class) public void testDiv1_1() { test(1, -1); }
	@Test(timeout=1000, expected=Exception.class) public void testDiv5_10() { test(5, -10); }
	@Test(timeout=1000, expected=Exception.class) public void testDiv_1_1() { test(-1, -1); }

}
