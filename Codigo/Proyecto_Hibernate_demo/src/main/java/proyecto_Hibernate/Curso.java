package proyecto_Hibernate;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Curso {
    @Id
    private int id;
    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "curso")
    private Set<Alumno> alumnos;

    // Constructores, getters y setters
}