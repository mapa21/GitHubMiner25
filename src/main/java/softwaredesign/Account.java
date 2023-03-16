package softwaredesign;
import org.kohsuke.github.*;


import org.jetbrains.annotations.NotNull;

public class Account implements Comparable<Account>{
    public String name;
    private String password;
    public String token;
    public Boolean login(String password){
        return true;
    }

    public int addRepo() {
        String repoName = UserConsole.getInput("Enter the repository name");
        String repoOwner = UserConsole.getInput("Enter the repository's owner");

        // TODO: instantiate new Repo class
        return 0;
//        return (Repository.clone()) ? 1 : 0;
    }

    protected void setToken() {
        String newToken = UserConsole.getInput("Enter a token");

        if (!isTokenValid(newToken)) {
            // TODO: change output format
            System.out.println("Token " + newToken + " is invalid. Please try again.");
            return;
        }

        this.token = token;
    }

    protected boolean isTokenValid(String newToken) {
        boolean isValid = false;

        try {
            isValid = GitHub.connectUsingOAuth(newToken).isCredentialValid();
        } catch (Exception e) {
            // TODO: this seems like nonsense
            System.out.println("bleh.");
        }
        return isValid;
    }

    @Override
    public String toString() {
        return this.name;
    }
    @Override
    public int compareTo(@NotNull Account rhs) {
        if (this == rhs) return 0;
        return (this.name.compareToIgnoreCase(rhs.name));
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        return this.compareTo((Account) o) == 0;
    }
    @Override
    public int hashCode() {
        return this.name.toLowerCase().hashCode();
    }
}
