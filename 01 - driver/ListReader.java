import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class ListReader {
	
	private enum State{
		BEGIN, HEADER, CONTENT, END
	}
	
	private static final String START_TAG = "startHISsheet";
	private static final String END_TAG = "endHISsheet";
	
	private static final String MTKNR = "mtknr";
	private static final String SORTNAME = "sortname";
	
	public class Student{
		public String firstname, lastname, matnr;

		public Student(String firstname, String lastname, String matnr) {
			this.firstname = firstname;
			this.lastname = lastname;
			this.matnr = matnr;
		}

		@Override
		public String toString() {
			return "Student [firstname=" + firstname + ", lastname=" + lastname
					+ ", matnr=" + matnr + "]";
		}		
	}
	
	private String[] filenames;

	public ListReader(String filenames) {
		super();
		this.filenames = filenames.split(";");
	}

	public Student getStudent(String moodlename){
		for (String filename : filenames) {
			Student student = this.getStudent(filename, moodlename);
			if(student != null)
				return student;			
		}
		return null;
	}
	
	private Student getStudent(String filename, String moodlename){
		
	    try {
			FileInputStream fis = new FileInputStream(filename);
			Workbook wb = new HSSFWorkbook(fis);

			Sheet sheet = wb.getSheetAt(0);
			State state = State.BEGIN;
			
			int mi = -1, si = -1;

			for (Row row : sheet){
				switch(state){
					case BEGIN: 
						if(row.getCell(0).toString().equals(START_TAG))
							state = State.HEADER;
							break;
					case CONTENT:
						if(row.getCell(0).toString().equals(END_TAG)){
							state = State.END;
							break;
						}
//						System.out.println(row.getCell(si) + "\t" + row.getCell(mi));
						String[] name = row.getCell(si).toString().split(",");
						String firstname = name[1];
						String lastname = name[0];
						String matnr = null;
						Cell mCell = row.getCell(mi);
						if(mCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
							matnr = Integer.toString((int)mCell.getNumericCellValue());
						else
							matnr = mCell.toString();
						if(moodlename.startsWith(firstname) && moodlename.endsWith(lastname))
							return new Student(firstname, lastname, matnr);
						break;
					case END: // do nothing, read until end
						break;
					case HEADER:
						state = State.CONTENT;
						for(int i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++){
							Cell cell = row.getCell(i);
							if(cell != null && cell.toString().equals(MTKNR))
								mi = i;
							if(cell != null && cell.toString().equals(SORTNAME))
								si = i;
						}
						break;
				}
			}
			
			fis.close();
			wb.close();
		} catch (IOException e) {
//			e.printStackTrace();
		}
	    return null;
	}

}
