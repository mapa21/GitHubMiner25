public class Account {
    public String name;
    private String password;
    private StringBuilder token = new StringBuilder();
    public Account(String string) {
        token.append(string);
    }
    public String getToken() {
        return (token.toString()
        );
    }

    public void changeToken(String newString) {
        token.replace(0, token.length() - 1, newString);
    }

    static public void printSomething(String string) {
        System.out.println(string);
        return;
    }
}
