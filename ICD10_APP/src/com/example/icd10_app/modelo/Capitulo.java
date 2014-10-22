package com.example.icd10_app.modelo;

import java.util.ArrayList;

public class Capitulo {

	private String nome;
	private String cod;
	//private ArrayList<Bloco> listaBloco;

	public Capitulo() {}

	public Capitulo(String nome, String cod) {
		super();
		this.nome = nome;
		this.cod = cod;
		//this.listaBloco = listaBloco;
	}

	@Override
	public String toString() {
		return cod+" " + nome ;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}
}
