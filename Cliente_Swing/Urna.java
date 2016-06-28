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
	private	List<Candidato> listaCandidatos = null;
        private Arquivo arquivoCandidatos = null;
        private Arquivo arquivoVotos = null;
	private JLabel lblLabel1;
	private JLabel lblLabel2;
	private JLabel lblLabel3;
	private JButton btnButton1;
	private JButton btnButton2;
	private JButton btnButton3;
	private JButton btnButton4;
	private JButton btnButton5;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
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

		lblLabel1 = new JLabel("Urna fora de funcionamento!");
		lblLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		lblLabel1.setBounds(82, 12, 297, 15);
		getContentPane().add(lblLabel1);

		lblLabel2 = new JLabel("Carregue a lista de Candidatos.");
		lblLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		lblLabel2.setBounds(82, 39, 297, 15);
		getContentPane().add(lblLabel2);

		lblLabel3 = new JLabel("Urna em funcionamento!");
		lblLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		lblLabel3.setBounds(82, 22, 297, 15);
		getContentPane().add(lblLabel3);
		lblLabel3.setVisible(false);

		btnButton1 = new JButton("1 - Votar");
		btnButton1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Votar votar = new Votar(listaCandidatos, arquivoVotos);
				votar.setVisible(true);
				nroVotos++;
			}
		});
		btnButton1.setBounds(82, 80, 297, 25);
		getContentPane().add(btnButton1);
		btnButton1.setVisible(false);

		btnButton2 = new JButton("2 - Votar branco");
		btnButton2.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                arquivoVotos.gravarVoto(2);
				JOptionPane.showMessageDialog(null, "Voto realizado!");
				nroVotos++;
                        }
                });
		btnButton2.setBounds(82, 110, 297, 25);
		getContentPane().add(btnButton2);
		btnButton2.setVisible(false);
		
		btnButton3 = new JButton("3 - Votar nulo");
		btnButton3.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                arquivoVotos.gravarVoto(3);
				JOptionPane.showMessageDialog(null, "Voto realizado!");
				nroVotos++;
                        }
                });
		btnButton3.setBounds(82, 140, 297, 25);
		getContentPane().add(btnButton3);
		btnButton3.setVisible(false);
		
		btnButton4 = new JButton("999 - Carregar lista de Candidatos.");
		btnButton4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Cliente conexao = new Cliente();
                                try {
                            		conexao.receberTransferenciaArquivoCandidatos(ARQ_CANDIDATOS);
                      	                arquivoCandidatos = new Arquivo(ARQ_CANDIDATOS);
      	                                listaCandidatos = arquivoCandidatos.listaCandidatos();
                                        arquivoVotos = new Arquivo(ARQ_VOTOS);
               	                        arquivoVotos.inicializarVotos(listaCandidatos);
                                } catch (IOException err) {
                                        err.printStackTrace();
                                }
				lblLabel1.setVisible(false);
				lblLabel2.setVisible(false);
				lblLabel3.setVisible(true);
				btnButton1.setVisible(true);
				btnButton2.setVisible(true);
				btnButton3.setVisible(true);
				btnButton4.setVisible(false);
			}
		});
		btnButton4.setBounds(82, 170, 297, 25);
		getContentPane().add(btnButton4);
		
		btnButton5 = new JButton("888 - Finalizar votação");
		btnButton5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (nroVotos > 0) {
                        	Cliente conexao = new Cliente();
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
		btnButton5.setBounds(82, 200, 297, 25);
		getContentPane().add(btnButton5);
	}

	private static Arquivo contagemVotos(List<Candidato> listaCandidatos, Arquivo arqVotos) {
                Arquivo arqFechamento = new Arquivo(ARQ_FECHAMENTO_URNA);
                arqFechamento.gerarFechamento(listaCandidatos, arqVotos);
                return arqFechamento;
        }
}
