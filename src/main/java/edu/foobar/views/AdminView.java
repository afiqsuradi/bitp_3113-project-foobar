package edu.foobar.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import edu.foobar.controllers.MenuController;
import edu.foobar.models.Enums;
import edu.foobar.models.Menu;

public class AdminView extends JFrame {
    
    private String[] columnNames = {"ID", "Name", "Price","Category"};
    private DefaultTableModel tableModel = new DefaultTableModel(columnNames,0);
    private JTable mytable= new JTable(tableModel);;
    private MenuController menuController;

    public AdminView(){
        super("Admin Menu");
        menuController = new MenuController();
        setMenuTable();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(3,3));
        setVisible(true);
        this.add(createMenuPanel(),BorderLayout.SOUTH);
        //this.add(createSaveButton());
    }

    public JPanel createMenuPanel(){
        JPanel menuTablePanel = new JPanel();
        String title = "Menu List Table";
        Border border = BorderFactory.createTitledBorder(title);
        menuTablePanel.setBorder(border);
        menuTablePanel.setBackground(Color.CYAN);
        menuTablePanel.setLayout(new BorderLayout(3,3));
        JScrollPane scrollPane = new JScrollPane(mytable);
        menuTablePanel.add(scrollPane,BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createSaveButton());
        buttonPanel.add(createDiscardButton());
        menuTablePanel.add(buttonPanel, BorderLayout.SOUTH);

        return menuTablePanel;
    }
    
    public void setMenuTable(){
        List<Menu> menu = menuController.getAllMenus();
        tableModel.setRowCount(0);
        for(Menu items : menu){
            Object[] rowData = {items.getId(),items.getName(),items.getPrice(),items.getCategory()};
            tableModel.addRow(rowData);
        }
    }

    private JButton createSaveButton() {
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(AdminView.this, 
                        "Are you sure you want to save changes?", "Confirm", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    saveChanges();
                }
            }
        });
        return saveButton;
    }

    private void saveChanges() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int id = (int) tableModel.getValueAt(i, 0);
            String name = (String) tableModel.getValueAt(i, 1);
            long price = (long) tableModel.getValueAt(i, 2);
            Enums.FoodCategory category = (Enums.FoodCategory) tableModel.getValueAt(i, 3);

            Menu updatedMenu = new Menu(id, name, price, category);
            menuController.updateMenu(updatedMenu);
        }
        JOptionPane.showMessageDialog(this, "Changes saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton createDiscardButton() {
        JButton discardButton = new JButton("Discard Changes");
        discardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(AdminView.this, 
                        "Are you sure you want to discard changes?", "Confirm", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    discardChanges();
                    tableModel.fireTableDataChanged();
                }
            }
        });
        return discardButton;
    }

    private void discardChanges() {
        setMenuTable();
        tableModel.fireTableDataChanged();
        mytable.repaint();
        JOptionPane.showMessageDialog(this, "Changes discarded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showMenuLists(){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminView();
            }
        });
    }
}
