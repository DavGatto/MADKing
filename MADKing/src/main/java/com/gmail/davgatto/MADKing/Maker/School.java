package com.gmail.davgatto.MADKing.Maker;

import javax.json.JsonObject;

public class School {

	private String codMec;
	private String nome;
	private String comune;
	private String provincia;
	private String tipo;

	public String getCodMec() {
		return codMec;
	}

	public void setCodMec(String codMec) {
		this.codMec = codMec;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public School(JsonObject jso) {

		setCodMec(jso.getString("codMec"));
		setNome(jso.getString("nome"));
		setComune(jso.getString("comune"));
		setProvincia(jso.getString("provincia"));
		setTipo(jso.getString("tipo"));
	}
}
