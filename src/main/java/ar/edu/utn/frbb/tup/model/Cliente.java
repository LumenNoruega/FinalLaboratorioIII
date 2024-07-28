package ar.edu.utn.frbb.tup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ar.edu.utn.frbb.tup.controller.ClienteDto;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

public class Cliente extends Persona {

    private Long id;
    private String tipoPersona;
    private String banco;
    private LocalDate fechaAlta;

    @JsonIgnore
    private Set<Cuenta> cuentas = new HashSet<>();

    public Cliente() {
        super();
    }

    public Cliente(ClienteDto clienteDto) {
        super(clienteDto.getDni(), clienteDto.getApellido(), clienteDto.getNombre(), clienteDto.getFechaNacimiento());
        fechaAlta = LocalDate.now();
        banco = clienteDto.getBanco();
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona2) {
        this.tipoPersona = tipoPersona2;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Set<Cuenta> getCuentas() {
        return cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
        cuenta.setTitular(this);
    }

    public boolean tieneCuenta(TipoCuenta tipoCuenta, TipoMoneda moneda) {
        for (Cuenta cuenta : cuentas) {
            if (tipoCuenta.equals(cuenta.getTipoCuenta()) && moneda.equals(cuenta.getMoneda())) {
                return true;
            }
        }
        return false;
    }

    public int getEdad() {
        if (this.getFechaNacimiento() != null) {
            return Period.between(this.getFechaNacimiento(), LocalDate.now()).getYears();
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "tipoPersona=" + tipoPersona +
                ", banco='" + banco + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", cuentas=" + cuentas +
                '}';
    }
}
