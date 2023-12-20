package proyecto_Hibernate;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Curso", schema = "examen")
@XmlRootElement(name = "BaseDeDatos")
@XmlAccessorType(XmlAccessType.FIELD)
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @XmlElement(name = "Nombre")
    private String nombre;

    @OneToMany(mappedBy = "curso")
    @XmlElementWrapper(name="Alumnos")
    private List<Alumno> Alumno = new ArrayList<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Alumno> getAlumnos() {
        return Alumno;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.Alumno = alumnos;
    }

    public void addAlumno(Alumno alumno) {
        if (alumno != null) {
            this.Alumno.add(alumno);
            alumno.setCurso(this);
        }
    }
}