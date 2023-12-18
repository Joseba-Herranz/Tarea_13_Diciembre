package proyecto_Hibernate;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

//import org.eclipse.persistence.oxm.annotations.XmlInverseReference;




@Entity
@Table(name = "Alumno", schema = "examen")
@XmlRootElement
@XmlAccessorType //( XmlAccessType.FIELD )
@XmlType(propOrder = {"id", "nombre", "apellido", "curso"})
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String apellido;
    
    
    //@XmlIDREF
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "curso_id")
    //@XmlTransient
    private Curso curso;

    //@XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //@XmlElement
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    //@XmlElement
    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    //@XmlInverseReference(mappedBy = "alumnos")
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

        return curso.getAlumnos();
    }
    
    /* @XmlElement(name = "Alumnos")
    public List<Alumno> getAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        alumnos.add(this);
        return alumnos;
    }*/
}
