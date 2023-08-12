package co.com.boxercrossgym.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pago implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private Double valorPagado;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "fecha_pago")
	private Date fechaPago;
	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "fecha_vencimiento")
	private Date fechaVencimiento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Plan plan;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name =  "tp_id")
	private TipoPago tipoPago;

	@Override
	public String toString() {
		return "Pago [id=" + id + ", valorPagado=" + valorPagado + ", fechaPago=" + fechaPago + ", fechaVencimiento="
				+ fechaVencimiento + ", plan=" + plan + ", tipoPago=" + tipoPago + "]";
	}
	
	
	public boolean estadoPago() {
		Date fechaActual = new Date();
		if(this.fechaVencimiento == null) {
			return false;
		}
		if(fechaActual.before(this.fechaVencimiento)) {
			return true;
		}
		return false;
	}
	
	@PrePersist
	public void prePersist() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.fechaPago);
		calendar.add(Calendar.DAY_OF_MONTH, this.plan.getDuracion());
		
		this.fechaVencimiento = calendar.getTime();
		System.out.println(fechaVencimiento);
		
	}
	
	
	
	
	
	
	
	
	
}
