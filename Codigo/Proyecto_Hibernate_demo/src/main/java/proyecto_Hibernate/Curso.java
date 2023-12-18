package proyecto_Hibernate;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlElement;

import jakarta.xml.bind.annotation.XmlRootElement;


import org.eclipse.persistence.oxm.annotations.XmlInverseReference;


@Entity
@Table(name = "Curso", schema = "examen")
@XmlRootElement(name = "BaseDeDatos")
public class Curso {
    
    public Curso() {
        this.alumnos = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    
    
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<Alumno> alumnos = new ArrayList<>();

    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlInverseReference(mappedBy = "curso")
    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public void addAlumno(Alumno alumno) {
        if (alumno != null) {
            this.alumnos.add(alumno);
            alumno.setCurso(this);
        }
    }

    /*
    @XmlElement(name = "Cursos")
    public List<Curso> getCursos() {
        List<Curso> cursos = new ArrayList<>();
        cursos.add(this);
        return cursos;
    }*/
}
