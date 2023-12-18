package proyecto_Hibernate;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

@Entity
@Table(name = "Curso", schema = "examen")
@XmlRootElement(name = "BaseDeDatos")
//@XStreamAlias("Curso")
public class Curso {
    
    public Curso() {
        this.alumnos = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    
    @XmlTransient
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<Alumno> alumnos;

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
