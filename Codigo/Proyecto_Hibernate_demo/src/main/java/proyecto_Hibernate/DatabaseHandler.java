package proyecto_Hibernate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class DatabaseHandler {
    private HibernateHandler hibernateHandler;

    public DatabaseHandler() {
        this.hibernateHandler = new HibernateHandler();
    }

    public void cargarBaseDeDatos() {
        hibernateHandler.cargarBaseDeDatos();
    }

    public void mostrarBaseDeDatos() {
        hibernateHandler.mostrarBaseDeDatos();
    }

    public void volcarBaseDeDatos() {
        hibernateHandler.volcarBaseDeDatos();
    }
}

