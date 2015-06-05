import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JTextPane;

public class MapInterface extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	private NetworkDraw jContentPane = null;
	private NeuralNetwork nw = null; 
	private JMenuBar jJMenuBar = null;
	private JMenu jMenu = null;
	private JMenuItem jMenuItem = null;
	private JMenuItem jMenuItem1 = null;
	private JMenuItem jMenuItem2 = null;
	private JMenuItem jMenuItem3 = null;
	private JMenuItem jMenuItem31 = null;
	private JMenu jMenu1 = null;
	private JMenuItem jMenuItem311 = null;
	private JMenuItem jMenuItem32 = null;
	private JMenuItem jMenuItem21 = null;
	private JMenuItem jMenuItem4 = null;
	private JMenuItem jMenuItem11 = null;
	private JTabbedPane jTabbedPane = null;
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JLabel jLabel6 = null;
	private JTextField jTextField = null;
	private JTextField jTextField1 = null;
	private JTextField jTextField2 = null;
	private JTextField jTextField3 = null;
	private JTextField jTextField4 = null;
	private JTextField jTextField5 = null;
	private JTextField jTextField6 = null;
	private Thread t;
	private JButton jButton = null;
	private JMenu arret_algo = null;
	private JMenuItem jMenuItem5 = null;
	private JMenuItem jMenuItem6 = null;
	private JPanel jPanel11 = null;
	private JLabel jLabel7 = null;
	private JLabel jLabel8 = null;
	static JTextPane jTextPane_Theta1 = null;
	static JTextPane jTextPane_Theta2 = null;
	private JLabel jLabel9 = null;
	private JTextPane jTextPane_XClicked = null;
	private JLabel jLabel10 = null;
	private JTextPane jTextPane_YClicked = null;
	public static double theta1;
	public static double theta2;
	private JMenuItem jMenuItem7 = null;
	private JMenuItem jMenuItem8 = null;
    private int echelle = 500;
	private JMenuItem jMenuItem9;
	private JMenuItem jMenuItem10;
	
	/**
	 * This is the default constructor
	 */
	public MapInterface() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
	    nw = new NeuralNetwork(15,15);//new NeuralNetwork(2,2);
		this.setSize(520, 600);
		this.setContentPane(getJTabbedPane());
		this.setJMenuBar(getJJMenuBar());
		this.setTitle("Neural Network");
		nw.addObserver(this);
		
	}
	
	/*
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(4);
			borderLayout.setVgap(2);
			jContentPane = new NetworkDraw(nw, echelle);
			jContentPane.setLayout(borderLayout);
			jContentPane.add(getJPanel11(), BorderLayout.SOUTH);
			jContentPane.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if(t!=null){
						if(!t.isAlive() && nw.isRobot()){
							jTextPane_XClicked.setText(""+e.getX());
							jTextPane_YClicked.setText(""+e.getY());
							jContentPane.drawArm(e.getX(),e.getY());
						}
					}
				}
			});
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJMenu());
			jJMenuBar.add(getJMenu1());
			jJMenuBar.add(getArret_algo());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setText("SOM");
			jMenu.add(getJMenuItem());
			jMenu.add(getJMenuItem1());
			jMenu.add(getJMenuItem2());
			jMenu.add(getJMenuItem3());
			jMenu.add(getJMenuItem31());
			jMenu.add(getJMenuItem10());
		}
		return jMenu;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem();
			jMenuItem.setText("Robot");
			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jPanel11.setVisible(true);
					nw.setOn(false);
					nw.setData('R');
					nw.setCompar(false);
					nw.setMode(true);
					nw.setOn(true);
					t = new Thread(nw);
					t.start();
					//nw.learn(true,false);									
				}
			});
		}
		return jMenuItem;
	}

	/**
	 * This method initializes jMenuItem1	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem1() {
		if (jMenuItem1 == null) {
			jMenuItem1 = new JMenuItem();
			jMenuItem1.setText("Lune");
			jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jPanel11.setVisible(false);
					nw.setOn(false);
					nw.setData('L');
					nw.setCompar(false);
					nw.setMode(true);
					nw.setOn(true);
					t = new Thread(nw);
					t.start();
					//nw.learn(true,false);		
				}
			});
		}
		return jMenuItem1;
	}

	/**
	 * This method initializes jMenuItem2	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem2() {
		if (jMenuItem2 == null) {
			jMenuItem2 = new JMenuItem();
			jMenuItem2.setText("Anneau");
			jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jPanel11.setVisible(false);
					nw.setOn(false);
					nw.setData('A');
					nw.setCompar(false);
					nw.setMode(true);
					nw.setOn(true);
					t = new Thread(nw);
					t.start();
					//nw.learn(true,false);							
				}
			});
		}
		return jMenuItem2;
	}

	/**
	 * This method initializes jMenuItem3	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem3() {
		if (jMenuItem3 == null) {
			jMenuItem3 = new JMenuItem();
			jMenuItem3.setText("Cercle");
			jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jPanel11.setVisible(false);
					nw.setOn(false);
					nw.setData('c');
					nw.setCompar(false);
					nw.setMode(true);
					nw.setOn(true);
					t = new Thread(nw);
					t.start();
					//nw.learn(true,false);											
				}
			});
		}
		return jMenuItem3;
	}

	/**
	 * This method initializes jMenuItem31	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem31() {
		if (jMenuItem31 == null) {
			jMenuItem31 = new JMenuItem();
			jMenuItem31.setText("Random");
			jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
						jPanel11.setVisible(false);
						nw.setOn(false);
						nw.setData('x');
						nw.setCompar(true);
						nw.setMode(true);
						nw.setOn(true);
						t = new Thread(nw);
						t.start();
						//nw.learn(true,false);					
				}
			});
		}
		return jMenuItem31;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		jContentPane.paintImmediately(0, 0, jContentPane.getWidth(), jContentPane.getHeight());		
	}

	/**
	 * This method initializes jMenu1	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu1() {
		if (jMenu1 == null) {
			jMenu1 = new JMenu();
			jMenu1.setText("DSOM");
			jMenu1.add(getJMenuItem4());
			jMenu1.add(getJMenuItem11());
			jMenu1.add(getJMenuItem21());
			jMenu1.add(getJMenuItem32());
			jMenu1.add(getJMenuItem311());
			jMenu1.add(getJMenuItem7());
			jMenu1.add(getJMenuItem8());
			jMenu1.add(getJMenuItem9());
		}
		return jMenu1;
	}

	/**
	 * This method initializes jMenuItem311	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem311() {
		if (jMenuItem311 == null) {
			jMenuItem311 = new JMenuItem();
			jMenuItem311.setText("Random");
			jMenuItem311.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jPanel11.setVisible(false);
					nw.setOn(false);
					nw.setData('x');
					nw.setCompar(true);
					nw.setMode(false);
					nw.setOn(true);
					t = new Thread(nw);
					t.start();
				}
			});
		}
		return jMenuItem311;
	}

	/**
	 * This method initializes jMenuItem32	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem32() {
		if (jMenuItem32 == null) {
			jMenuItem32 = new JMenuItem();
			jMenuItem32.setText("Cercle");
			jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jPanel11.setVisible(false);
					nw.setOn(false);
					nw.setData('c');
					nw.setCompar(false);
					nw.setMode(false);
					nw.setOn(true);
					t = new Thread(nw);
					t.start();
					//nw.learn(true,false);				
				}
			});
		}
		return jMenuItem32;
	}

	/**
	 * This method initializes jMenuItem21	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem21() {
		if (jMenuItem21 == null) {
			jMenuItem21 = new JMenuItem();
			jMenuItem21.setText("Anneau");
			jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jPanel11.setVisible(false);
					nw.setOn(false);
					nw.setData('A');
					nw.setCompar(false);
					nw.setMode(false);
					nw.setOn(true);
					t = new Thread(nw);
					t.start();
					//nw.learn(true,false);		
				}
			});
		}
		return jMenuItem21;
	}

	/**
	 * This method initializes jMenuItem4	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem4() {
		if (jMenuItem4 == null) {
			jMenuItem4 = new JMenuItem();
			jMenuItem4.setText("Robot");
			jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {	
					jPanel11.setVisible(true);
					nw.setOn(false);
					nw.setData('R');
					nw.setCompar(false);
					nw.setMode(false);
					nw.setOn(true);
					t = new Thread(nw);
					t.start();
					//nw.learn(true,false);		
				}
			});
		}
		return jMenuItem4;
	}

	/**
	 * This method initializes jMenuItem11	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem11() {
		if (jMenuItem11 == null) {
			jMenuItem11 = new JMenuItem();
			jMenuItem11.setText("Lune");
			jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jPanel11.setVisible(false);
					nw.setOn(false);
					nw.setData('L');
					nw.setCompar(false);
					nw.setMode(false);
					nw.setOn(true);
					t = new Thread(nw);
					t.start();
					//nw.learn(true,false);		
				}
			});
		}
		return jMenuItem11;
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab(null, null, getJContentPane(), null);
			jTabbedPane.addTab(null, null, getJPanel(), null);
			jTabbedPane.setTitleAt(0, "visualisation");
			jTabbedPane.setTitleAt(1, "parametres");
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel6 = new JLabel();
			jLabel6.setBounds(new Rectangle(286, 46, 90, 15));
			jLabel6.setText("Sigma_final");
			jLabel5 = new JLabel();
			jLabel5.setBounds(new Rectangle(286, 24, 90, 15));
			jLabel5.setText("Epsilon_final");
			jLabel4 = new JLabel();
			jLabel4.setBounds(new Rectangle(14, 107, 62, 15));
			jLabel4.setText("Max_iter");
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(14, 84, 66, 15));
			jLabel3.setText("Elasticity");
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(14, 63, 61, 15));
			jLabel2.setText("Nb_data");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(14, 43, 75, 15));
			jLabel1.setText("Sigma_init");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(14, 20, 84, 15));
			jLabel.setText("Epsilon_init");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel, null);
			jPanel.add(jLabel1, null);
			jPanel.add(jLabel2, null);
			jPanel.add(jLabel3, null);
			jPanel.add(jLabel4, null);
			jPanel.add(jLabel5, null);
			jPanel.add(jLabel6, null);
			jPanel.add(getJTextField(), null);
			jPanel.add(getJTextField1(), null);
			jPanel.add(getJTextField2(), null);
			jPanel.add(getJTextField3(), null);
			jPanel.add(getJTextField4(), null);
			jPanel.add(getJTextField5(), null);
			jPanel.add(getJTextField6(), null);
			jPanel.add(getJButton(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setBounds(new Rectangle(112, 17, 50, 20));
			jTextField.setText(""+nw.getE_init());
		}
		return jTextField;
	}

	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField1() {
		if (jTextField1 == null) {
			jTextField1 = new JTextField();
			jTextField1.setBounds(new Rectangle(112, 41, 50, 20));
			jTextField1.setText(""+nw.getSigma_init());
		}
		return jTextField1;
	}

	/**
	 * This method initializes jTextField2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField2() {
		if (jTextField2 == null) {
			jTextField2 = new JTextField();
			jTextField2.setBounds(new Rectangle(112, 85, 50, 20));
			jTextField2.setText(""+nw.getElasticity());
		}
		return jTextField2;
	}

	/**
	 * This method initializes jTextField3	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField3() {
		if (jTextField3 == null) {
			jTextField3 = new JTextField();
			jTextField3.setBounds(new Rectangle(112, 63, 50, 20));
			jTextField3.setText(""+nw.getNb_data());
		}
		return jTextField3;
	}

	/**
	 * This method initializes jTextField4	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField4() {
		if (jTextField4 == null) {
			jTextField4 = new JTextField();
			jTextField4.setBounds(new Rectangle(112, 107, 50, 20));
			jTextField4.setText(""+nw.getMax_iter());
		}
		return jTextField4;
	}

	/**
	 * This method initializes jTextField5	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField5() {
		if (jTextField5 == null) {
			jTextField5 = new JTextField();
			jTextField5.setBounds(new Rectangle(400, 22, 50, 20));
			jTextField5.setText(""+nw.getE_final());
		}
		return jTextField5;
	}

	/**
	 * This method initializes jTextField6	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField6() {
		if (jTextField6 == null) {
			jTextField6 = new JTextField();
			jTextField6.setBounds(new Rectangle(400, 46, 50, 20));
			jTextField6.setText(""+nw.getSigma_final());
		}
		return jTextField6;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(12, 157, 114, 26));
			jButton.setText("Valider");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int max_iter = new Integer(jTextField4.getText());
					int nb_data = new Integer(jTextField3.getText()); //nombre de données
					double e_init = new Float(jTextField.getText()); // U pour le vainqueur
					double e_final = new Float(jTextField5.getText()); // U pour le vainqueur
					double sigma_init = new Float(jTextField1.getText()); // B pour les voisins du vainqueur
					double sigma_final = new Float(jTextField6.getText());
					double elasticity = new Float(jTextField2.getText());
					nw.setNb_data(nb_data);
					nw.setE_final(e_final);
					nw.setE_init(e_init);
					nw.setSigma_final(sigma_final);
					nw.setSigma_init(sigma_init);
					nw.setElasticity(elasticity);
					nw.setMax_iter(max_iter);
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes arret_algo	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getArret_algo() {
		if (arret_algo == null) {
			arret_algo = new JMenu();
			arret_algo.setText("Off");
			arret_algo.add(getJMenuItem5());
			arret_algo.add(getJMenuItem6());
		}
		return arret_algo;
	}

	/**
	 * This method initializes jMenuItem5	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem5() {
		if (jMenuItem5 == null) {
			jMenuItem5 = new JMenuItem();
			jMenuItem5.setText("On");
			jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					nw.setOn(true);
				}
			});
		}
		return jMenuItem5;
	}

	/**
	 * This method initializes jMenuItem6	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem6() {
		if (jMenuItem6 == null) {
			jMenuItem6 = new JMenuItem();
			jMenuItem6.setText("Off");
			jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					nw.setOn(false);
				}
			});
		}
		return jMenuItem6;
	}

	/**
	 * This method initializes jPanel11	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel11() {
		if (jPanel11 == null) {
			jLabel10 = new JLabel();
			jLabel10.setText("Y Clicked");
			jLabel9 = new JLabel();
			jLabel9.setText("X Clicked");
			jLabel8 = new JLabel();
			jLabel8.setText("Theta2");
			jLabel7 = new JLabel();
			jLabel7.setBounds(new Rectangle(5, 5, 44, 15));
			jLabel7.setText("Theta1");
			jPanel11 = new JPanel();
			jPanel11.add(jLabel7, null);
			jPanel11.add(getJTextPane_Theta1(), null);
			jPanel11.add(jLabel8, null);
			jPanel11.add(getJTextPane_Theta2(), null);
			if(!nw.isRobot()){
				jPanel11.setVisible(false);
				jPanel11.add(jLabel9, null);
				jPanel11.add(getJTextPane_XClicked(), null);
				jPanel11.add(jLabel10, null);
				jPanel11.add(getJTextPane_YClicked(), null);
			}
		}
		return jPanel11;
	}

	/**
	 * This method initializes jTextPane_Theta1	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getJTextPane_Theta1() {
		if (jTextPane_Theta1 == null) {
			jTextPane_Theta1 = new JTextPane();
			jTextPane_Theta1.setText("0.0");
		}
		return jTextPane_Theta1;
	}

	/**
	 * This method initializes jTextPane_Theta2	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getJTextPane_Theta2() {
		if (jTextPane_Theta2 == null) {
			jTextPane_Theta2 = new JTextPane();
			jTextPane_Theta2.setText("0.0");
		}
		return jTextPane_Theta2;
	}

	/**
	 * This method initializes jTextPane_XClicked	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getJTextPane_XClicked() {
		if (jTextPane_XClicked == null) {
			jTextPane_XClicked = new JTextPane();
			jTextPane_XClicked.setText("0.0");
		}
		return jTextPane_XClicked;
	}

	/**
	 * This method initializes jTextPane_YClicked	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getJTextPane_YClicked() {
		if (jTextPane_YClicked == null) {
			jTextPane_YClicked = new JTextPane();
			jTextPane_YClicked.setText("0.0");
		}
		return jTextPane_YClicked;
	}

	/**
	 * This method initializes jMenuItem7	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem7() {
		if (jMenuItem7 == null) {
			jMenuItem7 = new JMenuItem();
			jMenuItem7.setText("bras motor");
			jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jPanel11.setVisible(false);
					nw.setOn(false);
					nw.setData('S');
					nw.setParcours('M');
					nw.setCompar(false);
					nw.setMode(false);
					nw.setOn(true);
					nw.setRobot_move(true);
					t = new Thread(nw);
					t.start();
				}
			    });
		}
		return jMenuItem7;
	}
	
    private JMenuItem getJMenuItem8() {
	if (jMenuItem8 == null) {
	    jMenuItem8 = new JMenuItem();
	    jMenuItem8.setText("bras retine");
	    jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
			jPanel11.setVisible(false);
			nw.setOn(false);
			nw.setData('S');
			nw.setParcours('R');
			nw.setCompar(false);
			nw.setMode(false);
			nw.setOn(true);
			nw.setRobot_move(true);
			t = new Thread(nw);
			t.start();
		    }
		});
	}
	return jMenuItem8;
	}

	/**
	 * This method initializes jMenuItem8	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem9() {
		if (jMenuItem9 == null) {
			jMenuItem9 = new JMenuItem();
			jMenuItem9.setText("Robot_interet");
			jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jPanel11.setVisible(true);
					nw.setOn(false);
					nw.setData('r');
					nw.setCompar(false);
					nw.setMode(false);
					nw.setOn(true);
					t = new Thread(nw);
					t.start();
					//nw.learn(true,false);	
				}
			});
		}
		return jMenuItem9;
	}

	/**
	 * This method initializes jMenuItem8	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem10() {
		if (jMenuItem10 == null) {
			jMenuItem10 = new JMenuItem();
			jMenuItem10.setText("Robot_interet");
			jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jPanel11.setVisible(true);
					nw.setOn(false);
					nw.setData('r');
					nw.setCompar(false);
					nw.setMode(true);
					nw.setOn(true);
					t = new Thread(nw);
					t.start();
					//nw.learn(true,false);	
				}
			});
		}
		return jMenuItem10;
	}
}  //  @jve:decl-index=0:visual-constraint="30,-2"

