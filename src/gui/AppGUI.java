package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import red.NeuralNet;

import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Font;
import java.util.Objects;
import javax.swing.JScrollPane;

public class AppGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private final JTextField txtKil;
	private final JTextArea txTrain, txTest, txPred;
	private final JButton btnTrain, btnPredecir, btnTest;
	private JComboBox<String> combColor, combClar, combCut = new JComboBox<>();
	private String[] clarity = { "IF", "VVS1", "VVS2", "VS1", "VS2", "SI12", "SI2", "I1" },
			cut = { "Fair", "Good", "Very_Good", "Premium", "Ideal" }, color = { "D", "E", "F", "G", "H", "I", "J" };
	private int cont = 0;
	NeuralNet rn = new NeuralNet();

	public AppGUI() {
		super("Predicting Diamonds Prices");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		getContentPane().setLayout(null);

		JLabel lbl1 = new JLabel("Diamonds App - Artificial Intelligence - Cristian Defaz", SwingConstants.CENTER);
		lbl1.setBounds(0, 0, 909, 29);
		getContentPane().add(lbl1);
		

		JLabel lbl2 = new JLabel("Train Summary", SwingConstants.CENTER);
		lbl2.setBackground(new Color(192, 192, 192));
		lbl2.setOpaque(true);
		lbl2.setBounds(90, 31, 400, 20);
		getContentPane().add(lbl2);

		JLabel lbl3 = new JLabel("Test Summary", SwingConstants.CENTER);
		lbl3.setOpaque(true);
		lbl3.setBackground(Color.LIGHT_GRAY);
		lbl3.setBounds(500, 31, 400, 20);
		getContentPane().add(lbl3);

		JLabel lbl5 = new JLabel("Color:");
		lbl5.setBounds(13, 344, 70, 25);
		getContentPane().add(lbl5);

		JLabel lbl4 = new JLabel("4C", SwingConstants.CENTER);
		lbl4.setOpaque(true);
		lbl4.setBackground(Color.LIGHT_GRAY);
		lbl4.setBounds(10, 313, 480, 20);
		getContentPane().add(lbl4);

		JLabel lbl6 = new JLabel("Carat:");
		lbl6.setBounds(13, 413, 70, 25);
		getContentPane().add(lbl6);

		JLabel lbl7 = new JLabel("Clarity:");
		lbl7.setBounds(13, 380, 67, 25);
		getContentPane().add(lbl7);

		JLabel lbl9 = new JLabel(" D (Best)  --> E > F > G > I -->  J (Worst)");
		lbl9.setBounds(267, 342, 220, 25);
		getContentPane().add(lbl9);

		JLabel lbl10 = new JLabel("From 0.2  -->  to 5.01");
		lbl10.setBounds(267, 411, 220, 25);
		getContentPane().add(lbl10);

		JLabel lbl11 = new JLabel("IF (Best)  -->  I1 (Worst)");
		lbl11.setBounds(267, 378, 220, 25);
		getContentPane().add(lbl11);

		JLabel lbl8 = new JLabel("Cut:");
		lbl8.setBounds(13, 450, 70, 25);
		getContentPane().add(lbl8);
		
		JLabel label = new JLabel("0.0: 300-699, 1.0: 1000-4999, 2.0: 5000-9999, 3.0: 10000+, 4.0: 700-99",
				SwingConstants.CENTER);
		label.setBounds(500, 316, 399, 20);
		getContentPane().add(label);
		
		//areas de texto
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(90, 51, 400, 251);
		getContentPane().add(scrollPane);

		txTrain = new JTextArea();
		scrollPane.setViewportView(txTrain);
		txTrain.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(500, 51, 400, 251);
		getContentPane().add(scrollPane_1);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(500, 345, 330, 109);
		getContentPane().add(scrollPane_2);

		txPred = new JTextArea();
		scrollPane_2.setViewportView(txPred);
		txPred.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		txTest = new JTextArea();
		scrollPane_1.setViewportView(txTest);
		txTest.setFont(new Font("Monospaced", Font.PLAIN, 11));


		// combos
		combClar = new JComboBox<>(clarity);
	//	combClar = new JComboBox<String>();
		combClar.setBounds(90, 380, 150, 25);
		getContentPane().add(combClar);

		combColor = new JComboBox<>(color);
	//	combColor = new JComboBox<>();
		combColor.setBounds(90, 344, 150, 25);
		getContentPane().add(combColor);

		combCut = new JComboBox<>(cut);
	//	combCut = new JComboBox<>();
		combCut.setBounds(90, 450, 150, 25);
		getContentPane().add(combCut);

		txtKil = new JTextField();
		txtKil.setBounds(90, 415, 150, 25);
		getContentPane().add(txtKil);
		txtKil.setColumns(10);

		// Botones
		btnTrain = new JButton("Train");
		btnTrain.setBounds(10, 30, 70, 50);
		btnTrain.addActionListener(this);
		getContentPane().add(btnTrain);

		btnPredecir = new JButton("Predict Price >>");
		btnPredecir.setBounds(320, 445, 150, 30);
		btnPredecir.addActionListener(this);
		getContentPane().add(btnPredecir);
		
		btnTest = new JButton("Test");
		btnTest.setBounds(10, 86, 70, 50);
		btnTest.setEnabled(false);
		btnTest.addActionListener(this);
		getContentPane().add(btnTest);
		
		setSize(925, 525);
		super.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
    
		if (e.getSource() == btnTrain) {
			System.out.println("\nTraining...");
			String p = "-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 4";
			rn.train(p, txTrain);
			btnTest.setEnabled(true);
		}

		if (e.getSource() == btnTest) {
			System.out.println("\nTesting...");
			rn.test(txTest);
		}
		
		if (e.getSource() == btnPredecir) {
	        cont++;
			String[] data = {
					txtKil.getText(),
					Objects.requireNonNull(combCut.getSelectedItem()).toString(),
					Objects.requireNonNull(combColor.getSelectedItem()).toString(),
					Objects.requireNonNull(combClar.getSelectedItem()).toString(),
					"5000-9999"}; //Fields for predect
			txPred.append(cont+")");
			rn.predict(txPred, data);
		}

	}

}
