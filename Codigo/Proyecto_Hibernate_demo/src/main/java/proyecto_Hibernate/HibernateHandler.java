package proyecto_Hibernate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class HibernateHandler {
    private SessionFactory sessionFactory;

    public HibernateHandler() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        this.sessionFactory = configuration.buildSessionFactory();
    }

    public void cargarBaseDeDatos() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try (Scanner scanner = new Scanner(new File("C:\\Users\\2dam3\\Documents\\GitHub\\Tarea_13_Diciembre\\Archivos\\curos_alumnos.txt"))) {
                String currentCursoNombre = null;
                Curso currentCurso = null;

                while (scanner.hasNextLine()) {
                    String linea = scanner.nextLine();

                    if (linea.startsWith("Curso")) {

                        String[] cursoParts = linea.split(":");
                        currentCursoNombre = cursoParts[1].trim();

                        currentCurso = new Curso();
                        currentCurso.setNombre(currentCursoNombre);
                    } else if (linea.startsWith("Alumno")) {

                        String[] alumnoParts = linea.split(":");
                        String[] nombreApellido = alumnoParts[1].trim().split(" ");
                        String nombre = nombreApellido[0].trim();
                        String apellido = nombreApellido.length > 1 ? nombreApellido[1].trim() : "";

                        Alumno alumno = new Alumno();
                        alumno.setNombre(nombre);
                        alumno.setApellido(apellido);

                        if (currentCurso != null) {
                            currentCurso.addAlumno(alumno);
                        }
                    }
                }

                if (currentCurso != null) {
                    session.save(currentCurso);
                }

                transaction.commit();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                transaction.rollback();
            }
        }
    }

    public void mostrarBaseDeDatos() {
        try (Session session = sessionFactory.openSession()) {
            Query<Curso> query = session.createQuery("from Curso", Curso.class);
            List<Curso> cursos = query.list();

            for (Curso curso : cursos) {
                System.out.println("Curso: " + curso.getNombre());
                System.out.println("Descripción: Curso de " + curso.getNombre());

                for (Alumno alumno : curso.getAlumnos()) {
                    System.out.println("Alumno: " + alumno.getNombre()+ " " + alumno.getApellido());

                }
            }
        }
    }
    
    public void volcarBaseDeDatos() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try (PrintWriter writer = new PrintWriter(new File("E:\\Github\\Tarea_13_Diciembre\\Archivos\\pruebas.xml"))) {
                writer.println("<BaseDeDatos>");

                List<Curso> cursos = session.createQuery("FROM Curso", Curso.class).getResultList();

                writer.println("\t<Cursos>");

                for (Curso curso : cursos) {
                    writer.println("\t\t<Curso>");
                    writer.println("\t\t\t<id>" + curso.getId() + "</id>");
                    writer.println("\t\t\t<nombre>" + curso.getNombre() + "</nombre>");
                    writer.println("\t\t\t<descripcion>Curso de " + curso.getNombre().toLowerCase() + "</descripcion>");

                    List<Alumno> alumnos = curso.getAlumnos();
                    writer.println("\t\t\t<Alumnos>");

                    for (Alumno alumno : alumnos) {
                        writer.println("\t\t\t\t<Alumno>");
                        writer.println("\t\t\t\t\t<id>" + alumno.getId() + "</id>");
                        writer.println("\t\t\t\t\t<nombre>" + alumno.getNombre() + "</nombre>");
                        writer.println("\t\t\t\t\t<apellido>" + alumno.getApellido() + "</apellido>");
                        writer.println("\t\t\t\t</Alumno>");
                    }

                    writer.println("\t\t\t</Alumnos>");
                    writer.println("\t\t</Curso>");
                }

                writer.println("\t</Cursos>");
                writer.println("</BaseDeDatos>");

                transaction.commit();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                transaction.rollback();
            }
        }
    }
}
