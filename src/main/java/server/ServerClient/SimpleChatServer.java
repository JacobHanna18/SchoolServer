package server.ServerClient;
import com.google.gson.Gson;
import server.App;
import server.Commands;
import server.Server.AbstractServer ;
import server.Server.ConnectionToClient ;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import server.clientClasses.*;

import server.clientClasses.*;


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
					client.sendToClient(cmd.getGradesOfStudent(ca.studentID,0));
					break;
                case subjectList:
                    client.sendToClient(cmd.allSubjects());
                    break;
                case examList:
                    client.sendToClient(cmd.subjectExamList(ca.subjectID));
                    break;
                case questionList:
                    client.sendToClient(cmd.subjectQuestionList(ca.subjectID));
                    break;
                case questionByTeacher:
                    client.sendToClient(cmd.questionsOfTeacher(ca.teacherID));
                    break;
                case examByTeacher:
                    client.sendToClient(cmd.examsFromTeacher(ca.teacherID));
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
