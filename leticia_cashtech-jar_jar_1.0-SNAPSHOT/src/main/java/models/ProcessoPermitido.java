/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDateTime;

/**
 *
 * @author murilo
 */
public class ProcessoPermitido {

    private Integer id;
    private Integer caixa_eletronico_id;
    private String nome;
    private Integer pid;
    private Double uso_cpu;
    private Double uso_memoria;
    private Integer byte_utilizado;
    private Double memoria_virtual_ultilizada;
    private Integer id_dead;
    private LocalDateTime dt_processo;

    public ProcessoPermitido(Integer id, Integer caixa_eletronico_id, String nome, Integer pid, Double uso_cpu, Double uso_memoria, Integer byte_utilizado, Double memoria_virtual_ultilizada, Integer id_dead, LocalDateTime dt_processo) {
        this.id = id;
        this.caixa_eletronico_id = caixa_eletronico_id;
        this.nome = nome;
        this.pid = pid;
        this.uso_cpu = uso_cpu;
        this.uso_memoria = uso_memoria;
        this.byte_utilizado = byte_utilizado;
        this.memoria_virtual_ultilizada = memoria_virtual_ultilizada;
        this.id_dead = id_dead;
        this.dt_processo = dt_processo;
    }

    public ProcessoPermitido() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaixa_eletronico_id() {
        return caixa_eletronico_id;
    }

    public void setCaixa_eletronico_id(Integer caixa_eletronico_id) {
        this.caixa_eletronico_id = caixa_eletronico_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Double getUso_cpu() {
        return uso_cpu;
    }

    public void setUso_cpu(Double uso_cpu) {
        this.uso_cpu = uso_cpu;
    }

    public Double getUso_memoria() {
        return uso_memoria;
    }

    public void setUso_memoria(Double uso_memoria) {
        this.uso_memoria = uso_memoria;
    }

    public Integer getByte_utilizado() {
        return byte_utilizado;
    }

    public void setByte_utilizado(Integer byte_utilizado) {
        this.byte_utilizado = byte_utilizado;
    }

    public Double getMemoria_virtual_ultilizada() {
        return memoria_virtual_ultilizada;
    }

    public void setMemoria_virtual_ultilizada(Double memoria_virtual_ultilizada) {
        this.memoria_virtual_ultilizada = memoria_virtual_ultilizada;
    }

    public Integer getId_dead() {
        return id_dead;
    }

    public void setId_dead(Integer id_dead) {
        this.id_dead = id_dead;
    }

    public LocalDateTime getDt_processo() {
        return dt_processo;
    }

    public void setDt_processo(LocalDateTime dt_processo) {
        this.dt_processo = dt_processo;
    }

}
