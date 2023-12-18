package proyecto_Hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@Entity
@Table(name = "Alumno", schema = "examen")
@XmlRootElement
//@XStreamAlias("Alumno")
@XmlAccessorType
@XmlType(propOrder = {"id", "nombre", "apellido", "curso"})
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String apellido;
    
    
    @XmlIDREF
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "curso_id")
    private Curso curso;

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

    @XmlElement
    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @XmlInverseReference(mappedBy = "alumnos")
    @XmlElement(name = "Curso")  
    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    @XmlTransient
    //@XmlElement(name = "Alumnos")
    public List<Alumno> getAlumnos() {
        // Exclude the reference to the Curso object to avoid the cycle
        return new ArrayList<>();
    }
    
    /* @XmlElement(name = "Alumnos")
    public List<Alumno> getAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        alumnos.add(this);
        return alumnos;
    }*/
}
