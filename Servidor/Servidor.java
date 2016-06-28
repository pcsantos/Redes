import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

	private static final String ARQUIVO_CANDIDATOS = "candidatos.txt";
	private static final String ARQUIVO_FINAL = "urnas_apuradas.txt";

	public static void main(String[] args) throws IOException {
		System.out.println("#### Servidor em Funcionamento ####");
		ServerSocket serverSocket = new ServerSocket(40007);
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
						receberVotosUrna(buffReader);
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
					boolean final_do_arquivo = false;
					String mensagem_arquivo = "";
					
					while(!final_do_arquivo){
						while((linha = buffReader.readLine()) != null) {
							mensagem_arquivo= mensagem_arquivo + linha + "\n";
						}
						final_do_arquivo = mensagem_arquivo.contains("Chun");
						if(final_do_arquivo){
							writer.write(mensagem_arquivo);
							writer.flush();
						}
					}

					writer.close();
					buffReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			private void receberVotosUrna(BufferedReader buffReader) {
				
				boolean final_da_mensagem = false;
				try {
					BufferedWriter buffWriter = new BufferedWriter(new FileWriter(ARQUIVO_FINAL, true));
					String linha;
					String urna = " " + socket.getInetAddress()+"\n";
					int count = 0;
					
					while(!final_da_mensagem && count<3){
						
						while((linha = buffReader.readLine()) != null) {
							urna= urna + linha + "\n";
							
						}
						final_da_mensagem = urna.contains("Chun");
						if(final_da_mensagem){
							
							buffWriter.append(urna);
							buffWriter.flush();
						}
						if(final_da_mensagem == false){
							Thread.sleep(500);
							count++;
						}
					}

					if(count == 3){System.out.println("Connection Timeout(1,5s) com ip: " + socket.getInetAddress());}

					buffReader.close();
					buffWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}catch(InterruptedException e){}
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

