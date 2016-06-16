import java.io.*;
import java.util.*;

public class Arquivo {
	
	private String arquivo;

	public Arquivo(String arquivo) {
		this.arquivo = arquivo;
	}
	//carrega a lista de candidatos de um aquivo 
	public  List<Candidato> listaCandidatos() {
                List<Candidato> listaCandidatos = new ArrayList<>();
              	try {
			BufferedReader buffReader =  new BufferedReader(new FileReader(this.arquivo));

                	String linha;
                	while((linha = buffReader.readLine()) != null) {
                        	StringTokenizer st = new StringTokenizer(linha, "|");
                        	Candidato candidato = new Candidato();
                        	candidato.setCodigo(Integer.parseInt(st.nextToken()));
                        	candidato.setNome(st.nextToken());
                        	candidato.setPartido(st.nextToken());
                        	candidato.setNroVotos(Integer.parseInt(st.nextToken()));
                        	listaCandidatos.add(candidato);
                	}
                	buffReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

                return listaCandidatos;
        }
	//faz o soma de todos os votos registrados e imprime o resultado das urnas apuradas 
	public void apurarVotos(List<Candidato> listaCandidatos) {
               
		String urnas = "";
		int nroCandidatos = listaCandidatos.size();	
		try {
                        BufferedReader buffReader = new BufferedReader(new FileReader(this.arquivo));
                        String linha;
                        while((linha = buffReader.readLine()) != null) {
				urnas += linha + "  ";
				for(int i = 0; i < nroCandidatos; i++) {
					linha = buffReader.readLine();
                        		StringTokenizer st = new StringTokenizer(linha, "|");
                        		Candidato candidato = new Candidato();
                        		candidato.setCodigo(Integer.parseInt(st.nextToken()));
                        		candidato.setNome(st.nextToken());
                        		candidato.setPartido(st.nextToken());
                        		candidato.setNroVotos(Integer.parseInt(st.nextToken()));
					for(Candidato c: listaCandidatos) {
						if(c.getCodigo() == candidato.getCodigo()) {
							c.setNroVotos(c.getNroVotos() + candidato.getNroVotos());
							break;
						}
					}
				}
                        }
                        buffReader.close();

                } catch (IOException e) {
                        e.printStackTrace();
                }
			System.out.println(urnas);
			for(Candidato candidato: listaCandidatos) {
				System.out.printf("Codigo: %d - Nome: %s - Partido: %s - NroVotos: %d\n", candidato.getCodigo(),
						candidato.getNome(), candidato.getPartido(), candidato.getNroVotos());
			}
	}
}
