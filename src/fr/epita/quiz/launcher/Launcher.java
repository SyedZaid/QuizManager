package fr.epita.quiz.launcher;

import java.util.Scanner;

import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.Student;
import fr.epita.quiz.services.QuestionJDBCDAO;

public class Launcher {

	static Scanner scn = new Scanner(System.in);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("May I know who is this, Student or a Teacher ? Enter 1 , if you're a student and 2 if faculty");
		
		int choice_Chosen = scn.nextInt();
		scn.nextLine();
		if(choice_Chosen==1)
		{
			studentoperations();
		}
		else
		{
			System.out.println("Enter the user name:");
			String uname = scn.nextLine();
			System.out.println("Enter the Password:");
			String pwd = scn.nextLine();
			
			if(authenticateAdminmodule(uname,pwd))
			{
				System.out.println("Need to update or create the questions ?");
				if("Y".equals(scn.nextLine()))
				{
					createOrUpdateQuestions();
				}
				else {
					System.out.println("You want to verify the answers of students ?");
					if("Y".equals(scn.nextLine()))
					{
						Answer_Verificationn();
					}
				}
			}
		}
		

	}

	private static void Answer_Verificationn() {
		// TODO Auto-generated method stub
		System.out.println("Please enter the student id to verify the answers:");
		int stdId = scn.nextInt();
		scn.nextLine();
		Student std = new Student();
		std.setStudId(stdId);
		QuestionJDBCDAO qnjdbc = new QuestionJDBCDAO();
		qnjdbc.Answer_Verification(std);
		System.out.println("Answers are checked");
		
	}

	private static void createOrUpdateQuestions() {
		// TODO Auto-generated method stub
		System.out.println("DO u wish to add qns?");
		if("Y".equals(scn.nextLine()))
		{
			System.out.println("Give the Question id?");
			int qnId = scn.nextInt();
			scn.nextLine();
			System.out.println("Give the question");
			String questn = scn.nextLine();
			System.out.println("Answer");
			String ans = scn.nextLine();
			System.out.println("OD");
			String opt_A = scn.nextLine();
			System.out.println("O_A");
			String opt_B = scn.nextLine();
			System.out.println("O_B");
			String opt_C = scn.nextLine();
			System.out.println("O_C");
			String opt_D = scn.nextLine();
			System.out.println("Topic");
			String topic = scn.nextLine();
			System.out.println("Difficulty");
			int diff = scn.nextInt();
			scn.nextLine();
			Question quest = new Question();
			quest.setId(qnId);
			quest.setQuestion(questn);
			quest.setAnswer(ans);
			quest.setDifficulty(diff);
			quest.setTopics(topic);
			quest.setOption_A(opt_A);
			quest.setOption_B(opt_B);
			quest.setOption_C(opt_C);
			quest.setOption_D(opt_D);
			QuestionJDBCDAO qnjdbc = new QuestionJDBCDAO();
			qnjdbc.AddFacultyQuestion(quest);
		}
		else {
			System.out.println("Updation !");
			System.out.println("Please enter the Question id?");
			int qnId = scn.nextInt();
			scn.nextLine();
			System.out.println("Question");
			String questn = scn.nextLine();
			System.out.println("Answer");
			String ans = scn.nextLine();
			System.out.println("OD");
			String opt_A = scn.nextLine();
			System.out.println("O_A");
			String opt_B = scn.nextLine();
			System.out.println("O_B");
			String opt_C = scn.nextLine();
			System.out.println("O_C");
			String opt_D = scn.nextLine();
			System.out.println("Topic");
			String topic = scn.nextLine();
			System.out.println("Difficulty");
			int diff = scn.nextInt();
			scn.nextLine();
			Question quest = new Question();
			quest.setId(qnId);
			quest.setQuestion(questn);
			quest.setAnswer(ans);
			quest.setDifficulty(diff);
			quest.setTopics(topic);
			quest.setOption_A(opt_A);
			quest.setOption_B(opt_B);
			quest.setOption_C(opt_C);
			quest.setOption_D(opt_D);
			QuestionJDBCDAO qnjdbc = new QuestionJDBCDAO();
			qnjdbc.Question_Updation(quest);			
		}
	}

	private static boolean authenticateAdminmodule(String uname, String pwd) {
		// TODO Auto-generated method stub
		if("pikachu".equals(uname) && "pokemon".equals(pwd))
		{
			return true;
		}
		return false;
		
	}

	private static void studentoperations() {
		// TODO Auto-generated method stub
		System.out.println("Student, Enter the username");
		String usrname = scn.nextLine();
		System.out.println("Password");
		String pswd = scn.nextLine();
		System.out.println("Id");
		int id = scn.nextInt();
		scn.nextLine();
		authenticateStdmodule(usrname, pswd , id);
		
		
	}


	private static void authenticateStdmodule(String usrname, String pswd, int id) {
		// TODO Auto-generated method stub
		Student std = new Student();
		std.setStudId(id);
		std.setPassword(pswd);
		std.setUsername(usrname);
		QuestionJDBCDAO db = new QuestionJDBCDAO();
		if(db.Student_Authentication(std))
		{
			System.out.println("Do you want to take the quiz?");
			if("Y".equals(scn.nextLine()))
				db.Quiz_Start(scn, id);
			else
			{
				System.out.println("you wish to see");
				if("Y".equals(scn.nextLine()))
					db.Score_validation(id);
			}
		}
		else
		{
			System.out.println("Invalid student");
		}
		
	}

}
