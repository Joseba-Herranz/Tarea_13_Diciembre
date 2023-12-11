package proyecto_Hibernate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
		// Leer el nombre del fichero
		System.out.println("Introduce el nombre del fichero:");
		String nombreFichero = sc.nextLine();

		// Cargar los datos del fichero en la base de datos
		try {
			// Crear una sesión de Hibernate
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			Session session = sessionFactory.openSession();

			// Cargar los datos del fichero
			FileReader fr = new FileReader(nombreFichero);
			BufferedReader br = new BufferedReader(fr);

			String linea;
			while ((linea = br.readLine()) != null) {
				// Parsear la línea del fichero
				String[] campos = linea.split(";");

				// Crear un nuevo curso
				Curso curso = new Curso();
				curso.setNombre(campos[0]);
				curso.setDescripcion(campos[1]);

				// Crear una lista de alumnos
				List<Alumno> alumnos = new ArrayList<>();
				for (int i = 2; i < campos.length; i++) {
					alumnos.add(new Alumno(campos[i]));
				}

				// Asociar el curso a los alumnos
				curso.setAlumnos(alumnos);

				// Guardar el curso en la base de datos
				session.save(curso);
			}

			// Cerrar la sesión de Hibernate
			session.close();
		} catch (Exception e) {
			// Manejar la excepción
			System.out.println("Error al cargar la base de datos: " + e.getMessage());
		}
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
