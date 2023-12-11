package proyecto_Hibernate;

import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CargaBaseDeDatos extends Main{

	  public static void main(String[] args) {

	    // Crea una sesión de Hibernate
	    Configuration configuration = new Configuration();
	    configuration.configure("hibernate.cfg.xml");
	    SessionFactory sessionFactory = configuration.buildSessionFactory();
	    Session session = sessionFactory.openSession();

	    // Crea una nueva transacción
	    session.beginTransaction();

	    // Lee los datos del archivo
	    Scanner scanner = new Scanner(new File("cursos_alumnos.txt"));
	    while (scanner.hasNextLine()) {
	      String linea = scanner.nextLine();

	      // Divide la línea en dos partes
	      String[] partes = linea.split(":");

	      // Obtén el nombre del curso
	      String nombreCurso = partes[0];

	      // Obtén los alumnos del curso
	      String[] alumnos = partes[1].split(";");

	      // Crea un curso
	      Curso curso = new Curso();
	      curso.setNombre(nombreCurso);

	      // Crea los alumnos
	      for (String alumno : alumnos) {
	        Alumno a = new Alumno();
	        a.setNombre(alumno.split(":")[0]);
	        a.setApellido(alumno.split(":")[1]);
	        curso.addAlumno(a);
	      }

	      // Guarda el curso
	      session.save(curso);
	    }

	    // Commitea la transacción
	    session.getTransaction().commit();

	    // Cierra la sesión
	    session.close();
	  }
	}