/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import com.github.britooo.looca.api.group.memoria.Memoria;

/**
 *
 * @author kanaiama
 */
public class Parametrizacao {
    private Integer id;
    private Integer empresa_id;
    private Integer qtd_cpu_max;
    private Long qtd_bytes_enviado_max;
    private Long qtd_bytes_recebido_max;
    private Long qtd_memoria_max;
    private Long qtd_disco_max;

    public Parametrizacao(Integer id, Integer empresa_id, Integer qtd_cpu_max,
            Long qtd_bytes_enviado_max, Long qtd_bytes_recebido_max, 
            Long qtd_memoria_max, Long qtd_disco_max) {
        this.id = id;
        this.empresa_id = empresa_id;
        this.qtd_cpu_max = qtd_cpu_max;
        this.qtd_bytes_enviado_max = qtd_bytes_enviado_max;
        this.qtd_bytes_recebido_max = qtd_bytes_recebido_max;
        this.qtd_memoria_max = qtd_memoria_max;
        this.qtd_disco_max = qtd_disco_max;
    }

    public Parametrizacao() {
    
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmpresa_id() {
        return empresa_id;
    }

    public void setEmpresa_id(Integer empresa_id) {
        this.empresa_id = empresa_id;
    }

    public Integer getQtd_cpu_max() {
        return qtd_cpu_max;
    }

    public void setQtd_cpu_max(Integer qtd_cpu_max) {
        this.qtd_cpu_max = qtd_cpu_max;
    }

    public Long getQtd_bytes_enviado_max() {
        return qtd_bytes_enviado_max;
    }

    public void setQtd_bytes_enviado_max(Long qtd_bytes_enviado_max) {
        this.qtd_bytes_enviado_max = qtd_bytes_enviado_max;
    }

    public Long getQtd_bytes_recebido_max() {
        return qtd_bytes_recebido_max;
    }

    public void setQtd_bytes_recebido_max(Long qtd_bytes_recebido_max) {
        this.qtd_bytes_recebido_max = qtd_bytes_recebido_max;
    }

    public Long getQtd_memoria_max() {
        return qtd_memoria_max;
    }

    public void setQtd_memoria_max(Long qtd_memoria_max) {
        this.qtd_memoria_max = qtd_memoria_max;
    }

    public Long getQtd_disco_max() {
        return qtd_disco_max;
    }

    public void setQtd_disco_max(Long qtd_disco_max) {
        this.qtd_disco_max = qtd_disco_max;
    }

    public double getQtd_memoria_max(Memoria memoria) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}
