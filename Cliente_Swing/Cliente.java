import java.io.*;
import java.net.Socket;
import java.util.*;

public class Cliente {

	private Socket socket;

	public Cliente() {

		try {	//Rodar servidor no nó Maia
			this.socket = new Socket("maia", 40007);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//solicita e recebe o arquivo de candidatos do servidor e faz uma copia no Cliente
	public void receberTransferenciaArquivoCandidatos(String arqDestino) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8");
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(arqDestino));
		List<Candidato> listaCandidatos = new ArrayList<>();
		
		// enviando para o servidor solicitação de lista de candidatos
		writer.write("999" + "\n");
		writer.flush();
		// recebendo do server a lista e gravando em arquivo destino
		String linha;
		while((linha = buffReader.readLine()) != null) {
			buffWriter.append(linha +"\n");
		}
		buffReader.close();
		buffWriter.close();

		this.socket.close();
	}
	//sinaliza e envia o arquivo de fechamento de urna para o servidor
	public void enviarContagem(String arqFechamento) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8");

		// enviando para o servidor sinalização para finalizar urna e em seguida enviar lista
		writer.write("888" + "\n");
		writer.flush();

		//enviando lista de candidatos com votos 
		BufferedReader buffReader = new BufferedReader(new FileReader(arqFechamento));
		String linha;
		while((linha = buffReader.readLine()) != null) {
			writer.write(linha + "\n");
			writer.flush();
		}
		buffReader.close();
		this.socket.close();
	}
}

