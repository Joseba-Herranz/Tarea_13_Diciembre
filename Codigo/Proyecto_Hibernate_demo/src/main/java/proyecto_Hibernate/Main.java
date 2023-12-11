package proyecto_Hibernate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		// Bucle principal del menú
		while (true) {
			// Mostrar el menú
			System.out.println("--------------------------------");
			System.out.println("1. Cargar base de datos desde fichero");
			System.out.println("2. Mostrar base de datos");
			System.out.println("3. Volcar base de datos a XML");
			System.out.println("4. Salir");
			System.out.println("--------------------------------");

			// Leer la opción del usuario
			int opcion = sc.nextInt();

			// Ejecutar la opción seleccionada
			switch (opcion) {
			case 1:
				cargarBaseDeDatos();
				break;
			case 2:
				mostrarBaseDeDatos();
				break;
			case 3:
				volcarBaseDeDatos();
				break;
			case 4:
				System.out.println("Saliendo del programa...");
				return;
			default:
				System.out.println("Opción no válida");
			}
		}
	}
	
	
	// Método para cargar la base de datos desde un fichero
	private static void cargarBaseDeDatos() {
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
	
	
	// Método para mostrar la base de datos
	private static void mostrarBaseDeDatos() {
		// Crear una sesión de Hibernate
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		// Consultar todos los cursos
		Query query = session.createQuery("from Curso");
		List<Curso> cursos = query.list();

		// Imprimir los datos de los cursos
		for (Curso curso : cursos) {
			System.out.println("Curso: " + curso.getNombre());
			System.out.println("Descripción: " + curso.getDescripcion());

			// Imprimir los datos de los alumnos
			for (Alumno alumno : curso.getAlumnos()) {
				System.out.println("Alumno: " + alumno.getNombre());
			}
		}

		// Cerrar la sesión de Hibernate
		session.close();
	}

	private static void volcarBaseDeDatos() {
		Configuration configuration = new Configuration();
		configuration.configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();

		// Obtén una lista de todos los cursos
		List<Curso> cursos = session.createQuery("SELECT c FROM Curso c", Curso.class).list();

		// Crea un nuevo archivo de volcado
		File archivoVolcado = new File("volcado.xml");
		PrintWriter pw = new PrintWriter(archivoVolcado);

		// Escribe el encabezado del archivo
		pw.println("<BaseDeDatos>");

		// Escribe los cursos
		for (Curso curso : cursos) {
			pw.println("  <Curso>");
			pw.println("    <id>" + curso.getId() + "</id>");
			pw.println("    <nombre>" + curso.getNombre() + "</nombre>");
			pw.println("    <descripcion>" + curso.getDescripcion() + "</descripcion>");

			// Escribe los alumnos del curso
			List<Alumno> alumnos = curso.getAlumnos();
			for (Alumno alumno : alumnos) {
				pw.println("      <Alumno>");
				pw.println("        <id>" + alumno.getId() + "</id>");
				pw.println("        <nombre>" + alumno.getNombre() + "</nombre>");
				pw.println("        <apellido>" + alumno.getApellido() + "</apellido>");
				pw.println("      </Alumno>");
			}

			pw.println("  </Curso>");
		}

		// Escribe el pie de página del archivo
		pw.println("</BaseDeDatos>");

		// Cierra el archivo de volcado
		pw.close();

	}
}
