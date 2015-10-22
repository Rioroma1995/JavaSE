package ATM;

public class Client {
    private int id;
    private String name;
    private String card;
    private String pass;
    private int balance;
    private String email;

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Client(int id, String name, String card, String pass, int balance, String email) {
    	this.id = id;
        this.name = name;
        this.card = card;
        this.pass = pass;
        this.balance = balance;
        this.email = email;
    }
    Client() {
	}

    public String toString() {
        return id+" "+name+ " "+ card+" "+pass+" "+balance+" "+email;
        
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public String getCard() {
        return card;
    }

    public String getPass() {
        return pass;
    }
    public String getEmail() {
        return email;
    }

	public int getId() {
		return id;
	}   
}
