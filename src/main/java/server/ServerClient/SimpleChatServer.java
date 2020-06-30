package server.ServerClient;
import com.google.gson.Gson;
import org.apache.poi.xwpf.usermodel.*;
import server.App;
import server.Commands;
import server.Server.AbstractServer ;
import server.Server.ConnectionToClient ;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import server.clientClasses.*;

import server.clientClasses.*;


import server.entities.Grade;
import server.entities.Question;


public class SimpleChatServer extends AbstractServer {

	static public Gson gson = new Gson();
	static public Commands cmd = new Commands();

	public SimpleChatServer(int port) {
		super(port);
	}


	ArrayList<String> newQuestion = new ArrayList<String>();
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {

	    System.out.println(msg);
		clientAccess ca = gson.fromJson(msg.toString(),clientAccess.class);
		try {
			switch (ca.op){
				case logIn:
					client.user = cmd.LogIn(ca.userID,ca.password);
					client.sendToClient(gson.toJson(client.user));
					break;
				case teacherList:
					if(client.user.role == 3){
						client.sendToClient(cmd.allTeachers());
					}
					break;
				case studentList:
					if(client.user.role == 3){
						client.sendToClient(cmd.allStudents());
					}
					break;
				case gradesList:
				    if(client.user.role == 1){
                        client.sendToClient(cmd.getGradesOfStudent(client.user.id,1));
                    }else{
                        client.sendToClient(cmd.getGradesOfStudent(ca.studentID,0));
                    }
					break;
                case subjectList:
                    client.sendToClient(cmd.allSubjects());
                    break;
                case examList:
                    if(client.user.role > 1){
                        client.sendToClient(cmd.subjectExamList(ca.subjectID));
                    }
                    break;
                case questionList:
                    if(client.user.role > 1){
                        client.sendToClient(cmd.subjectQuestionList(ca.subjectID));
                    }
                    break;
                case questionByTeacher:
                    if(client.user.role > 1) {
                        client.sendToClient(cmd.questionsOfTeacher(ca.teacherID));
                    }
                    break;
                case examByTeacher:
                    if(client.user.role > 1) {
                        client.sendToClient(cmd.examsFromTeacher(ca.teacherID));
                    }
                    break;
				case getExam:
                    if(client.user.role > 1) {
                        client.sendToClient(cmd.getExam(ca.examID));
                    }
					break;
				case courseExam:
                    if(client.user.role > 1) {
                        client.sendToClient(cmd.examFromCourse(ca.courseID));
                    }
					break;
				case selectExamForCourse:
                    if(client.user.role == 2) {
                        cmd.selectExam(ca.examID, ca.courseID);
                    }
					break;
				case startExam:
                    if(client.user.role == 2) {
                        client.sendToClient(cmd.startExam(ca.courseID, ca.AccessCode, ca.duration, ca.online));
                    }
					break;
				case createExam:
				    if(client.user.role == 2){
                        cmd.createExam(ca.e,ca.subjectID,client.user.id);
                    }
					break;
				case createQuestion:
                    if(client.user.role == 2){
                        cmd.createQuestion(ca.q,ca.subjectID,client.user.id);
                    }
					break;
				case studentsFromCourse:
                    if(client.user.role > 1) {
                        client.sendToClient(cmd.courseStudents(ca.courseID));
                    }
					break;
				case coursesFromSubjectAndTeacher:
                    if(client.user.role > 1) {
                        client.sendToClient(cmd.coursesOfSubjectTeacher(ca.subjectID, ca.teacherID));
                    }
					break;
				case newRequest:
                    if(client.user.role == 2) {
                        cmd.newRequest(ca.courseID, ca.addedTime, ca.exp);
                    }
					break;
				case confirmGrade:
                    if(client.user.role == 2) {
                        cmd.confirmGrade(ca.studentID, ca.courseID);
                    }
					break;
				case changeAndConfirmGrade:
                    if(client.user.role == 2) {
                        cmd.changeAndConfirmGrade(ca.studentID, ca.courseID, ca.newGrade, ca.reason);
                    }
					break;
				case getGrade:
				    if(client.user.role == 1){
                        client.sendToClient(cmd.getGrade(client.user.id,ca.courseID));
                    }else{
                        client.sendToClient(cmd.getGrade(ca.studentID,ca.courseID));
                    }
					break;
				case getGradesOfCourse:
                    if(client.user.role > 1) {
                        client.sendToClient(cmd.getGradesOfCourse(ca.courseID));
                    }
					break;
				case takeExam:
                    if (client.user.role == 1) {
                        client.sendToClient(cmd.takeExam(ca.AccessCode,client.user.id));
                    }
					break;
				case submitOnlineExam:
				    if(client.user.role == 1){
                        cmd.submitOnlineExam(ca.arr,ca.courseID,client.user.id);
                    }
					break;
				case requestList:
                    if(client.user.role == 3) {
                        client.sendToClient(cmd.allRequests(0));
                    }
					break;
				case decideRequest:
                    if(client.user.role == 3) {
                        cmd.decideRequest(ca.requestID, ca.accept);
                    }
					break;
				case getManualExam:
					if(client.user.role == 1){
						client.sendToClient(cmd.getManualExam(client.user.id,ca.AccessCode));
					}
					break;
				case submitManualExam:
					if(client.user.role == 1){
						cmd.submitManualExam(ca.file,ca.courseID,client.user.id);
					}
					break;
				case downloadManualExam:
					if(client.user.role == 1){
						client.sendToClient(cmd.downloadStudentExam(client.user.id,ca.courseID));
					}else{
						client.sendToClient(cmd.downloadStudentExam(ca.studentID,ca.courseID));
					}
					break;


			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	  }
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		// TODO Auto-generated method stub
		
		System.out.println("Client Disconnected.");
		super.clientDisconnected(client);
	}
	@Override
	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
		System.out.println("Client connected: " + client.getInetAddress());
	}

	static public void toFile (byte[] bytes, String FilePath) throws IOException {
		OutputStream os = new FileOutputStream(FilePath);
		os.write(bytes);
		os.close();
	}

	static public byte[] toBytes (String FilePath) throws IOException {
		FileInputStream fis = null;
		File file = new File(FilePath);
		byte[] bArray = new byte[(int) file.length()];
		fis = new FileInputStream(file);
		fis.read(bArray);
		fis.close();
		return bArray;
	}

	public static void main(String[] args) throws IOException {

		App.SetUp();

		if (args.length != 1) {
			SimpleChatServer server = new SimpleChatServer(Integer.parseInt("1000"));
			server.listen();
			System.out.println("Server connected");
		} else {
			SimpleChatServer server = new SimpleChatServer(Integer.parseInt(args[0]));
			server.listen();
		}
	}

}
