package proyecto_Hibernate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;


public class HibernateHandler {
    private SessionFactory sessionFactory;

    public HibernateHandler() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Curso.class);
        configuration.addAnnotatedClass(Alumno.class);
        this.sessionFactory = configuration.buildSessionFactory();
    }
    
    public void cargarBaseDeDatos() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try (Scanner scanner = new Scanner(new File("curos_alumnos.txt"))) {
                Curso currentCurso = null;

                while (scanner.hasNextLine()) {
                    String linea = scanner.nextLine().trim();

                    if (linea.isEmpty()) {
                        continue;  // Skip empty lines
                    }

                    System.out.println("Leyendo línea: " + linea);

                    if (linea.startsWith("Curso")) {
                        if (currentCurso != null) {
                            // Save the curso and associated alumnos
                            session.save(currentCurso);
                            System.out.println("Guardando curso: " + currentCurso);
                        }

                        String[] cursoParts = linea.split(":");
                        String currentCursoNombre = cursoParts[1].trim();

                        currentCurso = new Curso();
                        currentCurso.setNombre(currentCursoNombre);
                        System.out.println("Nuevo curso: " + currentCurso);
                    } else if (linea.startsWith("Alumno")) {
                        if (currentCurso != null) {
                            String[] alumnoParts = linea.split(":");
                            String nombreCompleto = alumnoParts[1].trim();
                            String[] nombreApellido = nombreCompleto.split(" ", 2);
                            String nombre = nombreApellido[0].trim();
                            String apellido = nombreApellido.length > 1 ? nombreApellido[1].trim() : "";

                            Alumno alumno = new Alumno();
                            alumno.setNombre(nombre);
                            alumno.setApellido(apellido);

                            // Set the curso for the alumno
                            alumno.setCurso(currentCurso);

                            // Add the alumno to the curso
                            currentCurso.addAlumno(alumno);
                            session.save(alumno);

                            System.out.println("Añadiendo alumno a curso: " + alumno + " " + alumno.getNombre() + " " + alumno.getApellido());
                        }
                    }
                }

                // Save the last curso and associated alumnos
                if (currentCurso != null) {
                    session.save(currentCurso);
                    System.out.println("Guardando último curso: " + currentCurso);
                }

                transaction.commit();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    
    
    /*
    //Arreglar que no se machaca los datos anteriores
    public void cargarBaseDeDatos() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try (Scanner scanner = new Scanner(new File("curos_alumnos.txt"))) {
                Curso currentCurso = null;

                while (scanner.hasNextLine()) {
                    String linea = scanner.nextLine();
                    System.out.println("Leyendo línea: " + linea);

                    if (linea.startsWith("Curso")) {
                        if (currentCurso != null) {
                            session.save(currentCurso);
                            System.out.println("Guardando curso: " + currentCurso);
                            currentCurso = null;
                        }

                        String[] cursoParts = linea.split(":");
                        String currentCursoNombre = cursoParts[1].trim();

                        currentCurso = new Curso();
                        currentCurso.setNombre(currentCursoNombre);
                        System.out.println("Nuevo curso: " + currentCurso);

                    } else if (linea.startsWith("Alumno")) {
                        if (currentCurso != null) {
                            String[] alumnoParts = linea.split(":");
                            String nombreCompleto = alumnoParts[1].trim();

                            String[] nombreApellido = nombreCompleto.split(" ", 2);
                            String nombre = nombreApellido[0].trim();
                            String apellido = nombreApellido.length > 1 ? nombreApellido[1].trim() : "";

                            Alumno alumno = new Alumno();
                            alumno.setNombre(nombre);
                            alumno.setApellido(apellido);

                            currentCurso.addAlumno(alumno); 
                            session.save(alumno);
                            
                            System.out.println("Añadiendo alumno a curso: " + alumno + " " +alumno.getNombre() + " "+ alumno.getApellido());
                        }
                    }
                }

                if (currentCurso != null) {
                    session.save(currentCurso);
                    System.out.println("Guardando último curso: " + currentCurso);
                }

                transaction.commit();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }*/

    
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
                System.out.println("");
            }
        }
    }
    
    public void volcarBaseDeDatos() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            List<Curso> cursos = session.createQuery("FROM Curso", Curso.class).getResultList();

            XStream xstream = new XStream(new DomDriver()) {
                @Override
                protected MapperWrapper wrapMapper(MapperWrapper next) {
                    return new MapperWrapper(next) {
                        @Override
                        public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                            if (definedIn == Object.class) {
                                return false;
                            }
                            return super.shouldSerializeMember(definedIn, fieldName);
                        }
                    };
                }
            };
            xstream.alias("BaseDeDatos", List.class);
            xstream.alias("Curso", Curso.class);
            xstream.alias("Alumno", Alumno.class);
            String xml = xstream.toXML(cursos);

            // Escribir el XML en un archivo
            try (PrintWriter writer = new PrintWriter(new File("pruebas.xml"))) {
                writer.print(xml);
            }

            transaction.commit();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //transaction.rollback();
        }
    }
    
    
    /*
    public void volcarBaseDeDatos() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try (PrintWriter writer = new PrintWriter(new File("pruebas.xml"))) {
                List<Curso> cursos = session.createQuery("FROM Curso", Curso.class).getResultList();

                // Create a wrapper class to hold the list of Curso objects
                CursosWrapper cursosWrapper = new CursosWrapper();
                cursosWrapper.setCursos(cursos);

                JAXBContext context = JAXBContext.newInstance(CursosWrapper.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                // Marshal the CursosWrapper object into XML and write it to the file
                marshaller.marshal(cursosWrapper, writer);

                transaction.commit();
            } catch (FileNotFoundException | JAXBException e) {
                e.printStackTrace();
                transaction.rollback();
            }
        }
    }*/
    
   /* public void volcarBaseDeDatos() {
    	
    	 
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try (PrintWriter writer = new PrintWriter(new File("pruebas.xml"))) {
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
    }*/
}
