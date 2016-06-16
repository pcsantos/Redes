import java.awt.EventQueue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.util.*;
import java.io.*;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class Votar extends JDialog {
	
	private JTextField textPartido;
	private JTextField textNroCandidato;
	private JTextField textNomeCandidato;
	private JLabel lblNroCandidato;
	private JLabel lblNome;
	private JLabel lblPartido;
	private JLabel lblAvatar;
	private ImageIcon image;
	private JButton btnConfirmarVoto;
	private JButton btnVotarBranco;
	private JButton btnVotarNulo;
	private JButton btnVerificarSeCandidatoOk;
	
	public Votar(final List<Candidato> listaCandidatos, final Arquivo arquivoVotos) {
		setTitle("Votar");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setModal(true);
		this.setResizable(false);

		textPartido = new JTextField();
		textPartido.setBounds(214, 100, 166, 19);
		getContentPane().add(textPartido);
		textPartido.setColumns(10);
		textPartido.setEnabled(false);	
	
		textNroCandidato = new JTextField();
		textNroCandidato.setBounds(244, 25, 51, 19);
		getContentPane().add(textNroCandidato);
		textNroCandidato.setColumns(10);
		
		textNomeCandidato = new JTextField();
		textNomeCandidato.setColumns(10);
		textNomeCandidato.setBounds(214, 70, 166, 19);
		getContentPane().add(textNomeCandidato);
		textNomeCandidato.setEnabled(false);
		
		lblNroCandidato = new JLabel("Número do seu candidato:");
		lblNroCandidato.setBounds(46, 27, 206, 15);
		getContentPane().add(lblNroCandidato);
		
		lblNome = new JLabel("Nome:");
		lblNome.setBounds(150, 70, 70, 15);
		getContentPane().add(lblNome);
		
		lblPartido = new JLabel("Partido:");
		lblPartido.setBounds(150, 100, 70, 15);
		getContentPane().add(lblPartido);

		lblAvatar = new JLabel("Imagem");
		lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
		lblAvatar.setBounds(46, 40, 91, 87);
		getContentPane().add(lblAvatar);
		
		btnVerificarSeCandidatoOk = new JButton("Ok");
		btnVerificarSeCandidatoOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!textNroCandidato.getText().trim().equals("")) {
					int codigo = Integer.parseInt(textNroCandidato.getText());
					boolean flag = true;
					for(Candidato candidato: listaCandidatos) {
						if(candidato.getCodigo() == codigo) {
							textNomeCandidato.setText(candidato.getNome());
							textPartido.setText(candidato.getPartido());
							if(candidato.getCodigo() != 2 && candidato.getCodigo() != 3) {
								image = new ImageIcon(getClass().getResource("sfportrait/" + candidato.getNome() + ".gif"));
								lblAvatar.setIcon(image);
							}
							flag = false;
							break;
						}
					}
					if(flag) {
						textNomeCandidato.setText("");
						textPartido.setText("");
					}
				}
			}
		});
		btnVerificarSeCandidatoOk.setBounds(307, 22, 73, 25);
		getContentPane().add(btnVerificarSeCandidatoOk);
		
		btnConfirmarVoto = new JButton("Confirmar");
		btnConfirmarVoto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!textNroCandidato.getText().trim().equals("") && !textNomeCandidato.getText().trim().equals("")) {
					arquivoVotos.gravarVoto(Integer.parseInt(textNroCandidato.getText()));
					JOptionPane.showMessageDialog(null, "Voto realizado!");
					dispose();
				}
			}
		});
		btnConfirmarVoto.setBounds(46, 137, 334, 25);
		getContentPane().add(btnConfirmarVoto);
		
		btnVotarBranco = new JButton("2 - Votar branco");
		btnVotarBranco.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				arquivoVotos.gravarVoto(2);
				JOptionPane.showMessageDialog(null, "Voto realizado!");
				dispose();
			}
		});
		btnVotarBranco.setBounds(46, 174, 334, 25);
		getContentPane().add(btnVotarBranco);
		
		btnVotarNulo = new JButton("3 - Votar nulo");
		btnVotarNulo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				arquivoVotos.gravarVoto(3);
				JOptionPane.showMessageDialog(null, "Voto realizado!");
				dispose();
			}
		});
		btnVotarNulo.setBounds(46, 211, 334, 25);
		getContentPane().add(btnVotarNulo);
	}
}

