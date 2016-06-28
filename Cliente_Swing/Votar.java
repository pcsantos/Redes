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
	
	private JTextField textField;
	private JTextField textField1;
	private JTextField textField2;
	private JLabel lblLabel;
	private JLabel lblLabel1;
	private JLabel lblLabel2;
	private JLabel lblAvatar;
	private ImageIcon image;
	private JButton btnButton;
	private JButton btnButton1;
	private JButton btnButton2;
	private JButton btnButton3;
	
	public Votar(final List<Candidato> listaCandidatos, final Arquivo arquivoVotos) {
		setTitle("Votar");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setModal(true);
		this.setResizable(false);

		textField = new JTextField();
		textField.setBounds(214, 100, 166, 19);
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setEnabled(false);	
	
		textField1 = new JTextField();
		textField1.setBounds(244, 25, 51, 19);
		getContentPane().add(textField1);
		textField1.setColumns(10);
		
		textField2 = new JTextField();
		textField2.setColumns(10);
		textField2.setBounds(214, 70, 166, 19);
		getContentPane().add(textField2);
		textField2.setEnabled(false);
		
		lblLabel = new JLabel("Número do seu candidato:");
		lblLabel.setBounds(46, 27, 206, 15);
		getContentPane().add(lblLabel);
		
		lblLabel1 = new JLabel("Nome:");
		lblLabel1.setBounds(150, 70, 70, 15);
		getContentPane().add(lblLabel1);
		
		lblLabel2 = new JLabel("Partido:");
		lblLabel2.setBounds(150, 100, 70, 15);
		getContentPane().add(lblLabel2);

		lblAvatar = new JLabel("Imagem");
		lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
		lblAvatar.setBounds(46, 40, 91, 87);
		getContentPane().add(lblAvatar);
		
		btnButton3 = new JButton("Ok");
		btnButton3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!textField1.getText().trim().equals("")) {
					int codigo = Integer.parseInt(textField1.getText());
					boolean flag = true;
					for(Candidato candidato: listaCandidatos) {
						if(candidato.getCodigo() == codigo) {
							textField2.setText(candidato.getNome());
							textField.setText(candidato.getPartido());
							if(candidato.getCodigo() != 2 && candidato.getCodigo() != 3) {
								image = new ImageIcon(getClass().getResource("sfportrait/" + candidato.getNome() + ".gif"));
								lblAvatar.setIcon(image);
							}
							flag = false;
							break;
						}
					}
					if(flag) {
						textField2.setText("");
						textField.setText("");
					}
				}
			}
		});
		btnButton3.setBounds(307, 22, 73, 25);
		getContentPane().add(btnButton3);
		
		btnButton = new JButton("Confirmar");
		btnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!textField1.getText().trim().equals("") && !textField2.getText().trim().equals("")) {
					arquivoVotos.gravarVoto(Integer.parseInt(textField1.getText()));
					JOptionPane.showMessageDialog(null, "Voto realizado!");
					dispose();
				}
			}
		});
		btnButton.setBounds(46, 137, 334, 25);
		getContentPane().add(btnButton);
		
		btnButton1 = new JButton("2 - Votar branco");
		btnButton1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				arquivoVotos.gravarVoto(2);
				JOptionPane.showMessageDialog(null, "Voto realizado!");
				dispose();
			}
		});
		btnButton1.setBounds(46, 174, 334, 25);
		getContentPane().add(btnButton1);
		
		btnButton2 = new JButton("3 - Votar nulo");
		btnButton2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				arquivoVotos.gravarVoto(3);
				JOptionPane.showMessageDialog(null, "Voto realizado!");
				dispose();
			}
		});
		btnButton2.setBounds(46, 211, 334, 25);
		getContentPane().add(btnButton2);
	}
}

