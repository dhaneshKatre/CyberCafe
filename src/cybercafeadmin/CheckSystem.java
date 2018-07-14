package cybercafeadmin;

public class CheckSystem extends java.awt.Frame {

    /**
     * Creates new form CheckSystem
     */
    public CheckSystem() {
        initComponents();
        txt.setText("PC id\tAvailable\tUser\t\tIn Time\n\n");
        DatabaseHandler.getStatus().forEach((e) -> {
            txt.setText(txt.getText() + e);
        });
        setVisible(true);
    }

   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        d = new javax.swing.JPanel();
        text = new javax.swing.JScrollPane();
        txt = new javax.swing.JTextArea();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        text.setPreferredSize(new java.awt.Dimension(500, 200));

        txt.setColumns(20);
        txt.setRows(5);
        text.setViewportView(txt);

        d.add(text);

        add(d, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        dispose();
    }//GEN-LAST:event_exitForm

   


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel d;
    private javax.swing.JScrollPane text;
    private javax.swing.JTextArea txt;
    // End of variables declaration//GEN-END:variables
}
