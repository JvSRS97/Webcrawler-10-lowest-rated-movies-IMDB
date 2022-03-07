package jvreis.WebcrawlerLowestRatedMoviesIMDB.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import jvreis.WebcrawlerLowestRatedMoviesIMDB.classes.Filme;

public class WebcrawlerConfiguration {// obs.: editar caminho do arquivo em caso de mudan�a de pasta
	private String path = "C:\\Users\\jvict\\eclipse-workspace\\webcrawler-lowestmovies-imdb\\Webcrawler-10-lowest-rated-movies-IMDB.txt";
	private OutputStreamWriter writer;
	private String resultadoTXT;
	private String resultadoHTML;
		
	// -------------------------
	public String getResultadoTXT() {
		return resultadoTXT;
	}

	public void setResultadoTXT(String resultadoTXT) {
		this.resultadoTXT = resultadoTXT;
	}

	public String getResultadoHTML() {
		return resultadoHTML;
	}

	public void setResultadoHTML(String resultadoHTML) {
		this.resultadoHTML = resultadoHTML;
	}
	// -------------------------
	
	//Construtor que já realizar a extração de dados do IMDB e salva em um objeto do tipo WebcrawlerConfiguration
	public WebcrawlerConfiguration() {
		List<String> resultado = new ArrayList<String>();
		resultado = executar();
		this.resultadoTXT = resultado.get(0);
		this.resultadoHTML = resultado.get(1);
	}

	// ------------------------------------------------------------------

	private List<String> executar() {
		List<Filme> dados;
		List<String> resultado = new ArrayList<String>();

		dados = Webcrawler.search();
		resultado.add(converterMovieListEmTXT(dados));
		resultado.add(converterMovieListEmHTML(dados));
				
		escreverArquivo(resultado.get(0), path);
		System.out.println(resultado.get(0));
		
		return resultado;
	}
	
	// ------------------------------------------------------------------
	private void escreverArquivo(String texto, String caminho) {
		try {
			File arquivo = new File(caminho);
			if (!arquivo.exists()) {
				arquivo.createNewFile();
			}
			writer = new OutputStreamWriter(new FileOutputStream(arquivo));
			writer.write(texto);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String converterMovieListEmTXT(List<Filme> filmes) {// convertendo lista em texto numa única variável
		String txt = "Created by João Victor Reis\n\n";
		int i, id;
		List<String> diretores, elencoPrincipal;
		String tituloPortugues, tituloIngles, link, imdbRating1, imdbRating2, ano, comentarioLink,
				comentarioTexto;
		for (i = 0; i < filmes.size(); i++) {
			id = filmes.get(i).getId();
			tituloPortugues = filmes.get(i).getTituloPortugues();
			tituloIngles = filmes.get(i).getTituloIngles();
			link = filmes.get(i).getLink();
			imdbRating1 = filmes.get(i).getImdbRating1();
			imdbRating2 = filmes.get(i).getImdbRating2();
			ano = filmes.get(i).getAno();
			diretores = filmes.get(i).getDiretores();
			elencoPrincipal = filmes.get(i).getElencoPrincipal();
			comentarioLink = filmes.get(i).getComentarioLink();
			comentarioTexto = filmes.get(i).getComentarioTexto();

			txt += "ID: "+id+";\nTitulo portugues: " + tituloPortugues + ";\nTitulo ingles: " + tituloIngles + ";\nLink: " + link
					+ ";\nIMDB 1: " + imdbRating1 + ";\nIMDB 2: " + imdbRating2 + ";\nAno: " + ano
					+ ";\nDiretores: " + diretores + ";\nElenco principal: " + elencoPrincipal + ";\nLink do comentario: "
					+ comentarioLink + ";\nTexto do comentario: " + comentarioTexto + ".\n\n";
		}
		return txt;
	}
	
	private String converterMovieListEmHTML(List<Filme> filmes) {// convertendo lista em texto numa única variável
		String html;
		int i, id;
		List<String> diretores, elencoPrincipal;
		String tituloPortugues, tituloIngles, link, imdbRating1, imdbRating2, ano, comentarioLink,
				comentarioTexto;
		html = "<html> <body> <pre> <title>Lista dos 10 filmes com pior nota IMDB</title>";
		html += "<font size=7><center><b>Lista dos 10 filmes com pior nota <a href=https://www.imdb.com/chart/bottom target=\\\"_blank\\\">IMDB</a></b></center></font>";
		html += "<br><font color=Red>Created by</font> <font color=Blue>João Victor Reis<br><br></font>";
		for (i = 0; i < filmes.size(); i++) {
			id = filmes.get(i).getId();
			tituloPortugues = filmes.get(i).getTituloPortugues();
			tituloIngles = filmes.get(i).getTituloIngles();
			link = "<a href="+filmes.get(i).getLink()+" target=\"_blank\">Movie Link</a>";;
			imdbRating1 = filmes.get(i).getImdbRating1();
			imdbRating2 = filmes.get(i).getImdbRating2();
			ano = filmes.get(i).getAno();
			diretores = filmes.get(i).getDiretores();
			elencoPrincipal = filmes.get(i).getElencoPrincipal();
			comentarioLink = "<a href="+filmes.get(i).getComentarioLink()+" target=\"_blank\">Comment Link</a>";
			comentarioTexto = filmes.get(i).getComentarioTexto();

			html += "ID: "+id+";<br>Titulo portugues: " + tituloPortugues + ";<br>Titulo ingles: " + tituloIngles + ";<br>Link: " + link+ ";<br>IMDB 1: " + imdbRating1 + ";<br>IMDB 2: " + imdbRating2 + ";<br>Ano: " + ano
					+ ";<br>Diretores: " + diretores + ";<br>Elenco principal: " + elencoPrincipal + ";<br>Link do comentario: "
					+comentarioLink+";<br>Texto do comentario: " + comentarioTexto + "<br><br>";
		}
		html += "</pre> </body> </html>";
		return html;
	}
}