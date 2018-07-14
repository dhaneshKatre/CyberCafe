package cybercafeadmin;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;

public class AdminDashboard extends Frame {
    private static String email;
    public AdminDashboard(String email){
        this.email = email;
        if(!CyberCafe.isAdminLoggedIn){
            setVisible(false);
            dispose();
            new AdminLogin();
        }
        Button checkSystem = new Button("Check System");
        checkSystem.addActionListener((ActionEvent e) -> {
            new CheckSystem();
        });
        Button getReport = new Button("Check Report");
        getReport.addActionListener((ActionEvent e) -> {
            new CheckReport();
        });
        Button addNewUser = new Button("New User");
        addNewUser.addActionListener((ActionEvent e) -> {
            new AddNewUser();
        });
        Button updateUser = new Button("Update User");
        updateUser.addActionListener((e) -> {
            new UpdateUser();
        });
        Button activateUser = new Button("Start User Session");
        activateUser.addActionListener((ActionEvent e) -> {
            new StartSession();
        });
        Button deactivateUser = new Button("End User Session");
        deactivateUser.addActionListener((ActionEvent e) -> {
            new TerminateSession();
        });
        Button logout = new Button("Logout");
        logout.addActionListener((ActionEvent e) -> { 
            CyberCafe.isAdminLoggedIn = false;
            this.email = null;
            setVisible(false);
            dispose();
            JOptionPane.showOptionDialog(null, "Logout Successful!", "Success!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            new AdminLogin();
        });
        setLayout(new FlowLayout());
        setSize(210, 400);
        setVisible(true);
        add(new Label("ADMIN DASHBOARD"));
        add(checkSystem);
        add(getReport);
        add(addNewUser);
        add(updateUser);
        add(activateUser);
        add(deactivateUser);
        add(logout);
    }
}
