## Ejemplo aplicación ZK con patrón MVVM
* Crear una calculadora de enteros con un "historial" de las operaciones realizadas.
* Se usa el patrón MVVM (Model-View-ViewModel)

### Crear proyecto empleando arquetipos ZK
```
mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate \
             -DarchetypeCatalog=http://mavensync.zkoss.org/maven2/ \
             -DarchetypeGroupId=org.zkoss \
             -DarchetypeArtifactId=zk-archetype-webapp \
             -DgroupId=es.uvigo.mei \
             -DartifactId=zk-mvvm \
             -Dversion=1.0 \
             -Dpackage=es.uvigo.mei.ejemplozk
```

Se usa el arquetipo para aplicaciones web ZK (`zk-archetype-web`) del repositorio de ZK Framework
* El catálogo del arquetipos del ZK Framework no es compatible con las versiones más recientes del `maven-archetype-pluing`, por lo que se fuerza el uso de la versión 2.4 del mismo.

#### Ajustes en el proyecto
1. Ajustar la versión de Java usada por el plugin `maven-compiler-plugin` en el `pom.xml` generado (establecer la versión 1.8)
```
cd zk-mvvm
nano pom.xml
```
```xml
<projet>
   ...
   <build>
       <plugins>
          ...
          <!-- Compile java -->
          <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-compiler-plugin</artifactId>
              <version>2.3.2</version>
                  <configuration>
                       <source>1.8</source>
                       <target>1.8</target>
                  </configuration>
          </plugin>
          ...
       </plugins>
   <build>
   ...
</project>
```

2. Ajustar el descriptor de despliegue `web.xml` para hacer uso de la versión 3.0 de la especificación Servlet (se incluye una variante para Servlet 3.0, `web.servlet-3.xml`)
```
cd src/main/webapp/WEB-INF
mv web.servlet-3.xml web.xml
cd ../../../..
```

### Crear vista index.zul
```
nano src/main/webapp/index.zul
```

```xml
<zk>
	<window title="Calculadora ZK MVVM" border="normal" width="800px" 
            apply="org.zkoss.bind.BindComposer"
		    viewModel="@id('vm') @init('es.uvigo.mei.ejemplozk.CalculadoraViewModel')"
		    validationMessages="@id('vmsgs')">
		<vbox>
			<hbox>
				Operando 1:
				<textbox value="@bind(vm.operando1)" constraint="no empty" />
			</hbox>
			<hbox>
				Operando 2:
				<textbox value="@bind(vm.operando2)" constraint="no empty" />
			</hbox>
			<hbox>
				Resultado:
				<label value="@load(vm.resultado)" />
			</hbox>
		</vbox>

		<separator height="10px" />

		<hbox>
			<button label="Sumar" onClick="@command('sumar')" />
			<button label="Restar" onClick="@command('restar')" />
			<button label="Multiplicar" onClick="@command('multiplicar')" />
			<button label="Dividir" onClick="@command('dividir')" />
		</hbox>

		<separator height="10px" />

		Historial de operaciones

		<listbox model="@bind(vm.operaciones)"
			emptyMessage="No hay operaciones que mostrar">
			<listhead>
				<listheader label="Id" />
				<listheader label="Tipo" />
				<listheader label="Operando 1" />
				<listheader label="Operando 2" />
				<listheader label="Resultado" />
				<listheader />
			</listhead>
			<template name="model" var="operacion">
				<listitem>
					<listcell> <label value="@bind(operacion.id)"/> </listcell>
					<listcell> <label value="@bind(operacion.tipo)"/> </listcell>
					<listcell> <label value="@bind(operacion.operando1)"/> </listcell>
					<listcell> <label value="@bind(operacion.operando2)"/> </listcell>
					<listcell> <label value="@bind(operacion.resultado)"/> </listcell>
					<listcell>
						<button label="Eliminar"
							onClick="@command('eliminar', operacionEliminar=operacion)" />
					</listcell>
				</listitem>
			</template>
		</listbox>

		<separator height="10px" />

		<button label="Eliminar Todas"
			onClick="@command('eliminarTodas')" />
	</window>
</zk>
```
Descarga: [index.zul](https://github.com/mei2018si/zk-mvvm/raw/master/src/main/webapp/index.zul)

### Crear controlador CalculadoraViewModel.java
```
nano src/main/java/es/uvigo/mei/ejemplozk/CalculadoraViewModel.java
```

```java
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
```
Descarga: [CalculadoraViewModel.java](https://github.com/mei2018si/zk-mvvm/raw/master/src/main/java/es/uvigo/mei/ejemplozk/CalculadoraModelView.java)

### Crear clases complementarias
```
nano src/main/java/es/uvigo/mei/ejemplozk/TipoOperacion.java
```

```java
package es.uvigo.mei.ejemplozk;

public enum TipoOperacion {
	SUMA, RESTA, MULTIPLICACION, DIVISION
}
```
Descarga: [TipoOperacion.java](https://github.com/mei2018si/zk-mvvm/raw/master/src/main/java/es/uvigo/mei/ejemplozk/TipoOperacion.java)

```
nano src/main/java/es/uvigo/mei/ejemplozk/CalculadoraControlador.java
```

```java
package es.uvigo.mei.ejemplozk;


public class Operacion {
	private int id;
	private TipoOperacion tipo;
	private int operando1;
	private int operando2;
	private int resultado;
	
	public Operacion(int id, TipoOperacion tipo, int operando1, int operando2, int resultado) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.operando1 = operando1;
		this.operando2 = operando2;
		this.resultado = resultado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TipoOperacion getTipo() {
		return tipo;
	}

	public void setTipo(TipoOperacion tipo) {
		this.tipo = tipo;
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

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Operacion other = (Operacion) obj;

		return id == other.id;
	}

	@Override
	public String toString() {
		return "Operacion [id=" + id + ", " + tipo + "(" + operando1 + ", " + operando2 + ") = " + resultado + "]";
	}
}
```

Descarga: [Operacion.java](https://github.com/mei2018si/zk-mvvm/raw/master/src/main/java/es/uvigo/mei/ejemplozk/Operacion.java)

### Compilar y ejecutar el proyecto con maven
```
mvn install
mvn jetty:run
```

El despligue de la aplicación ZK requiere disponer de un servidor de aplicaciones Java EE (realmente basta un contenedor de Servlet compatible con la versión 3.0)
* El fichero `pom.xml` declara el uso del plugin `jetty-maven-plugin` 
* Este plugin ofrece el goal `jetty:run` que descarga un contenedor de servlets ligero Jetty y depliega la aplicación ZK sobre el mismo.
* Más información: [jetty-maven-plugin](https://www.eclipse.org/jetty/documentation/9.4.x/jetty-maven-plugin.html)

La aplicación desplegada estará disponible en la URL `http://localhost:8080/zk-mvvm`

### Proyecto resultante
Disponible en github: [https://github.com/mei2018si/zk-mvvm](https://github.com/mei2018si/zk-mvvm)

```
git clone https://github.com/mei2018si/zk-mvvm.git
```

Puede importase en Eclipse (ver [zk-en-eclipse](https://github.com/mei2018si/documentos-si/blob/master/zk-en-eclipse.md))

