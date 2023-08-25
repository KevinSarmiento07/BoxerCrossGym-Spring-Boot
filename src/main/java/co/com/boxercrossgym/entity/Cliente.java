package co.com.boxercrossgym.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@AllArgsConstructor
public class Cliente implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	private String nombre;
	
	@NotEmpty
	private String apellido;
	
	@NotNull
	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern =  "yyyy-MM-dd")
	@Column(name = "fecha_nacimiento")
	private Date fechaNacimiento;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern =  "yyyy-MM-dd")
	@Column(name = "fecha_inscripcion")
	private Date fechaInscripcion;
	
	@NotEmpty
	private String sexo;
	
	@NotEmpty
	@Size(max = 10, message = "El telefono solo puede tener un maximo de 10 digitos")
	private String telefono;
	
	@NotEmpty
	@Column(length = 100,unique = true)
	private String username;
	
	@Column(length = 60)
	private String password;
	
	private boolean enabled;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "cliente_id")
	private List<Role> roles;
	
	private String antecedente;
	
	@NotEmpty
	private String cedula;
	
	@JsonIgnore
	@OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
	private List<Pago> pagos;
	
	
	public void addPago(Pago pago) {
		pagos.add(pago);
	}
	
	public void addRole(Role role) {
		roles.add(role);
	}
	
	public Cliente() {
		this.pagos = new ArrayList<>();
		this.roles = new ArrayList<Role>();
	}
	
	public Pago ultimoPago() {
		if(this.pagos != null && !this.pagos.isEmpty()) {
			return this.pagos.get(pagos.size()-1);
		}
		return null;
	}
	
	@JsonIgnore
	public boolean getEstado() {
		
		Pago pago = ultimoPago();
		
		if(pago != null) {
			Date fechaVencimento = pago.getFechaVencimiento();
			Date fecha = new Date();
			if(fecha.before(fechaVencimento)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", fechaNacimiento="
				+ fechaNacimiento + ", fechaInscripcion=" + fechaInscripcion + ", sexo=" + sexo + ", telefono="
				+ telefono + ", email=" + username + ", antecedente=" + antecedente + ", cedula=" + cedula + "]";
	}
	
	
	
	
	
	
}
