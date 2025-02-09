package edu.foobar.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import edu.foobar.controllers.MenuController;
import edu.foobar.models.Enums;
import edu.foobar.models.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.Component;

public class AdminView extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(AdminView.class);

    private final Color PRIMARY_COLOR = new Color(0x3F51B5);
    private final Color SECONDARY_COLOR = new Color(0x673AB7);
    private final Color BACKGROUND_COLOR = new Color(0xE8EAF6);
    private final Color TEXT_PRIMARY_COLOR = new Color(0x212121);
    private final Color TEXT_SECONDARY_COLOR = new Color(0x757575);
    private final Color ACCENT_COLOR = new Color(0xFF4081);

    private String[] columnNames = {"ID", "Name", "Price", "Category"};
    private DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return Integer.class;
                case 1:
                    return String.class;
                case 2:
                    return Long.class;
                case 3:
                    return Enums.FoodCategory.class;
                default:
                    return Object.class;
            }
        }
    };
    private JTable mytable = new JTable(tableModel);
    private MenuController menuController;

    private JTextField nameField = new JTextField(20);
    private JTextField priceField = new JTextField(10);
    private JComboBox<Enums.FoodCategory> categoryComboBox = new JComboBox<>(Enums.FoodCategory.values());

    private JButton createButton = new JButton("Create");
    private JButton updateButton = new JButton("Update");
    private JButton cancelButton = new JButton("Cancel");
    private JButton backButton = new JButton("Back");

    private JPanel formPanel;
    private JLabel titleLabel;

    private Menu selectedMenu;

    public AdminView() {
        super("Admin Menu");
        menuController = new MenuController();
        setupUI();
        setMenuTable();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setVisible(true);
        setupTableSelectionListener();
    }

    private void setupUI() {
        getContentPane().setBackground(BACKGROUND_COLOR);

        formPanel = createMenuFormPanel();
        this.add(formPanel, BorderLayout.NORTH);
        this.add(createMenuTablePanel(), BorderLayout.CENTER);

        setFormMode(true);

    }

    private JPanel createMenuTablePanel() {
        JPanel menuTablePanel = new JPanel();
        String title = "Menu List Table";
        Border border = BorderFactory.createTitledBorder(title);
        menuTablePanel.setBorder(border);
        menuTablePanel.setBackground(BACKGROUND_COLOR);
        menuTablePanel.setLayout(new BorderLayout(3, 3));
        JScrollPane scrollPane = new JScrollPane(mytable);
        menuTablePanel.add(scrollPane, BorderLayout.CENTER);
        return menuTablePanel;
    }

    private JPanel createMenuFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(1080, 200));
        panel.setBackground(BACKGROUND_COLOR);

        titleLabel = new JLabel("Create Menu", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.add(new JLabel("Name:"));

        nameField.setBackground(Color.WHITE);
        nameField.setForeground(TEXT_PRIMARY_COLOR);

        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Price:"));

        priceField.setBackground(Color.WHITE);
        priceField.setForeground(TEXT_PRIMARY_COLOR);
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Category:"));

        categoryComboBox.setBackground(Color.WHITE);
        categoryComboBox.setForeground(TEXT_PRIMARY_COLOR);
        inputPanel.add(categoryComboBox);

        panel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JPanel innerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        innerButtonPanel.setBackground(BACKGROUND_COLOR);

        styleButton(backButton);
        styleButton(createButton);
        styleButton(updateButton);
        styleButton(cancelButton);

        innerButtonPanel.add(cancelButton);
        innerButtonPanel.add(updateButton);
        innerButtonPanel.add(createButton);

        buttonPanel.add(backButton, BorderLayout.WEST);
        buttonPanel.add(innerButtonPanel, BorderLayout.EAST);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createMenu();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMenu();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
                selectedMenu = null;
                mytable.clearSelection();
                setFormMode(true);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginView.showLogin();
                dispose();
            }
        });

        return panel;
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    private void setupTableSelectionListener() {
        ListSelectionModel selectionModel = mytable.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = mytable.getSelectedRow();
                    if (selectedRow != -1) {
                        int id = (int) tableModel.getValueAt(selectedRow, 0);
                        String name = (String) tableModel.getValueAt(selectedRow, 1);
                        long price = (long) tableModel.getValueAt(selectedRow, 2);
                        Enums.FoodCategory category = (Enums.FoodCategory) tableModel.getValueAt(selectedRow, 3);

                        selectedMenu = new Menu(id, name, price, category);
                        populateForm(selectedMenu);
                        setFormMode(false);
                    }
                }
            }
        });
    }

    private void setFormMode(boolean isCreateMode) {
        createButton.setVisible(isCreateMode);
        updateButton.setVisible(!isCreateMode);
        cancelButton.setVisible(!isCreateMode);

        if (isCreateMode) {
            titleLabel.setText("Create Menu");
        } else {
            titleLabel.setText("Update Menu");
        }
    }

    private void populateForm(Menu menu) {
        nameField.setText(menu.getName());
        priceField.setText(String.valueOf(menu.getPrice()));
        categoryComboBox.setSelectedItem(menu.getCategory());
    }

    private void clearForm() {
        nameField.setText("");
        priceField.setText("");
        categoryComboBox.setSelectedIndex(0);
    }

    private void setMenuTable() {
        List<Menu> menu = menuController.getAllMenus();
        tableModel.setRowCount(0);
        for (Menu items : menu) {
            Object[] rowData = {items.getId(), items.getName(), items.getPrice(), items.getCategory()};
            tableModel.addRow(rowData);
        }
    }

    private void createMenu() {
        try {
            String name = nameField.getText();
            long price = Long.parseLong(priceField.getText());
            Enums.FoodCategory category = (Enums.FoodCategory) categoryComboBox.getSelectedItem();

            Menu newMenu = new Menu(name, price, category);
            Menu createdMenu = menuController.createMenu(newMenu);

            if (createdMenu != null) {
                setMenuTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Menu created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setFormMode(true);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create menu.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error creating menu. Please check the data.", "Error", JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage());
        }
    }

    private void updateMenu() {
        if (selectedMenu == null) {
            JOptionPane.showMessageDialog(this, "No menu selected for update.", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            selectedMenu.setName(nameField.getText());
            selectedMenu.setPrice(Long.parseLong(priceField.getText()));
            selectedMenu.setCategory((Enums.FoodCategory) categoryComboBox.getSelectedItem());

            menuController.updateMenu(selectedMenu);
            setMenuTable();
            clearForm();
            mytable.clearSelection();
            selectedMenu = null;
            setFormMode(true);
            JOptionPane.showMessageDialog(this, "Menu updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating menu. Please check the data.", "Error", JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage());
        }
    }

    public static void showMenuLists() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminView();
            }
        });
    }
}