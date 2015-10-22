package ATM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

final public class ATM {
	private static Client currentClient;
	private static MailServer ms;
	private static ArrayList<Client> clientList = new ArrayList<>();
	private ArrayList<String> transactions = new ArrayList<>();
	private static int num;
	private static int IDtransaction = 12345;
	private static int cashLimit = 5000;
	private static int minBalanceLimit = 10;
	private static int minSendLimit = 10;
	private static Integer moneyCount10 = 0;
	private static Integer totalCash = 0;
	private static JFrame window;

	public ArrayList<Client> getClient() {
		return clientList;
	}

	void addTransaction(String tr) {
		transactions.add(tr);
	}

	static String nextTransaction() {
		IDtransaction += 1;
		return String.valueOf(IDtransaction);
	}

	public static void update(int id, String password, int balance)
			throws IOException {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			try {
				PreparedStatement statement = c
						.prepareStatement("UPDATE ATM SET "
								+ "PASSWORD = ?, BALANCE = ?Where id = ?");
				statement.setString(1, password);
				statement.setInt(2, balance);
				statement.setInt(3, id);
				statement.executeUpdate();
				statement.close();
				c.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
				System.exit(0);
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public ATM() {
		window = GUI.getInstance().getATMwindow();
		ms = new MailServer();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			stmt = c.createStatement();
			try {
				String selTable = "SELECT * FROM ATM";
				stmt.execute(selTable);
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					clientList.add(new Client(rs.getInt(1), rs.getString(2), rs
							.getString(3), rs.getString(4), rs.getInt(5), rs
							.getString(6)));
				}
				stmt.close();
				c.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
				System.exit(0);
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		num = clientList.size();
	}

	public static void createBD() {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			stmt = c.createStatement();
			String sql = "CREATE TABLE ATM "
					+ "(ID INT PRIMARY KEY     NOT NULL,"
					+ " NAME           TEXT    NOT NULL, "
					+ " CARD            TEXT     NOT NULL, "
					+ " PASSWORD        TEXT NOT NULL, "
					+ " BALANCE         INT NOT NULL,"
					+ " EMAIL           TEXT    NOT NULL " + ")";
			String dropTable = "DROP TABLE ATM ";
			stmt.execute(dropTable);
			stmt.executeUpdate(sql);
			try {
				String sql1 = "INSERT INTO ATM (ID, NAME, CARD, PASSWORD, BALANCE, EMAIL) "
						+ "VALUES (1, 'Roma', 11111, '1111', 4000, 'rio1995@meta.ua');";
				stmt.executeUpdate(sql1);
				sql1 = "INSERT INTO ATM (ID,NAME,CARD,PASSWORD,BALANCE, EMAIL) "
						+ "VALUES (2, 'Yura', 22222, '2222', 3000, 'riznyk7@gmail.com');";
				stmt.executeUpdate(sql1);
				sql1 = "INSERT INTO ATM (ID,NAME,CARD,PASSWORD,BALANCE, EMAIL) "
						+ "VALUES (3, 'Paul', 33333, '3333', 2000, 'rius@meta.ua');";
				stmt.executeUpdate(sql1);
				stmt.close();
				c.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
				System.exit(0);
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public static boolean isClient(String card) {
		boolean find = false;
		for (int i = 0; i < num && !find; i++)
			if (clientList.get(i).getCard().equals(card)) {
				currentClient = clientList.get(i);
				find = true;
			}
		return find;
	}

	public static boolean enterCard(String input) {
		boolean finder = isClient(input);
		return finder;
	}

	public static boolean enterPIN(String InputPin) {
		return currentClient.getPass().equals(InputPin);
	}

	public static boolean doCash(int amount) {
		if (amount > cashLimit) {
			JOptionPane.showMessageDialog(window, "Перевищено ліміт",
					"Помилка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (amount > totalCash) {
			JOptionPane.showMessageDialog(window, "Недостатньо коштів",
					"Помилка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (currentClient.getBalance() > amount + minBalanceLimit) {
			currentClient.setBalance(currentClient.getBalance() - amount);
			totalCash -= amount;
			JOptionPane.showMessageDialog(window, "Заберіть готівку",
					"Успішна транзакція", JOptionPane.INFORMATION_MESSAGE);
			try {
				update(currentClient.getId(), currentClient.getPass(),
						currentClient.getBalance());
				ms.prepareMessage(currentClient.getName(), true, String.valueOf(amount), new java.util.Date().toString(), String.valueOf(currentClient.getBalance()), currentClient.getEmail());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(window, "Помилка");
			}
			return true;
		} else {
			return false;
		}
	}

	public static int search(String id) {
		/*
		 * return -1 - client not found return int - this int is client's ID
		 */
		int isCl = -1;
		for (int i = 0; i < num && isCl == -1; i++) {
			if ((clientList.get(i).getCard().equals(id)))
				isCl = i;
		}
		return isCl;
	}

	public static boolean ProcessSend(String fromCard, String toCard, int sum) {
		if (toCard.equals(currentClient.getCard())) {
			JOptionPane.showMessageDialog(window, "Ви ввели свій номер картки",
					"Помилка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (sum < minSendLimit) {
			JOptionPane.showMessageDialog(window, "Мінімальна сума переказу: "
					+ minBalanceLimit + " грн", "Помилка",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (toCard.length() > 6 || toCard.length() < 5) {
			JOptionPane.showMessageDialog(window, "Неправильний номер картки",
					"Помилка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (search(toCard) == -1) {
			JOptionPane.showMessageDialog(window, "Такої картки не існує",
					"Помилка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (cashLimit < sum) {
			JOptionPane.showMessageDialog(window, "Перевищено ліміт",
					"Помилка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (currentClient.getBalance() - minBalanceLimit < sum) {
			JOptionPane.showMessageDialog(window, "Недостатньо коштів",
					"Помилка", JOptionPane.ERROR_MESSAGE);
			return true;
		} else {

			int order = search(toCard);
			clientList.get(order).setBalance(
					clientList.get(order).getBalance() + sum);

			int CurSum = currentClient.getBalance();
			currentClient.setBalance(CurSum - sum);
			JOptionPane.showMessageDialog(window, "Кошти переказано",
					"Успішна транзакція", JOptionPane.INFORMATION_MESSAGE);

			try {
				update(clientList.get(order).getId(), clientList.get(order)
						.getPass(), clientList.get(order).getBalance());
				ms.prepareMessage(currentClient.getName(), false, String.valueOf(sum), new java.util.Date().toString(), String.valueOf(currentClient.getBalance()), currentClient.getEmail());
				update(currentClient.getId(), currentClient.getPass(),
						currentClient.getBalance());
				ms.prepareMessage(currentClient.getName(), true, String.valueOf(sum), new java.util.Date().toString(), String.valueOf(currentClient.getBalance()), currentClient.getEmail());
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(window, "Помилка");
			}
			return true;
		}
	}

	public static boolean MobileFillUp(int sum) {
		if (sum < 5) {
			JOptionPane.showMessageDialog(window, "Введіть більшу суму",
					"Помилка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (sum > cashLimit) {
			JOptionPane.showMessageDialog(window, "Перевищено ліміт",
					"Помилка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (currentClient.getBalance() - minBalanceLimit < sum) {
			JOptionPane.showMessageDialog(window, "Недостатньо коштів",
					"Помилка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		currentClient.setBalance(currentClient.getBalance() - sum);
		JOptionPane.showMessageDialog(window, "Мобільний поповнено",
				"Успішна транзакція", JOptionPane.INFORMATION_MESSAGE);
		try {
			update(currentClient.getId(), currentClient.getPass(),
					currentClient.getBalance());
			ms.prepareMessage(currentClient.getName(), true, String.valueOf(sum), new java.util.Date().toString(), String.valueOf(currentClient.getBalance()), currentClient.getEmail());
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(window, "Помилка");
		}
		return true;
	}

	public static boolean setPassword(String oldPwd, String newPwd1, String newPwd2) {
		boolean check = false;
		if (!oldPwd.equals(currentClient.getPass()))
			JOptionPane.showMessageDialog(window,
					"Неправильний поточний пароль", "Помилка",
					JOptionPane.ERROR_MESSAGE);
		else if (newPwd1.equals(newPwd2)) {
			currentClient.setPass(newPwd2);
			check = true;
			JOptionPane.showMessageDialog(window, "Пароль змінено",
					"Зміна PIN-коду", JOptionPane.INFORMATION_MESSAGE);
		} else
			JOptionPane.showMessageDialog(window, "Пароль не змінено",
					"Помилка", JOptionPane.ERROR_MESSAGE);
		try {
			update(currentClient.getId(), currentClient.getPass(),
					currentClient.getBalance());
			ms.changePassword(currentClient.getName(), currentClient.getPass(), new java.util.Date().toString(), currentClient.getEmail());
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(window, "Помилка");
		}
		return check;
	}

	public static Client getCurrentClient() {
		return currentClient;
	}

	public int getIDtransaction() {
		return IDtransaction;
	}

	public void setIDtransaction(int iDtransaction) {
		IDtransaction = iDtransaction;
	}

	public Integer getMoneyCount10() {
		return moneyCount10;
	}

	public static Integer getTotalCash() {
		return totalCash;
	}

	public static void setMoneyCount10(Integer moneyCount) {
		moneyCount10 = moneyCount;
	}

	public static void setTotalCash(Integer cash) {
		totalCash = cash;
	}
}
