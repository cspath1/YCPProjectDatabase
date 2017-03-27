package edu.ycp.cs320.cspath1.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.ycp.cs320.cspath1.enums.ClassType;
import edu.ycp.cs320.cspath1.enums.MajorType;
import edu.ycp.cs320.cspath1.user.Faculty;
import edu.ycp.cs320.cspath1.user.Student;

public class InitialData {
	public static List<Faculty> getFaculty() throws IOException {
		List<Faculty> facultyList = new ArrayList<Faculty>();
		ReadCSV readFaculty = new ReadCSV("FacultyUser.CSV");
		try {
			Integer userID = 1;
			while (true) {
				List<String> tuple = readFaculty.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Faculty faculty = new Faculty();
				faculty.setUserID(userID++);
				faculty.setFirstname(i.next());
				faculty.setLastname(i.next());
				faculty.setPassword(i.next());
				String string = i.next();
				MajorType major = getMajorTypeFromParameter(string);
				faculty.setMajor(major);
				faculty.setEmail(i.next());
				faculty.setUsername(i.next());
				facultyList.add(faculty);
			}
			return facultyList;
		} finally {
			readFaculty.close();
		}
		
	}
	
	public static List<Student> getStudents() throws IOException {
		List<Student> studentList = new ArrayList<Student>();
		ReadCSV readStudents = new ReadCSV("StudentUsers.CSV");
		try {
			Integer userID = 18;
			while (true) {
				List<String> tuple = readStudents.next();
				if (tuple == null){
					break;
				}
				Iterator<String> i = tuple.iterator();
				Student student = new Student();
				student.setUserID(userID++);
				student.setFirstname(i.next());
				student.setLastname(i.next());
				student.setPassword(i.next());
				String string = i.next();
				MajorType major = getMajorTypeFromParameter(string);
				student.setMajor(major);
				string = i.next();
				ClassType classtype = getClassTypeFromParameter(string);
				student.setClassLevel(classtype);
				student.setEmail(i.next());
				student.setUsername(i.next());
				studentList.add(student);
			}
			return studentList;
		} finally {
			readStudents.close();
		}
	}
	
	private static MajorType getMajorTypeFromParameter(String s){
		MajorType majortype = null;
		if (s == null || s.equals("")){
			return null;
		}
		else if (s.equals("ME")){
			majortype = MajorType.ME;
			
		}
		else if (s.equals("CE")){
			majortype = MajorType.CE;
		}
		else if(s.equals("CS")){
			majortype = MajorType.CS;
		}
		else if(s.equals("EE")){
			majortype = MajorType.EE;
		}
		return majortype;
	}
	
	private static ClassType getClassTypeFromParameter(String s){
		ClassType classtype = null;
		if(s == null || s.equals("")){
			return null;
		}
		else if (s == "FRESHMAN"){
			classtype = ClassType.FRESHMAN;
		}
		else if (s == "SOPHOMORE"){
			classtype = ClassType.SOPHOMORE;
		}
		else if (s == "JUNIOR"){
			classtype = ClassType.JUNIOR;
		}
		else if (s == "SENIOR"){
			classtype = ClassType.SENIOR;
		}
		return classtype;
	}
}