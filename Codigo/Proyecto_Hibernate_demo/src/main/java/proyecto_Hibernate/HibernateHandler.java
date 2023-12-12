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
                        // Create a new Curso when a line starts with "Curso"
                        String[] cursoParts = linea.split(":");
                        currentCursoNombre = cursoParts[1].trim();

                        currentCurso = new Curso();
                        currentCurso.setNombre(currentCursoNombre);
                    } else if (linea.startsWith("Alumno")) {
                        // Add Alumno to the current Curso when a line starts with "Alumno"
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

                // Save the last Curso after exiting the loop
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
                System.out.println("Descripción: " + curso.getDescripcion());

                for (Alumno alumno : curso.getAlumnos()) {
                    System.out.println("Alumno: " + alumno.getNombre());
                }
            }
        }
    }
    
    public void volcarBaseDeDatos() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try (PrintWriter writer = new PrintWriter(new File("E:\\Github\\Tarea_13_Diciembre\\Archivos\\dump.xml"))) {
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
    
   /* public void volcarBaseDeDatos() {
        try (Session session = sessionFactory.openSession()) {
            List<Curso> cursos = session.createQuery("SELECT c FROM Curso c", Curso.class).list();

            File archivoVolcado = new File("C:\\Users\\josdr\\Desktop\\prueba\\pruebas.xml");
            try (PrintWriter pw = new PrintWriter(archivoVolcado)) {
                pw.println("<BaseDeDatos>");

                for (Curso curso : cursos) {
                    pw.println("  <Curso>");
                    pw.println("    <id>" + curso.getId() + "</id>");
                    pw.println("    <nombre>" + curso.getNombre() + "</nombre>");
                    pw.println("    <descripcion>" + curso.getDescripcion() + "</descripcion>");

                    for (Alumno alumno : curso.getAlumnos()) {
                        pw.println("      <Alumno>");
                        pw.println("        <id>" + alumno.getId() + "</id>");
                        pw.println("        <nombre>" + alumno.getNombre() + "</nombre>");
                        pw.println("        <apellido>" + alumno.getApellido() + "</apellido>");
                        pw.println("      </Alumno>");
                    }

                    pw.println("  </Curso>");
                }

                pw.println("</BaseDeDatos>");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }*/
}
