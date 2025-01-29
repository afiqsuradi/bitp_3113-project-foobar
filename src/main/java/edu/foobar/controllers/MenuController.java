package edu.foobar.controllers;

import edu.foobar.dao.MenuDao;
import edu.foobar.models.Menu;

import java.util.List;

// i am too lazy to implement anything lol
public class MenuController {
    private MenuDao menuDAO;

    public MenuController(){
        this.menuDAO = new MenuDao();
    }
    public Menu getMenu(int id){
        return this.menuDAO.get(id);
    }

    public List<Menu> getAllMenus(){
        return this.menuDAO.getAll();
    }

    public void createMenu(Menu menu){
        this.menuDAO.save(menu);
    }

    public void updateMenu(Menu menu){
        this.menuDAO.update(menu);
    }
    public void deleteMenu(Menu menu){
        this.menuDAO.delete(menu);
    }
}
