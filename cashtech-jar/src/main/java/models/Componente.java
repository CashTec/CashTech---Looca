package models;

public class Componente {

    private Integer id;
    private String tipo;

    private String modelo;

    private String serie;

    private Integer qtd_maxima;

    private Double frequencia;

    private Integer qtd_cpu_fisica;

    private Integer caixa_eletronico_fk;


    public Componente(Integer id, String tipo, String modelo, String serie, Integer qtd_maxima, Double frequencia, Integer qtd_cpu_fisica, Integer caixa_eletronico_fk) {
        this.id = id;
        this.tipo = tipo;
        this.modelo = modelo;
        this.serie = serie;
        this.qtd_maxima = qtd_maxima;
        this.frequencia = frequencia;
        this.qtd_cpu_fisica = qtd_cpu_fisica;
        this.caixa_eletronico_fk = caixa_eletronico_fk;
    }

    public Componente(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Integer getQtd_maxima() {
        return qtd_maxima;
    }

    public void setQtd_maxima(Integer qtd_maxima) {
        this.qtd_maxima = qtd_maxima;
    }

    public Double getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(Double frequencia) {
        this.frequencia = frequencia;
    }

    public Integer getQtd_cpu_fisica() {
        return qtd_cpu_fisica;
    }

    public void setQtd_cpu_fisica(Integer qtd_cpu_fisica) {
        this.qtd_cpu_fisica = qtd_cpu_fisica;
    }

    public Integer getCaixa_eletronico_fk() {
        return caixa_eletronico_fk;
    }

    public void setCaixa_eletronico_fk(Integer caixa_eletronico_fk) {
        this.caixa_eletronico_fk = caixa_eletronico_fk;
    }
}
