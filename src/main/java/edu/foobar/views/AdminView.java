package edu.foobar.views;

import java.awt.Color;
import java.util.List;
import java.util.Locale.Category;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import edu.foobar.controllers.MenuController;
import edu.foobar.models.Menu;

public class AdminView extends JFrame {
    
    private JFrame frame;
    private MenuController menuController;

    public AdminView(){
        menuController = new MenuController();
        frame = new JFrame("Admin Menu");
        frame.add(showMenuLists());
        frame.setSize(1080, 720);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public JPanel showMenuLists(){
        JPanel menuTablePanel = new JPanel();
        menuTablePanel.setBounds(40,200,200,200);
        menuTablePanel.setBackground(Color.GRAY);
        JScrollPane scrollPane = new JScrollPane(getMenuTable());
        menuTablePanel.add(scrollPane);
        return menuTablePanel;
    }

    public JTable getMenuTable(){
        List<Menu> menu = menuController.getAllMenus();

        String[] columnNames = {"ID", "Name", "Price","Category"};
        Object[][] data = new Object[menu.size()][4];

        for (int i = 0; i < menu.size(); i++) {
            data[i][0] = menu.get(i).getId();
            data[i][1] = menu.get(i).getName();
            data[i][2] = menu.get(i).getPrice();
            data[i][3] = menu.get(i).getCategory();
        }
        JTable table = new JTable(data, columnNames);
        return table;
    }

}
