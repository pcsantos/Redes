import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

	private static final String ARQUIVO_CANDIDATOS = "candidatos.txt";
	private static final String ARQUIVO_FINAL = "urnas_apuradas.txt";

	public static void main(String[] args) throws IOException {

        if(args.length != 1) {
            System.out.println("Nro de parametros incorreto!");
            System.out.println("Para rodar o Servidor corretamente utilize: java Servidor [porta]");
            System.exit(1);
        }
        int porta = Integer.parseInt(args[0]);

		System.out.println("#### Servidor em Funcionamento ####");
		ServerSocket serverSocket = new ServerSocket(porta);
		try {
			while (true) {
				Socket socket = serverSocket.accept();
				atendimentoCliente(socket);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			serverSocket.close();
		}
	}
	//Atendimento as requisições do cliente
	private static void atendimentoCliente(final Socket socket) {

		Thread thread = new Thread() {
			public void run() {
				try {

					BufferedReader buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
					// Recebe código do cliente
					String codigo = buffReader.readLine();
					if (codigo.equals("999")) {
						enviarListaCandidatos();	
					}
					if (codigo.equals("888")) {
					// Recebe Votação finalizada de uma urna
						receberVotosUrna();
						System.out.println("\n#### Urnas Apuradas até o momento ####\n");
						gerarStatus();
						System.out.println("\nAguardando mais urnas finalizarem a votação...\n");
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			private void enviarListaCandidatos() {
		
				try {	
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					BufferedReader buffReader = new BufferedReader(new FileReader(ARQUIVO_CANDIDATOS));
					String linha;
					while((linha = buffReader.readLine()) != null) {
						writer.write(linha + "\n");
						writer.flush();
					}
					buffReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			private void receberVotosUrna() {
				
				try {
					BufferedWriter buffWriter = new BufferedWriter(new FileWriter(ARQUIVO_FINAL, true));
					BufferedReader buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
					String linha;
					String urna = " " + socket.getInetAddress();
					buffWriter.append(urna + "\n");
					while((linha = buffReader.readLine()) != null) {
						buffWriter.append(linha + "\n");
					}
					buffReader.close();
					buffWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	private static void gerarStatus() {
		
		Arquivo arquivoCandidatos = new Arquivo(ARQUIVO_CANDIDATOS);
		List<Candidato> listaCandidatos = arquivoCandidatos.listaCandidatos();
		Arquivo arquivoFinal = new Arquivo(ARQUIVO_FINAL);
		
		arquivoFinal.apurarVotos(listaCandidatos);
	}
}

