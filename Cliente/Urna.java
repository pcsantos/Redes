import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import java.util.*;
import java.io.*;

public class Urna extends JFrame {
	private static final String ARQ_VOTOS = "votos.txt";
    private static final String ARQ_CANDIDATOS = "candidatosEmVotacao.txt";
    private static final String ARQ_FECHAMENTO_URNA = "fechamento.txt";
	private static int nroVotos = 0;
    private static String servidor;
    private static int porta;
	private	List<Candidato> listaCandidatos = null;
    private Arquivo arquivoCandidatos = null;
    private Arquivo arquivoVotos = null;
	private JLabel lblUrnaForaDeFuncionamento;
	private JLabel lblCarregarCandidatos;
	private JLabel lblUrnaEmFuncionamento;
	private JButton btnVotar;
	private JButton btnVotarBranco;
	private JButton btnVotarNulo;
	private JButton btnCarregarListaCandidatos;
	private JButton btnFinalizarVotacao;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
                if(args.length != 2) {
                    System.out.println("Nro de parametros incorreto!");
                    System.out.println("Para rodar a Urna corretamente utilize: java Urna [servidor] [porta]");
                    System.exit(1);
                }
                servidor = args[0];
                porta = Integer.parseInt(args[1]);

				try {
					Urna frame = new Urna();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Urna() {
		setTitle("Urna");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(null);
		this.setResizable(false);

		lblUrnaForaDeFuncionamento = new JLabel("Urna fora de funcionamento!");
		lblUrnaForaDeFuncionamento.setHorizontalAlignment(SwingConstants.CENTER);
		lblUrnaForaDeFuncionamento.setBounds(82, 12, 297, 15);
		getContentPane().add(lblUrnaForaDeFuncionamento);

		lblCarregarCandidatos = new JLabel("Carregue a lista de Candidatos.");
		lblCarregarCandidatos.setHorizontalAlignment(SwingConstants.CENTER);
		lblCarregarCandidatos.setBounds(82, 39, 297, 15);
		getContentPane().add(lblCarregarCandidatos);

		lblUrnaEmFuncionamento = new JLabel("Urna em funcionamento!");
		lblUrnaEmFuncionamento.setHorizontalAlignment(SwingConstants.CENTER);
		lblUrnaEmFuncionamento.setBounds(82, 22, 297, 15);
		getContentPane().add(lblUrnaEmFuncionamento);
		lblUrnaEmFuncionamento.setVisible(false);

		btnVotar = new JButton("1 - Votar");
		btnVotar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Votar votar = new Votar(listaCandidatos, arquivoVotos);
				votar.setVisible(true);
				nroVotos++;
			}
		});
		btnVotar.setBounds(82, 80, 297, 25);
		getContentPane().add(btnVotar);
		btnVotar.setVisible(false);

		btnVotarBranco = new JButton("2 - Votar branco");
		btnVotarBranco.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                arquivoVotos.gravarVoto(2);
				JOptionPane.showMessageDialog(null, "Voto realizado!");
			    nroVotos++;
            }
        });
		btnVotarBranco.setBounds(82, 110, 297, 25);
		getContentPane().add(btnVotarBranco);
		btnVotarBranco.setVisible(false);
		
		btnVotarNulo = new JButton("3 - Votar nulo");
		btnVotarNulo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                arquivoVotos.gravarVoto(3);
				JOptionPane.showMessageDialog(null, "Voto realizado!");
				nroVotos++;
            }
        });
		btnVotarNulo.setBounds(82, 140, 297, 25);
		getContentPane().add(btnVotarNulo);
		btnVotarNulo.setVisible(false);
		
		btnCarregarListaCandidatos = new JButton("999 - Carregar lista de Candidatos.");
		btnCarregarListaCandidatos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Cliente conexao = new Cliente(servidor, porta);
                try {
                    conexao.receberTransferenciaArquivoCandidatos(ARQ_CANDIDATOS);
                    arquivoCandidatos = new Arquivo(ARQ_CANDIDATOS);
      	            listaCandidatos = arquivoCandidatos.listaCandidatos();
                    arquivoVotos = new Arquivo(ARQ_VOTOS);
               	    arquivoVotos.inicializarVotos(listaCandidatos);
                } catch (IOException err) {
                    err.printStackTrace();
                }
				lblUrnaForaDeFuncionamento.setVisible(false);
				lblCarregarCandidatos.setVisible(false);
				lblUrnaEmFuncionamento.setVisible(true);
				btnVotar.setVisible(true);
				btnVotarBranco.setVisible(true);
				btnVotarNulo.setVisible(true);
				btnCarregarListaCandidatos.setVisible(false);
			}
		});
		btnCarregarListaCandidatos.setBounds(82, 170, 297, 25);
		getContentPane().add(btnCarregarListaCandidatos);
		
		btnFinalizarVotacao = new JButton("888 - Finalizar votação");
		btnFinalizarVotacao.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (nroVotos > 0) {
                    Cliente conexao = new Cliente(servidor, porta);
                    try {
                        Arquivo arquivoFechamento = contagemVotos(listaCandidatos, arquivoVotos);
                        conexao.enviarContagem(ARQ_FECHAMENTO_URNA);
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                } else {
					JOptionPane.showMessageDialog(null, "Nenhuma votação foi realizada!");
               	}
				JOptionPane.showMessageDialog(null, "Urna finalizada!");
				dispose();
			}
		});
		btnFinalizarVotacao.setBounds(82, 200, 297, 25);
		getContentPane().add(btnFinalizarVotacao);
	}

	private static Arquivo contagemVotos(List<Candidato> listaCandidatos, Arquivo arqVotos) {
        Arquivo arqFechamento = new Arquivo(ARQ_FECHAMENTO_URNA);
        arqFechamento.gerarFechamento(listaCandidatos, arqVotos);
        return arqFechamento;
    }
}
