package uy.edu.um.telmovil.terminal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import uy.edu.um.telmovil.msg.Msg;
import uy.edu.um.telmovil.msg.RegistrationMsg;
import uy.edu.um.telmovil.msg.SimpleMsg;
import uy.edu.um.telmovil.radiobase.BaseStation;
import uy.edu.um.telmovil.utils.ConstantesGenerales;

import com.google.gson.Gson;

public class Terminal {

	private String terminalID;
	
	private static final Gson gson = new Gson();

	public void register(int socket) throws IOException{
//		int socket = base.getConexionSocket();
		RegistrationMsg msg = new RegistrationMsg();
		msg.setMin(terminalID);
		msg.setMsn(terminalID);
		msg.setTipo(ConstantesGenerales.TIPO_MSG_REGISTRATION_MSG);
		String respuesta = sendRequest(msg, socket);
	}
	
	private String sendRequest(Msg msg, int socket) throws IOException{
		Socket clientSocket = new Socket("localhost", socket);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String mensaje = gson.toJson(msg);
		outToServer.writeBytes(mensaje + '\n');
		String response = inFromServer.readLine();
		System.out.println("Respuesta desde el servidor: "+(response));
		return response;
	}
	
	public String getTerminalID() {
		return terminalID;
	}

	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}
}
