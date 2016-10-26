/**
 * 	MADKing - Genera e invia tramite PEC moduli di MAD per supplenze nelle scuole itailane
 *  (No chance you need this program and do not speak Italian!)
 *  
 *   Copyright (C) 2016  Davide Gatto
 *   
 *   @author Davide Gatto
 *   @mail davgatto@gmail.com
 *   
 *   This file is part of MADKing
 *   MADKing is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
