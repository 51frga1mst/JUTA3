import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestForAusgabe {
	
	private static SourceFileReader reader = new SourceFileReader();
	private static String sourcePath = null;

	// to be injected by the Test Runner
	public static void setSourcePath(String sourcePath) {
		TestForAusgabe.sourcePath = sourcePath;
	}
	
	// to be called by the Test Runner
	public static String getGroup() {
		return "A";
	}
	
	// to be called by the Test Runner
	public static String getNumber() {
		return "1";
	}
	
	private boolean sourceAvailable;
	private int keyword;
	
	@Before
	public void analyzeSource(){
		String sourceFilename = reader.filename(sourcePath, ForAusgabe.class);
		sourceAvailable = reader.exists(sourceFilename);
		int[] occurences = reader.contains(sourceFilename, "for");
		keyword = occurences[0];
	}


	private enum Coverage { LENGTH, CONTENT }
	
	private static final String NEGATIVE_OUTPUT = "*";
	private static final String POSITIVE_OUTPUT = "#";

	private void targetCall() {
		ForAusgabe.main(null);
	}


	private void testForFor() {
		if(!sourceAvailable)
			return; // no source check possible
		if(keyword == 0)
			fail("<for> expected");
	}


	private void testMain(Coverage coverage, int n) {
		this.testForFor(); // all tests will fail without "for"!
		
		Input.redirect(Integer.toString(n));
		Output.redirect();
		
		targetCall();
		
		String[] strings = Output.getOutput();

		String expected;
		if(n > 0)
			 expected = POSITIVE_OUTPUT;
		else
			expected = NEGATIVE_OUTPUT;
		
		if(n < 0) n = -n;
		assertEquals(strings.length, n+1);
		if(coverage == Coverage.LENGTH)
			return;
		
		for(int i = 1; i < strings.length; i++){
			String actual = strings[i];
			if(!expected.equals(actual))
				fail("<" + expected + "> expected, but was: <" + actual +">");
		}
		
	}


	@Test(timeout = 1000)
	public void testMainLength2() {
		testMain(Coverage.LENGTH, 2);
	}

	@Test(timeout = 1000)
	public void testMainContent2() {
		testMain(Coverage.CONTENT, 2);
	}

	@Test(timeout = 1000)
	public void testMainLength0() {
		testMain(Coverage.LENGTH, 0);
	}

	@Test(timeout = 1000)
	public void testMainContent0() {
		testMain(Coverage.CONTENT, 0);
	}

	@Test(timeout = 1000)
	public void testMainLength_1() {
		testMain(Coverage.LENGTH, -1);
	}

	@Test(timeout = 1000)
	public void testMainContent_1() {
		testMain(Coverage.CONTENT, -1);
	}
	
	
	@Test(timeout = 1000)
	public void testMainLength1() {
		testMain(Coverage.LENGTH, 1);
	}

	@Test(timeout = 1000)
	public void testMainContent1() {
		testMain(Coverage.CONTENT, 1);
	}
	
	
	@Test(timeout = 1000)
	public void testMainLengthNegative() {
		for(int i = -1000; i < 0; i++)
		testMain(Coverage.LENGTH, i);
	}

	@Test(timeout = 1000)
	public void testMainLengthPositive() {
		for(int i = 1; i <= 1000; i++)
		testMain(Coverage.LENGTH, i);
	}

	@Test(timeout = 1000)
	public void testMainContentNegative() {
		for(int i = -1000; i < 0; i++)
		testMain(Coverage.CONTENT, i);
	}

	@Test(timeout = 1000)
	public void testMainContentPositive() {
		for(int i = 1; i <= 1000; i++)
		testMain(Coverage.CONTENT, i);
	}

}
