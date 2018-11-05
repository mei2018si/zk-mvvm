package es.uvigo.mei.ejemplozk;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

public class CalculadoraViewModel {

	private int contadorOperaciones;
	private int operando1;
	private int operando2;
	private int resultado;
	private List<Operacion> operaciones;

	@Init
	public void init() {
		contadorOperaciones = 0;
		operaciones = new ArrayList<>();
	}

	public int getOperando1() {
		return operando1;
	}

	public void setOperando1(int operando1) {
		this.operando1 = operando1;
	}

	public int getOperando2() {
		return operando2;
	}

	public void setOperando2(int operando2) {
		this.operando2 = operando2;
	}

	public int getResultado() {
		return resultado;
	}

	public void setResultado(int resultado) {
		this.resultado = resultado;
	}

	public List<Operacion> getOperaciones() {
		return operaciones;
	}

	public void setOperaciones(List<Operacion> operaciones) {
		this.operaciones = operaciones;
	}


	@Command
	@NotifyChange({ "resultado", "operaciones" })
	public void sumar() {
		resultado = operando1 + operando2;
		registrarOperacion(TipoOperacion.SUMA);
	}

	@Command
	@NotifyChange({ "resultado", "operaciones" })
	public void restar() {
		resultado = operando1 - operando2;
		registrarOperacion(TipoOperacion.RESTA);
	}

	@Command
	@NotifyChange({ "resultado", "operaciones" })
	public void multiplicar() {
		resultado = operando1 * operando2;
		registrarOperacion(TipoOperacion.MULTIPLICACION);
	}

	@Command
	@NotifyChange({ "resultado", "operaciones" })
	public void dividir() {
		resultado = operando1 / operando2;
		registrarOperacion(TipoOperacion.DIVISION);
	}

	private void registrarOperacion(TipoOperacion tipo) {
		contadorOperaciones++;
		operaciones.add(new Operacion(contadorOperaciones, tipo, operando1, operando2, resultado));
	}

	@Command
	@NotifyChange("operaciones")
	public void eliminar(@BindingParam("operacionEliminar") Operacion operacion) {
		if (operacion != null) {
			operaciones.remove(operacion);
		}
	}

	@Command
	@NotifyChange("operaciones")
	public void eliminarTodas() {
		operaciones.clear();
		contadorOperaciones = 0;
	}
}
