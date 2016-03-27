package process;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class Frame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFileChooser chooser;
	private BufferedImage img;
	private JPanel panelShow;
	private JPanel panelControl;
	private JButton btnChooseImage;
	private JButton btnMedian;
	private JTextField textRadius;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		setTitle("Median Filter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(547, 378);
		this.setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(5, 10));
		setContentPane(contentPane);

		chooser = new JFileChooser("D:\\gambar");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		panelShow = new JPanel();
		panelShow.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(panelShow, BorderLayout.CENTER);
		panelShow.setLayout(new GridLayout(1, 0, 0, 0));

		panelControl = new JPanel();
		panelControl.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		contentPane.add(panelControl, BorderLayout.SOUTH);
		FlowLayout fl_panelControl = new FlowLayout(FlowLayout.CENTER, 5, 5);
		panelControl.setLayout(fl_panelControl);

		btnChooseImage = new JButton("Choose Image");
		btnChooseImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnChooseImage(evt);
			}

		});

		lblNewLabel = new JLabel("Radius");
		panelControl.add(lblNewLabel);

		textRadius = new JTextField();
		panelControl.add(textRadius);
		textRadius.setColumns(2);
		panelControl.add(btnChooseImage);

		btnMedian = new JButton("Median filter");
		btnMedian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnMedianFilter(e);
			}
		});
		panelControl.add(btnMedian);
	}

	private void btnChooseImage(ActionEvent evt) {
		// TODO Auto-generated method stub
		int result = chooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {

			File fileImg = chooser.getSelectedFile();
			try {
				img = ImageIO.read(fileImg);
				this.addPanelImage(PanelImage.getInstance(img));
				btnChooseImage.setEnabled(false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("btnChooseImage : " + e);
			}

		}
	}

	protected void btnMedianFilter(ActionEvent e) {
		// TODO Auto-generated method stub
		if (!(img == null)) {

			int size = Integer.valueOf(this.textRadius.getText());

			MedianFilter median = new MedianFilter(size);

			PanelImage panel = PanelImage.getInstance(median.filter(this.img));

			this.addPanelImage(panel);

		} else {

			JOptionPane.showMessageDialog(this, "choose image first !");

		}
	}

	private void addPanelImage(PanelImage panel) {

		if (panelImageIsFull()) {

			this.cleanPanelShow();
			btnChooseImage.setEnabled(true);
			JOptionPane.showMessageDialog(this, "image in panel is full! clean it up");

		} else {

			panelShow.add(panel);
			this.revalidate();

		}

	}

	private boolean panelImageIsFull() {
		if (PanelImage.isFull()) {
			return true;
		} else
			return false;
	}

	private void cleanPanelShow() {

		PanelImage.reset();
		this.panelShow.removeAll();
		this.panelShow.setLayout(new GridLayout(1, 0, 0, 0));
		this.panelShow.repaint();
		this.img = null;

	}

}
