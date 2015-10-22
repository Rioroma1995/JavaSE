package ATM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class GUI {
	private static volatile GUI instance;
	private static MyFrame ATMwindow;

	private JPanel panel;
	private JTextArea mainDisplay;
	private JTextArea printDisplay;
	private JPasswordField passwordField;
	private JTextField chooseCardField;

	private JButton b1;
	private JButton b2;
	private JButton b3;
	private JButton b4;
	private JButton b5;
	private JButton b6;
	private JButton b7;
	private JButton b8;
	private JButton b9;
	private JButton b0;
	private JButton cancel;
	private JButton correction;
	private JButton enter;
	private JButton exit;
	private JButton inputCard;

	private static JLabel chooseCardLabel = new JLabel();
	private JButton yesPrint;
	private JButton noPrint;

	private static final int h = 670;
	private static final int w = 650;

	private final Color frameColor = new Color(187, 224, 208);
	private final Color displaysColor = new Color(241, 230, 203);
	private final Color exitButtonColor = new Color(162, 205, 90);
	private final Color menuButtonColor = new Color(152, 251, 152);
	private final Color passwordFieldColor = new Color(243, 222, 196);
	private static final Color labelForeground = chooseCardLabel.getForeground();
	private final ActionListener BPressed = new KeyboardListener();

	private static final String welcome = "\n\n\n                   Welcome\n "+ "         to the ATM Simulator";
	private static final String idle = "";

	private JButton m1;
	private JButton m2;
	private JButton m3;
	private JButton m4;
	private JButton m5;

	private JButton OK1;
	private JButton OK2;
	private JButton OK3;

	private byte guess = 0;
	private JPasswordField oldPwdField;
	private JPasswordField newPwdField1;
	private JPasswordField newPwdField2;
	private String oldPwd;
	private String newPwd1;
	private String newPwd2;

	private Label oldPass; 
	private Label newPass;
	private Label confirmPass;

	public static GUI getInstance() {
		GUI localInstance = instance;
		if (localInstance == null) {
			synchronized (GUI.class) {
				localInstance = instance;
				if (localInstance == null)
					instance = localInstance = new GUI();
			}
		}
		return localInstance;
	}

	private GUI() {
		ATMwindow = new MyFrame();
		ATMwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ATMwindow.setVisible(true);
		initMoney();
	}

	private void initMoney() {
		boolean init = false;
		Integer num = 0;
		String money = null;
		while (init == false) {
			money = JOptionPane.showInputDialog(ATMwindow,"Введіть кількість купюр по 10 грн. (максимум: 5000)","Валюта", 1);
			if (money == null || money.equals("")) {
				JOptionPane.showMessageDialog(ATMwindow,
						"Ви не ввели кількість готівки", "Помилка",
						JOptionPane.ERROR_MESSAGE);
				int c = JOptionPane.showConfirmDialog(ATMwindow,
						"Завершити роботу?");
				if (c == 0) {
					ATMwindow.dispose();
					break;
				}
			} else {
				try {
					num = Integer.parseInt(money);
					if (num <= 0)
						JOptionPane.showMessageDialog(ATMwindow,
								"Ви ввели некоректне число", "Помилка",
								JOptionPane.ERROR_MESSAGE);
					else if (num > 5000)
						JOptionPane.showMessageDialog(ATMwindow,
								"Ви ввели занадто велике число", "Помилка",
								JOptionPane.ERROR_MESSAGE);
					else 
						init = true;
				} catch (Exception e) {
					JOptionPane.showMessageDialog(ATMwindow,
							"Ви не ввели кількість готівки", "Помилка",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		ATM.setMoneyCount10(num);
		ATM.setTotalCash(10 * num);
	}

	private class MyFrame extends JFrame {
		private static final long serialVersionUID = 1L;

		public MyFrame() {
			setTitle("ATM Simulator");
			setSize(h, w);
			setLocation(5, 5);
			setResizable(false);
			Container c = getContentPane();
			panel = new MyPanel();
			panel.setBackground(frameColor);
			c.add(panel);

		}
	}

	private class MyPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private MyPanel() { // ініціалізуємо всі елементи: клавіатура, екран ...
			setLayout(null);
			addPasswordField();
			addMainDisplay();
			addPrintDisplay();
			addKeyboard();
			addReadCard();
			initSettingElements();
			initFirstMenu();
			chooseCard(); // вибираємо карту для початку роботи з АТМ
		}

		private void addMainDisplay() {
			mainDisplay = new JTextArea(welcome);
			mainDisplay.setLocation(10, 10);
			mainDisplay.setFont(new Font("Century Gothic", Font.BOLD, 20));
			mainDisplay.setSize(400, 300);
			mainDisplay.setLineWrap(true);
			mainDisplay.setEditable(false);
			mainDisplay.setBackground(displaysColor);
			add(mainDisplay);
		}

		private void addPasswordField() {
			passwordField = new JPasswordField();
			passwordField.setEchoChar('*');
			passwordField.setSize(100, 50);
			passwordField.setLocation(150, 130);
			passwordField.setHorizontalAlignment(JPasswordField.CENTER);
			passwordField.setBackground(passwordFieldColor);
			passwordField.setEditable(false);
			passwordField.setVisible(false);
			add(passwordField);
		}

		private void addPrintDisplay() {
			printDisplay = new JTextArea(idle);
			printDisplay.setLocation(450, 10);
			printDisplay.setSize(200, 300);
			printDisplay.setEditable(false);
			printDisplay.setBackground(displaysColor);
			add(printDisplay);
		}

		private void addReadCard() {
			chooseCardField = new JTextField();
			chooseCardField.setColumns(2);
			chooseCardField.setLocation(70, 150);
			chooseCardField.setSize(280, 50);
			chooseCardField.setBackground(displaysColor);
			chooseCardField.setHorizontalAlignment(JPasswordField.CENTER);
			chooseCardField.setEditable(false);

			chooseCardLabel = new JLabel("Введіть номер картки:");
			chooseCardLabel.setSize(500, 30);
			chooseCardLabel.setFont(new Font("Dialog", Font.BOLD, 20));
			chooseCardLabel.setForeground(Color.black);
			chooseCardField.addActionListener(new KeyboardListener());

			add(chooseCardLabel);
			add(chooseCardField);
		}

		private void addKeyboard() {
			b1 = addButton("1", 50, 50, 70, 400);
			b1.addActionListener(BPressed);
			b2 = addButton("2", 50, 50, 120, 400);
			b2.addActionListener(BPressed);
			b3 = addButton("3", 50, 50, 170, 400);
			b3.addActionListener(BPressed);
			b4 = addButton("4", 50, 50, 70, 450);
			b4.addActionListener(BPressed);
			b5 = addButton("5", 50, 50, 120, 450);
			b5.addActionListener(BPressed);
			b6 = addButton("6", 50, 50, 170, 450);
			b6.addActionListener(BPressed);
			b7 = addButton("7", 50, 50, 70, 500);
			b7.addActionListener(BPressed);
			b8 = addButton("8", 50, 50, 120, 500);
			b8.addActionListener(BPressed);
			b9 = addButton("9", 50, 50, 170, 500);
			b9.addActionListener(BPressed);
			b0 = addButton("0", 50, 50, 120, 550);
			b0.addActionListener(BPressed);

			correction = addButton("CORRECTION", 120, 50, 230, 450);
			correction.addActionListener(BPressed);
			enter = addButton("ENTER", 120, 50, 230, 500);
			enter.addActionListener(new ActionListener() {
				private JFrame controllingFrame;

				@Override
				public void actionPerformed(ActionEvent event) {
					inputCard.setEnabled(true);
					if (chooseCardField.isVisible()) {
						if (ATM.enterCard(chooseCardField.getText())) {
							chooseCardField.setVisible(false);
							chooseCardLabel.setVisible(false);
							mainDisplay.setText(welcome);
							mainDisplay.setEnabled(true);
							printDisplay.setText(idle);
							setVisibleKeyboard(true);
							setEnabledKeyboard(true);
							setVisibleDisplays(true);
							chooseCardField.setText(idle);
						} else {
							chooseCardField.setText(idle);
							chooseCardLabel.setLocation(60, 80);
							chooseCardLabel.setForeground(Color.red);
							chooseCardLabel
									.setText("Неправильний номер картки, спробуйте ще раз:");
						}
					} else if (ATM.enterPIN(String.valueOf(passwordField
							.getPassword()))) {
						// correct PIN
						ATMwindow.setVisible(true);
						ATMwindow.setEnabled(true);
						cancel.setVisible(true);
						passwordField.setVisible(false);
						passwordField.setEnabled(false);
						passwordField.setText(idle);
						mainDisplay.setText(idle);
						enter.setEnabled(false);
						// show first menu
						setVisibleFirstMenu(true);
					} else {
						JOptionPane.showMessageDialog(controllingFrame,
								"Неправильний PIN", "Помилка",
								JOptionPane.ERROR_MESSAGE);
						if (guess == 2) {
							try {
								JOptionPane.showMessageDialog(controllingFrame,
										"Вашу картку заблоковано на 30 секунд",
										"Помилка", JOptionPane.ERROR_MESSAGE);
								Thread.sleep(30000);
								guess = 0;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else
							guess++;
						passwordField.setText(idle);
					}
				}

			});

			cancel = addButton("CANCEL", 120, 50, 230, 400);
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					inputCard.setEnabled(false);
					endWork();
				};
			});

			add(b1);
			add(b2);
			add(b3);
			add(cancel);
			add(b4);
			add(b5);
			add(b6);
			add(correction);
			add(b7);
			add(b8);
			add(b9);
			add(enter);
			add(b0);

			setVisibleKeyboard(false);

			exit = addButton("EXIT", 120, 50, 530, 550);
			exit.setBackground(exitButtonColor);
			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					ATMwindow.dispose();
				}
			});

			inputCard = addButton("Вставити картку", 150, 50, 70, 340);
			inputCard.setBackground(menuButtonColor);
			inputCard.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					setEnabledKeyboard(true);
					inputPIN();
				}
			});
			add(exit);
			add(inputCard);
		}
	}

	private void endWork() {
		passwordField.setVisible(false);
		mainDisplay.setText(idle);
		final JButton Yes = new JButton("Так");
		final JButton No = new JButton("Ні");
		Yes.setLocation(10, 80);
		Yes.setSize(150, 30);
		No.setLocation(10, 120);
		No.setSize(150, 30);
		Yes.setBackground(menuButtonColor);
		No.setBackground(menuButtonColor);

		setEnabledKeyboard(false);
		setVisibleFirstMenu(false);
		OK1.setVisible(false);
		OK2.setVisible(false);
		OK3.setVisible(false);

		oldPass.setVisible(false);
		newPass.setVisible(false);
		confirmPass.setVisible(false);

		oldPwdField.setVisible(false);
		newPwdField1.setVisible(false);
		newPwdField2.setVisible(false);

		mainDisplay.setText("\n   Завершити роботу?");
		mainDisplay.add(Yes);
		mainDisplay.add(No);

		Yes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				chooseCard();
				Yes.setVisible(false);
				No.setVisible(false);
				mainDisplay.setText(idle);
				printDisplay.setText(idle);
				guess = 0;
			}
		});

		No.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setEnabledKeyboard(true);
				inputPIN();
				passwordField.setVisible(true);
				passwordField.setEnabled(true);
				No.setVisible(false);
				Yes.setVisible(false);
				printDisplay.setText(idle);
				guess = 0;
			}
		});

	}
	//друк чеку
	private void doPrint(ActionEvent ev) {
		mainDisplay.setForeground(labelForeground);
		mainDisplay.setText(idle);
		yesPrint.setVisible(false);
		noPrint.setVisible(false);
		endWork();
		String date = new java.util.Date().toString();
		String transaction = "               ATM \"Virtual\"\n"
				+ "\n ID транзакції: " + ATM.nextTransaction() + "\n Дата:\n "
				+ date + "\n Номер картки: " + ATM.getCurrentClient().getCard()
				+ "\n\n Баланс: " + ATM.getCurrentClient().getBalance()
				+ " грн";
		if (ev.getActionCommand().equals("Так")) {
			printDisplay.setText(transaction);
		}
	}

	ActionListener PrintListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			doPrint(e);
		}
	};

	private void doCash(ActionEvent e, int amnt, JButton Amount1,
			JButton Amount2, JButton Amount3, JButton Amount4,
			JButton ReturnBack, JButton OtherAmount) {
		int Amount;
		if (amnt == -1)
			Amount = Integer.parseInt(e.getActionCommand());
		else
			Amount = amnt;

		if (!ATM.doCash(Amount)) {
			mainDisplay.setText("\nНедостатньо коштів");
			mainDisplay.setForeground(Color.red);
		} else {
			yesPrint.setVisible(true);
			noPrint.setVisible(true);
			mainDisplay.setForeground(labelForeground);
			mainDisplay.setText("\n   Друкувати чек?"
					+ "\n\n\n\n\n\n\n Ваш баланс: "
					+ ATM.getCurrentClient().getBalance() + " грн");
			setVisibleCashButtons(Amount1, Amount2, Amount3, Amount4,
					ReturnBack, OtherAmount, false);
		}

	}

	private void setVisibleCashButtons(JButton b1, JButton b2, JButton b3,
			JButton b4, JButton b5, JButton b6, boolean flag) {
		b1.setVisible(flag);
		b2.setVisible(flag);
		b3.setVisible(flag);
		b4.setVisible(flag);
		b5.setVisible(flag);
		b6.setVisible(flag);
	}

	private void initFirstMenu() {
		initShowBalanceAction();
		initCashAction();
		initSendMoneyAction();
		initMobileCashAction();
		initSettingsMenu();
	}
	//тут ми міняємо пароль
	private void initSettingElements() {
		
		oldPwdField = new JPasswordField();
		oldPwdField.setEchoChar('*');
		oldPwdField.setSize(100, 50);
		oldPwdField.setLocation(200, 50);
		oldPwdField.setHorizontalAlignment(JPasswordField.CENTER);
		oldPwdField.setBackground(passwordFieldColor);
		oldPwdField.setEditable(false);
		oldPwdField.setVisible(false);

		newPwdField1 = new JPasswordField();
		newPwdField1.setEchoChar('*');
		newPwdField1.setSize(100, 50);
		newPwdField1.setLocation(200, 120);
		newPwdField1.setHorizontalAlignment(JPasswordField.CENTER);
		newPwdField1.setBackground(passwordFieldColor);
		newPwdField1.setEditable(false);
		newPwdField1.setVisible(false);
		newPwdField1.setEnabled(false);

		newPwdField2 = new JPasswordField();
		newPwdField2.setEchoChar('*');
		newPwdField2.setSize(100, 50);
		newPwdField2.setLocation(200, 190);
		newPwdField2.setHorizontalAlignment(JPasswordField.CENTER);
		newPwdField2.setBackground(passwordFieldColor);
		newPwdField2.setEditable(false);
		newPwdField2.setVisible(false);
		newPwdField2.setEnabled(false);

		oldPass = new Label();
		oldPass.setText("Старий пароль:");
		oldPass.setFont(new Font("Helvetica", 1, 16));
		oldPass.setSize(160, 20);
		oldPass.setLocation(30, 65);
		oldPass.setVisible(false);
		mainDisplay.add(oldPass);
		
		newPass = new Label();
		newPass.setText("Новий пароль:");
		newPass.setFont(new Font("Helvetica", 1, 16));
		newPass.setSize(160, 20);
		newPass.setLocation(30, 135);
		newPass.setVisible(false);
		mainDisplay.add(newPass);
		
		confirmPass = new Label();
		confirmPass.setText("Підтвердження:");
		confirmPass.setFont(new Font("Helvetica", 1, 16));
		confirmPass.setSize(160, 20);
		confirmPass.setLocation(30, 205);
		confirmPass.setVisible(false);
		mainDisplay.add(confirmPass);
		
		OK1 = new JButton("OK");
		OK1.setBackground(menuButtonColor);
		OK1.setLocation(320, 50);
		OK1.setSize(70, 50);
		OK1.setBackground(Color.lightGray);
		OK1.setVisible(false);
		mainDisplay.add(OK1);

		OK2 = new JButton("OK");
		OK2.setBackground(menuButtonColor);
		OK2.setLocation(320, 120);
		OK2.setSize(70, 50);
		OK2.setBackground(Color.lightGray);
		OK2.setVisible(false);
		OK2.setEnabled(false);
		mainDisplay.add(OK2);

		OK3 = new JButton("OK");
		OK3.setBackground(menuButtonColor);
		OK3.setLocation(320, 190);
		OK3.setSize(70, 50);
		OK3.setBackground(Color.lightGray);
		OK3.setEnabled(false);
		OK3.setVisible(false);
		mainDisplay.add(OK3);

		
		OK1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				oldPwd = oldPwdField.getText();
				oldPwdField.setEnabled(false);
				newPwdField1.setEnabled(true);
				OK1.setEnabled(false);
				OK2.setEnabled(true);
			}
		});

		OK2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newPwd1 = newPwdField1.getText();
				newPwdField1.setEnabled(false);
				newPwdField2.setEnabled(true);
				OK2.setEnabled(false);
				OK3.setEnabled(true);
			}
		});

		OK3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newPwd2 = newPwdField2.getText();
				newPwdField2.setEnabled(false);
				OK3.setEnabled(false);
				oldPwdField.setText("");
				newPwdField1.setText("");
				newPwdField2.setText("");
				if (ATM.setPassword(oldPwd, newPwd1, newPwd2)) {
					oldPwd = "";
					newPwd1 = "";
					newPwd2 = "";
					endWork();
				} else {
					OK1.setEnabled(true);
					OK2.setEnabled(false);
					OK3.setEnabled(false);

					oldPwdField.setEnabled(true);
					newPwdField1.setEnabled(false);
					newPwdField2.setEnabled(false);

				}
				oldPass.setVisible(false);
				newPass.setVisible(false);
				confirmPass.setVisible(false);
			}
		});
		mainDisplay.add(oldPwdField);
		mainDisplay.add(newPwdField1);
		mainDisplay.add(newPwdField2);
	}

	private void initSettingsMenu() {
		m5 = new JButton("5. Зміна паролю");
		m5.setHorizontalAlignment(SwingConstants.LEFT);
		m5.setFont(new Font("Century Gothic", Font.BOLD, 16));
		m5.setLocation(10, 230);
		m5.setSize(380, 30);
		m5.setBackground(Color.lightGray);
		m5.setVisible(false);
		m5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisibleFirstMenu(false);
				oldPwdField.setText("");

				oldPass.setVisible(true);
				newPass.setVisible(true);
				confirmPass.setVisible(true);
				
				oldPwdField.setVisible(true);
				newPwdField1.setVisible(true);
				newPwdField2.setVisible(true);

				OK1.setVisible(true);
				OK2.setVisible(true);
				OK3.setVisible(true);

				newPwdField1.setEnabled(false);
				newPwdField2.setEnabled(false);

				OK1.setEnabled(true);
				OK2.setEnabled(false);
				OK3.setEnabled(false);

			}
		});
		mainDisplay.add(m5);
	}

	private void initShowBalanceAction() {
		yesPrint = addMenuButton("Так", 150, 30, 10, 80, menuButtonColor);
		noPrint = addMenuButton("Ні", 150, 30, 10, 120, menuButtonColor);
		yesPrint.setVisible(false);
		noPrint.setVisible(false);

		m1 = new JButton("1. Перевірити баланс");
		m1.setName("1");
		m1.setHorizontalAlignment(SwingConstants.LEFT);
		m1.setFont(new Font("Century Gothic", Font.BOLD, 16));
		m1.setLocation(10, 30);
		m1.setSize(380, 30);
		m1.setBackground(Color.lightGray);
		m1.setVisible(false);

		yesPrint.addActionListener(PrintListener);
		noPrint.addActionListener(PrintListener);
		m1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				yesPrint.setVisible(true);
				noPrint.setVisible(true);
				setVisibleFirstMenu(false);
				setEnabledKeyboard(false);
				mainDisplay.setText("\n   Друкувати чек?"
						+ "\n\n\n\n\n\n\n Ваш баланс: "
						+ ATM.getCurrentClient().getBalance() + " грн");
			}
		});
		mainDisplay.add(m1);

	}

	private void initCashAction() {

		final JButton Amount1 = addMenuButton("100", 120, 30, 10, 80,
				menuButtonColor);
		final JButton Amount2 = addMenuButton("200", 120, 30, 10, 120,
				menuButtonColor);
		final JButton Amount3 = addMenuButton("300", 120, 30, 10, 160,
				menuButtonColor);
		final JButton Amount4 = addMenuButton("400", 120, 30, 10, 200,
				menuButtonColor);
		final JButton OtherAmount = addMenuButton("Інша сума", 120, 30, 150,
				80, menuButtonColor);
		final JButton ReturnBack = addMenuButton("Головне меню", 120, 30, 150,
				120, menuButtonColor);

		Amount1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doCash(e, -1, Amount1, Amount2, Amount3, Amount4, ReturnBack,
						OtherAmount);
			}
		});
		Amount2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doCash(e, -1, Amount1, Amount2, Amount3, Amount4, ReturnBack,
						OtherAmount);
			}
		});
		Amount3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doCash(e, -1, Amount1, Amount2, Amount3, Amount4, ReturnBack,
						OtherAmount);
			}
		});
		Amount4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doCash(e, -1, Amount1, Amount2, Amount3, Amount4, ReturnBack,
						OtherAmount);
			}
		});
		ReturnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisibleFirstMenu(true);
				mainDisplay.setText(idle);
				mainDisplay.setForeground(labelForeground);
				setEnabledKeyboard(true);
				enter.setEnabled(false);
				setVisibleCashButtons(OtherAmount, Amount1, Amount2, Amount3,
						Amount4, ReturnBack, false);
			}
		});

		OtherAmount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = JOptionPane.showInputDialog(null,
						"Введіть число кратне 10: ", "Інша сума", 1);
				if (str == null) {
					JOptionPane.showMessageDialog(null, "Некоректне число",
							"Помилка", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						int amount = Integer.parseInt(str);
						if (amount > ATM.getTotalCash())
							JOptionPane.showMessageDialog(null,
									"Перевищений ліміт", "Помилка",
									JOptionPane.ERROR_MESSAGE);
						else if (amount % 10 == 0 && amount >= 10)
							doCash(e, amount, Amount1, Amount2, Amount3,
									Amount4, ReturnBack, OtherAmount);
						else
							JOptionPane.showMessageDialog(null,
									"Некоректне число", "Помилка",
									JOptionPane.ERROR_MESSAGE);
					} catch (NumberFormatException error) {
						JOptionPane.showMessageDialog(null, "Некоректне число",
								"Помилка", JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		});
		Amount1.setVisible(false);
		Amount2.setVisible(false);
		Amount3.setVisible(false);
		Amount4.setVisible(false);
		OtherAmount.setVisible(false);
		ReturnBack.setVisible(false);
		mainDisplay.add(Amount1);
		mainDisplay.add(Amount2);
		mainDisplay.add(Amount3);
		mainDisplay.add(Amount4);
		mainDisplay.add(OtherAmount);
		mainDisplay.add(ReturnBack);

		m2 = new JButton("2. Зняти готівку");
		m2.setHorizontalAlignment(SwingConstants.LEFT);
		m2.setFont(new Font("Century Gothic", Font.BOLD, 16));
		m2.setLocation(10, 80);
		m2.setSize(380, 30);
		m2.setBackground(Color.lightGray);
		m2.setVisible(false);
		m2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setEnabledKeyboard(true);
				Amount1.setVisible(true);
				Amount2.setVisible(true);
				Amount3.setVisible(true);
				Amount4.setVisible(true);
				Amount1.setText("20");
				Amount2.setText("50");
				Amount3.setText("100");
				Amount4.setText("200");
				OtherAmount.setVisible(true);
				ReturnBack.setVisible(true);
				setVisibleFirstMenu(false);
				setEnabledKeyboard(false);
				enter.setEnabled(false);
				mainDisplay.setText(" Ваш баланс: "
						+ ATM.getCurrentClient().getBalance() + " грн"
						+ "\n Виберіть суму");
			}
		});
		mainDisplay.add(m2);
	}

	private void initSendMoneyAction() {
		final JButton Amount = addMenuButton("Вибрати суму", 120, 30, 100, 80,
				menuButtonColor);
		final JButton Return = addMenuButton("Головне меню", 120, 30, 100, 120,
				menuButtonColor);
		Return.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisibleFirstMenu(true);
				mainDisplay.setText(idle);
				mainDisplay.setForeground(labelForeground);
				setEnabledKeyboard(true);
				Return.setVisible(false);
				Amount.setVisible(false);
				enter.setEnabled(false);
			}
		});

		Amount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = JOptionPane.showInputDialog(ATMwindow,
						"Введіть суму: ", "Переказ коштів", 1);

				if (str == null) {
					JOptionPane.showMessageDialog(ATMwindow,
							"Некоректне число", "Помилка",
							JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						int amount = Integer.parseInt(str);
						if (inputCardToSend(amount)) {
							Amount.setVisible(false);
							Return.setVisible(false);
							yesPrint.setVisible(true);
							noPrint.setVisible(true);
							mainDisplay.setForeground(labelForeground);
							mainDisplay.setText("\n   Друкувати чек?"
									+ "\n\n\n\n\n\n\n Ваш баланс: "
									+ ATM.getCurrentClient().getBalance()
									+ " грн");
						}
					} catch (NumberFormatException error) {
						JOptionPane.showMessageDialog(ATMwindow,
								"Некоректне число", "Помилка",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		});
		m3 = new JButton("3. Перевести кошти на інший рахунок");
		m3.setHorizontalAlignment(SwingConstants.LEFT);
		m3.setFont(new Font("Century Gothic", Font.BOLD, 16));
		m3.setLocation(10, 130);
		m3.setSize(380, 30);
		m3.setBackground(Color.lightGray);
		m3.setVisible(false);
		m3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setEnabledKeyboard(true);
				Return.setVisible(true);
				Amount.setVisible(true);
				setVisibleFirstMenu(false);
				setEnabledKeyboard(false);
				mainDisplay.setText(" Ваш баланс: "
						+ ATM.getCurrentClient().getBalance() + " грн"
						+ "\n Виберіть суму");
			}
		});
		mainDisplay.add(m2);
		mainDisplay.add(m3);
		Return.setVisible(false);
		Amount.setVisible(false);
	}

	private boolean inputCardToSend(int amnt) {
		boolean check = false;
		String cardnumber = JOptionPane.showInputDialog(ATMwindow,
				"Введіть номер карти отримувача: ", "Переказ коштів", 1);
		if (cardnumber == null) {
			JOptionPane.showMessageDialog(ATMwindow, "Некоректне число",
					"Помилка", JOptionPane.ERROR_MESSAGE);
		} else {
			try {
				Integer.parseInt(cardnumber);
				if (ATM.ProcessSend(ATM.getCurrentClient().getCard(),
						cardnumber, amnt)) {
					check = true;
				}
			} catch (NumberFormatException error) {
				JOptionPane.showMessageDialog(ATMwindow, "Некоректне число",
						"Помилка", JOptionPane.ERROR_MESSAGE);
			}
		}
		return check;
	}

	private boolean inputMobileNumber(int amnt) {
		boolean check = false;
		String number = JOptionPane.showInputDialog(ATMwindow,
				"Номер мобільного: +380", "Поповнення мобільного", 1);
		if (number == null || number.length() != 9) {
			JOptionPane.showMessageDialog(ATMwindow, "Некоректний номер",
					"Помилка", JOptionPane.ERROR_MESSAGE);
		} else {
			try {
				Integer.parseInt(number);
				if (ATM.MobileFillUp(amnt))
					check = true;
			} catch (NumberFormatException error) {
				JOptionPane.showMessageDialog(ATMwindow, "Некоректний номер",
						"Помилка", JOptionPane.ERROR_MESSAGE);
			}
		}
		return check;
	}

	private void initMobileCashAction() {

		final JButton Amount = addMenuButton("Вибрати суму", 120, 30, 100, 80,
				menuButtonColor);
		final JButton Return = addMenuButton("Головне меню", 120, 30, 100, 120,
				menuButtonColor);
		Return.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisibleFirstMenu(true);
				mainDisplay.setText(idle);
				mainDisplay.setForeground(labelForeground);
				setEnabledKeyboard(true);
				Return.setVisible(false);
				Amount.setVisible(false);
				enter.setEnabled(false);
			}
		});

		Amount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = JOptionPane.showInputDialog(ATMwindow,
						"Введіть суму(не менше 5 грн): ",
						"Поповнення мобільного", 1);
				int amount = 0;
				if (str == null) {
					JOptionPane.showMessageDialog(ATMwindow,
							"Некоректне число", "Помилка",
							JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						amount = Integer.parseInt(str);
						if (amount < 5)
							JOptionPane.showMessageDialog(ATMwindow,
									"Некоректне число", "Помилка",
									JOptionPane.ERROR_MESSAGE);
						else if (inputMobileNumber(amount)) {
							Amount.setVisible(false);
							Return.setVisible(false);
							yesPrint.setVisible(true);
							noPrint.setVisible(true);
							mainDisplay.setForeground(labelForeground);
							mainDisplay.setText("\n   Друкувати чек?"
									+ "\n\n\n\n\n\n Мобільний поповнено на: "
									+ String.valueOf(amount) + " грн"
									+ "\n Ваш баланс: "
									+ ATM.getCurrentClient().getBalance()
									+ " грн");
						}
					} catch (NumberFormatException error) {
						JOptionPane.showMessageDialog(ATMwindow,
								"Некоректне число", "Помилка",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		m4 = new JButton("4. Поповнити мобільний");
		m4.setHorizontalAlignment(SwingConstants.LEFT);
		m4.setLocation(10, 180);
		m4.setFont(new Font("Century Gothic", Font.BOLD, 16));
		m4.setSize(380, 30);
		m4.setBackground(Color.lightGray);
		m4.setVisible(false);
		m4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setEnabledKeyboard(true);
				Return.setVisible(true);
				Amount.setVisible(true);
				setVisibleFirstMenu(false);
				setEnabledKeyboard(false);
				mainDisplay.setText(" Ваш баланс: "
						+ ATM.getCurrentClient().getBalance() + " грн"
						+ "\n Виберіть суму");
			}
		});
		mainDisplay.add(m4);
		Return.setVisible(false);
		Amount.setVisible(false);
	}

	private void inputPIN() {
		setEnabledKeyboard(true);
		setVisibleKeyboard(true);
		mainDisplay.setText(idle);

		mainDisplay.setEnabled(true);
		inputCard.setVisible(false);
		mainDisplay.setFont(new Font("Century Gothic", Font.BOLD, 20));
		mainDisplay.setText("\n\n\n                  Введіть PIN: ");
		passwordField.setVisible(true);
		passwordField.setEnabled(true);
		passwordField.setText(idle);
	}

	// фабрична функція для створення кнопки
	private final JButton addButton(String lable, int sizeX, int sizeY, int x,
			int y) {
		JButton button = new JButton(lable);
		button.setSize(sizeX, sizeY);
		button.setLocation(x, y);
		return button;
	}

	private final JButton addMenuButton(String lable, int sizeX, int sizeY,
			int x, int y, Color color) {
		JButton button = new JButton(lable);
		button.setSize(sizeX, sizeY);
		button.setLocation(x, y);
		button.setBackground(color);
		mainDisplay.add(button);
		return button;
	}

	private void readCardNumber() {
		chooseCardField.setVisible(true);
		chooseCardLabel.setVisible(true);
	}

	private void chooseCard() {
		mainDisplay.setVisible(false);
		printDisplay.setVisible(false);
		setVisibleKeyboard(true);
		setEnabledKeyboard(true);
		inputCard.setVisible(false);
		cancel.setEnabled(false);
		passwordField.setVisible(false);
		readCardNumber();
		chooseCardLabel.setText("Введіть номер картки:");
		chooseCardLabel.setForeground(Color.black);
		chooseCardLabel.setLocation(100, 60);

	}

	private void setVisibleFirstMenu(boolean flag) {
		m1.setVisible(flag);
		m2.setVisible(flag);
		m3.setVisible(flag);
		m4.setVisible(flag);
		m5.setVisible(flag);
	}

	private void setVisibleKeyboard(boolean flag) {
		b1.setVisible(flag);
		b2.setVisible(flag);
		b3.setVisible(flag);
		b4.setVisible(flag);
		b5.setVisible(flag);
		b6.setVisible(flag);
		b7.setVisible(flag);
		b8.setVisible(flag);
		b9.setVisible(flag);
		b0.setVisible(flag);
		cancel.setVisible(flag);
		correction.setVisible(flag);
		enter.setVisible(flag);
	}

	private void setEnabledKeyboard(boolean flag) {
		b1.setEnabled(flag);
		b2.setEnabled(flag);
		b3.setEnabled(flag);
		b4.setEnabled(flag);
		b5.setEnabled(flag);
		b6.setEnabled(flag);
		b7.setEnabled(flag);
		b8.setEnabled(flag);
		b9.setEnabled(flag);
		b0.setEnabled(flag);
		cancel.setEnabled(flag);
		correction.setEnabled(flag);
		enter.setEnabled(flag);
	}

	private void setVisibleDisplays(boolean flag) {
		mainDisplay.setVisible(flag);
		printDisplay.setVisible(flag);
		inputCard.setVisible(flag);
	}

	private class KeyboardListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (passwordField.isVisible())
				readPIN(event);
			if (chooseCardField.isVisible())
				inputCard(event);
			if (OK1.isEnabled())
				readPwd1(event);
			if (OK2.isEnabled())
				readPwd2(event);
			if (OK3.isEnabled())
				readPwd3(event);
		}

		public void readPwd1(ActionEvent e) {
			int len = oldPwdField.getPassword().length;
			switch (e.getActionCommand()) {
			case "CORRECTION":
				if (len != 0)
					oldPwdField.setText(oldPwdField.getText().substring(0,
							len - 1));
				break;
			default:
				if (len != 4)
					oldPwdField.setText(oldPwdField.getText()
							+ e.getActionCommand());
				break;
			}
		}

		public void readPwd2(ActionEvent e) {
			int len = newPwdField1.getPassword().length;
			switch (e.getActionCommand()) {
			case "CORRECTION":
				if (len != 0)
					newPwdField1.setText(newPwdField1.getText().substring(0,
							len - 1));
				break;
			default:
				if (len != 4)
					newPwdField1.setText(newPwdField1.getText()
							+ e.getActionCommand());
				break;
			}
		}

		public void readPwd3(ActionEvent e) {
			int len = newPwdField2.getPassword().length;
			switch (e.getActionCommand()) {
			case "CORRECTION":
				if (len != 0)
					newPwdField2.setText(newPwdField2.getText().substring(0,
							len - 1));
				break;
			default:
				if (len != 4)
					newPwdField2.setText(newPwdField2.getText()
							+ e.getActionCommand());
				break;
			}
		}

		public void readPIN(ActionEvent e) {
			int len = passwordField.getPassword().length;
			switch (e.getActionCommand()) {
			case "CORRECTION":
				if (len != 0)
					passwordField.setText(passwordField.getText().substring(0,
							len - 1));
				break;
			default:
				if (len != 4)
					passwordField.setText(passwordField.getText()
							+ e.getActionCommand());
				break;
			}
		}

		public void inputCard(ActionEvent e) {
			int len = chooseCardField.getText().length();
			switch (e.getActionCommand()) {
			case "CORRECTION":
				if (len != 0)
					chooseCardField.setText(chooseCardField.getText()
							.substring(0, len - 1));
				break;
			default:
				if (len != 6)
					chooseCardField.setText(chooseCardField.getText()
							+ e.getActionCommand());
				break;
			}
		}
	}

	public MyFrame getATMwindow() {
		return ATMwindow;
	}
}