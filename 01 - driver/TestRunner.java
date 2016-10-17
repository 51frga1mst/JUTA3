import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestRunner {
	
	public enum State{
		COMPILED, UNCOMPILABLE, UNSOLICITED
	}

	public static final String TEST_PREFIX		= "Test";
	public static final String SELECTED			= "Selected:";
	public static final String TESTING			= "Testing:";
	
	public static final String SOURCE_SETTER	= "setSourcePath";
	public static final String GROUP_GETTER		= "getGroup";
	public static final String NUMBER_GETTER	= "getNumber";
	
	private String registrationFile, moodleName, moodleId, unitUnderTest, sourcePath = null;
	private State state;
	private Class<?> testClass = null;
		
	public static void printHeader(){
		System.out.println("Aufgabe;Moodle-Name;Moodle-Id;Nachname;Vorname;Mat.-Nr.;Gruppe;Nr;Status;Insgesamt;Erfolge;Fehler;Ergebnis");
	}
	
	private TestRunner(String args[]) throws IOException, ClassNotFoundException{
		if(!(args.length == 5 || args.length == 6))
		   	throw new IOException("Wrong Number of Parameters: expected [registrationFile, moodleName, moodleId, unitUnderTest, state, optional: sourcePath], got " + Arrays.toString(args));
		
		this.registrationFile = args[0];
		this.moodleName = args[1];
		this.moodleId = args[2];
		this.unitUnderTest = args[3];
		String state = args[4];
		this.state = State.valueOf(state.toUpperCase());
		if(args.length == 6)
			this.sourcePath = args[5];
		if(this.state != State.UNSOLICITED)
			testClass = Class.forName(TEST_PREFIX + this.unitUnderTest);
	}
	
	private String getFromTestClass(String getter){
		if(testClass == null)
			return "-";		
		try {
			Method m = testClass.getMethod(getter);
			Object o = m.invoke(null);
			if(o != null)
				return o.toString();
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			e.printStackTrace();
		}
		return "-";
	}
	
	private void setToTestClass(String setter, String arg){
		if(testClass == null || arg == null)
			return;		
		try {
			Method m = testClass.getMethod(setter, String.class);
			m.invoke(null, arg);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			e.printStackTrace();
		}		
	}
	
	private String getGroup(){
		return this.getFromTestClass(GROUP_GETTER);
	}
	
	private String getNumber(){
		return this.getFromTestClass(NUMBER_GETTER);
	}
	
	private void setSourcePath() {
		this.setToTestClass(SOURCE_SETTER, sourcePath);
	}
	

	private void test() {
		this.setSourcePath();
		ListReader reader = new ListReader(registrationFile);
		ListReader.Student student = reader.getStudent(moodleName);
		
		//Aufgabe;Moodle-Name;Moodle-Id;Nachname;Vorname;Mat.-Nr.;Gruppe;Nr;Status;Insgesamt;Erfolge;Fehler;Ergebnis
		System.out.print(unitUnderTest + ";" + moodleName + ";" + moodleId + ";");
		
		if(student == null)
			System.out.print("-;-;-;");
		else
			System.out.format("%s;%s;%s;", student.lastname, student.firstname, student.matnr);
		
		System.out.format("%s;%s;", this.getGroup(), this.getNumber());
		
		if(state == State.UNCOMPILABLE){
			System.out.println("uncompilable;-;-;-;0");
			return;
		}else if(state == State.UNSOLICITED){
			System.out.println("unsolicited;-;-;-;-");
			return;
		}
		
		System.out.print("compiled;");

		// Save as output may be redirected by the test cases
		PrintStream systemOut = System.out;

		Result result = JUnitCore.runClasses(testClass);
		int overall = result.getRunCount();
		int failures = result.getFailureCount();
		int success = overall - failures;
		double percentage = (double)success / overall;
		
		int scaled = (int)((percentage  + 0.005)* 100.0); // scale it
		double rounded = ((double)scaled)/100.0;
		
		systemOut.format(Locale.getDefault(),"%d;%d;%d;%f", overall, success, failures, rounded);
		System.exit(0);
	}

	public static void main(String[] args){
		try {
			TestRunner runner = new TestRunner(args);
			runner.test();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
