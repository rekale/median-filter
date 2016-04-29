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
import javax.swing.filechooser.FileNameExtensionFilter;
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
	private JButton btnSave;
	private BufferedImage filteredImage;
	private JButton btnRefresh;

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

		btnSave = new JButton("save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSaveImage(e);
			}
		});
		panelControl.add(btnSave);

		btnRefresh = new JButton("refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRefresh(e);
			}
		});
		panelControl.add(btnRefresh);
	}

	protected void btnRefresh(ActionEvent e) {
		this.cleanPanelShow();
		this.btnChooseImage.setEnabled(true);
		this.btnMedian.setEnabled(true);
	}

	/**
	 * fungsi untuk milih gambar
	 * 
	 * @param evt
	 */
	private void btnChooseImage(ActionEvent evt) {
		// buka popup pilih gambar
		int result = chooser.showOpenDialog(this);

		// kalo gambarnya udah kepilih
		if (result == JFileChooser.APPROVE_OPTION) {
			// ambil file gambar yang kepilih
			File fileImg = chooser.getSelectedFile();
			try {
				// dibaca gambarnya
				img = ImageIO.read(fileImg);
				// masukin ke panel gambar awal
				this.addPanelImage(PanelImage.getInstance(img));
				// disable tombol pilih gambar
				btnChooseImage.setEnabled(false);
			} catch (IOException e) {
				// kalo ada error cetak ke log
				System.err.println("btnChooseImage : " + e);
			}

		}
	}

	/**
	 * method untuk memfilter gambar
	 * 
	 * @param e
	 */
	protected void btnMedianFilter(ActionEvent e) {

		// baca radius yang dari inputan user
		String radius = this.textRadius.getText();

		if (img == null) {
			// munculin pop up
			JOptionPane.showMessageDialog(this, "choose image first !");
		} else if (radius.isEmpty()) {
			JOptionPane.showMessageDialog(this, "fill the radius field !");
		} else {

			int size = Integer.valueOf(radius);

			// bikin objek MEdianFIlter, ntar ini objek yang bakal ngefilter
			// gambar
			MedianFilter median = new MedianFilter(size);

			// filter gambar
			this.filteredImage = median.filter(this.img);
			PanelImage panel = PanelImage.getInstance(this.filteredImage);

			// masukin hasil filter ke panel hasil
			this.addPanelImage(panel);

			this.btnMedian.setEnabled(false);
		}
	}

	/**
	 * method untuk save gambar
	 * 
	 * @param e
	 */
	private void btnSaveImage(ActionEvent e) {
		// objek untuk memunculkan popup save
		JFileChooser fileChooser = new JFileChooser();
		// set popup agar cuma bisa milih gambar yg format jpg
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg");
		fileChooser.setFileFilter(filter);

		// kalau user sudah memilih file dan gambar sudah di filter
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION && this.filteredImage != null) {

			// ambil directory yang sudah di pilih dr popup
			File directory = fileChooser.getSelectedFile();
			try {
				// save file di directory yg sudah di pilih
				ImageIO.write(this.filteredImage, "jpg", directory);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(this, "there's no filtered image");
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
