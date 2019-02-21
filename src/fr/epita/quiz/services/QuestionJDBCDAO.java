package fr.epita.quiz.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.Student;

public class QuestionJDBCDAO {
	
	
/*


DELETE FROM QUESTION WHERE ID = 3;



select * from question;*/
   private static final String INSERT_STATEMENT = "INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES (?, ?)";
   private static final String SEARCH_STATEMENT = "SELECT * FROM QUESTION";
   private static final String UPDATE_STATEMENT = "UPDATE QUESTION SET QUESTION=?, DIFFICULTY=? WHERE ID=?";
   private static final String DELETE_STATEMENT = "DELETE FROM QUESTION WHERE ID = ?";
	
	
	
	public void AddFacultyQuestion(Question question) {
		
		String inertStatement = "INSERT INTO FACULTY_QUESTIONS (QUESTION_ID , QUESTION, ANSWER,OPTION_A, OPTION_B, OPTION_C, OPTION_D, TOPIC , DIFFICULTY) VALUES (?, ?, ?, ? ,?,?,?,?,?)";
		
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(inertStatement);) {
			
			insertStatement.setInt(1, question.getId());
			insertStatement.setString(2, question.getQuestion());
			insertStatement.setString(3, question.getAnswer());
			insertStatement.setString(4, question.getOption_A());
			insertStatement.setString(5, question.getOption_B());
			insertStatement.setString(6, question.getOption_C());
			insertStatement.setString(7, question.getOption_D());
			insertStatement.setString(8, question.getTopics());
			insertStatement.setInt(9, question.getDifficulty());
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void Question_Updation(Question question) {
		
		String updateQuery = "UPDATE FACULTY_QUESTIONS SET QUESTION=?, ANSWER = ?, OPTION_A=?, OPTION_B=?, OPTION_C=?, OPTION_D=?, TOPIC=?, DIFFICULTY=? WHERE QUESTION_ID=?";

		
		try (Connection connection = getConnection();
			PreparedStatement updateStatement = connection.prepareStatement(updateQuery)){
			updateStatement.setString(1, question.getQuestion());
			updateStatement.setString(2, question.getAnswer());
			updateStatement.setString(3, question.getOption_A());
			updateStatement.setString(4, question.getOption_B());
			updateStatement.setString(5, question.getOption_C());
			updateStatement.setString(6, question.getOption_D());
			updateStatement.setString(7, question.getTopics());
			updateStatement.setInt(8, question.getDifficulty());
			updateStatement.setInt(9, question.getId());
			updateStatement.executeUpdate();
			connection.commit();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private Connection getConnection() throws SQLException {
		Configuration conf = Configuration.getInstance();
		String jdbcUrl = conf.getConfigurationValue("jdbc.url");
		String user = conf.getConfigurationValue("jdbc.user");
		String password = conf.getConfigurationValue("jdbc.password");
		Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
		return connection;
	}

	public void Question_Deletion(Question question) {
		
		try (Connection connection = getConnection();
			PreparedStatement deleteStatement = connection.prepareStatement(DELETE_STATEMENT)){
			deleteStatement.setInt(1, question.getId());
			deleteStatement.executeQuery();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void Answer_Verification(Student stdId) {
		
		String selectQuery = "SELECT Q_ID, ANSWERS FROM STUDENT_RESPONSES WHERE S_ID=?";
		String validAnswer ="";
		String answers = "";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			
			preparedStatement.setInt(1, stdId.getStudId());
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next())
			{
				int qnId = rs.getInt("Q_ID");
				answers = rs.getString("ANSWERS");
				String chkquery = "SELECT ANSWER FROM FACULTY_QUESTIONS WHERE QUESTION_ID =?";
				PreparedStatement chkStmt = connection.prepareStatement(chkquery);
				chkStmt.setInt(1, qnId);
				ResultSet rs1 = chkStmt.executeQuery();
				while(rs1.next())
				{
					validAnswer = rs1.getString("ANSWER");
				}
				if(validAnswer.equals(answers))
				{
					String updtQuery = "UPDATE STUDENT_RESPONSES SET IS_VALID=?";
					PreparedStatement updStmt = connection.prepareStatement(updtQuery);
					updStmt.setString(1, "Y");
					updStmt.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean Student_Authentication(Student std) {
		// TODO Auto-generated method stub
		String selectQuery = "SELECT STD_ID, STD_NAME, PWD FROM STUDENT_DETAILS WHERE STD_ID=?";
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			
			preparedStatement.setInt(1, std.getStudId());
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next())
			{
				int stId = rs.getInt("STD_ID");
				String uname = rs.getString("STD_NAME");
				String pwd = rs.getString("PWD");
				if(uname.equals(std.getUsername()) && pwd.equals(std.getPassword()) && stId==std.getStudId())
				{
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}

	public void Quiz_Start(Scanner sn, int stdid) {
		// TODO Auto-generated method stub
		String selectQuery = "SELECT QUESTION_ID, QUESTION,OPTION_A,OPTION_B,OPTION_C,OPTION_D FROM FACULTY_QUESTIONS";
		List qnidList = new ArrayList();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next())
			{
				qnidList.add(rs.getInt("QUESTION_ID"));
				for(int i=0; i<qnidList.size();i++)
				{
					String answer = "";
					System.out.println(rs.getString("QUESTION")+"\n"+rs.getString("OPTION_A")+"\n"+rs.getString("OPTION_B")+"\n"+rs.getString("OPTION_C")+"\n"+rs.getString("OPTION_D"));
					System.out.println("Please enter the answer");
					answer = sn.nextLine();
					Enter_Answer(rs.getInt("QUESTION_ID"), answer, stdid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void Enter_Answer(int qnid, String answer, int stdid) {
		// TODO Auto-generated method stub
		String updateQuery = "INSERT INTO STUDENT_RESPONSES (S_ID , ANSWERS, Q_ID) VALUES (?, ?, ?)";

		try (Connection connection = getConnection();
			PreparedStatement updateStatement = connection.prepareStatement(updateQuery)){
			updateStatement.setInt(1, stdid);
			updateStatement.setString(2, answer);
			updateStatement.setInt(3, qnid);
			updateStatement.execute();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void Score_validation(int id) {
		// TODO Auto-generated method stub
		String selectQuery = "SELECT IS_VALID FROM STUDENT_RESPONSES WHERE S_ID=?";
		int i=0;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
				) {
			
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while(rs.next())
			{
				String value = rs.getString("IS_VALID");
				if(value!=null && "Y".equals(value))
				{
					i++;
				}
			}
			System.out.println("Your score is "+i+"/"+columnsNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
