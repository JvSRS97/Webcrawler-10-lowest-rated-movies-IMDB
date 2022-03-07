package jvreis.WebcrawlerLowestRatedMoviesIMDB.classes;

import java.util.ArrayList;
import java.util.List;

public class Filme {
	public int id;
	public String tituloPortugues;
	public String tituloIngles;
	public String link;
	public String imdbRating1;
	public String imdbRating2;
	public String ano;
	public List<String> diretores = new ArrayList();
	public List<String> elencoPrincipal = new ArrayList();;
	public String comentarioLink;
	public String comentarioTexto = "";

	public Filme(int id, String tituloPortugues, String tituloIngles, String link, String imdbRating1, String imdbRating2, String ano, List<String> diretores, List<String> elencoPrincipal, String comentarioLink, String comentarioTexto) {
    	this.id = id;
		this.tituloPortugues = tituloPortugues;
    	this.tituloIngles = tituloIngles;
    	this.link = link;
    	this.imdbRating1 = imdbRating1;
    	this.imdbRating2 = imdbRating2;
    	this.ano = ano;
    	this.diretores = diretores;
    	this.elencoPrincipal = elencoPrincipal;
    	this.comentarioLink = comentarioLink;
    	this.comentarioTexto = comentarioTexto;
    }

	public Filme() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTituloPortugues() {
		return tituloPortugues;
	}

	public void setTituloPortugues(String tituloPortugues) {
		this.tituloPortugues = tituloPortugues;
	}

	public String getTituloIngles() {
		return tituloIngles;
	}

	public void setTituloIngles(String tituloIngles) {
		this.tituloIngles = tituloIngles;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImdbRating1() {
		return imdbRating1;
	}

	public void setImdbRating1(String imdbRating1) {
		this.imdbRating1 = imdbRating1;
	}

	public String getImdbRating2() {
		return imdbRating2;
	}

	public void setImdbRating2(String imdbRating2) {
		this.imdbRating2 = imdbRating2;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public List<String> getDiretores() {
		return diretores;
	}

	public void setDiretores(List<String> diretores) {
		this.diretores = diretores;
	}

	public List<String> getElencoPrincipal() {
		return elencoPrincipal;
	}

	public void setElencoPrincipal(List<String> elencoPrincipal) {
		this.elencoPrincipal = elencoPrincipal;
	}

	public String getComentarioLink() {
		return comentarioLink;
	}

	public void setComentarioLink(String comentarioLink) {
		this.comentarioLink = comentarioLink;
	}

	public String getComentarioTexto() {
		return comentarioTexto;
	}

	public void setComentarioTexto(String comentarioTexto) {
		this.comentarioTexto = comentarioTexto;
	}

}
