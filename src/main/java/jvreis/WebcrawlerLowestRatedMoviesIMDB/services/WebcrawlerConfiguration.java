package jvreis.WebcrawlerLowestRatedMoviesIMDB.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import jvreis.WebcrawlerLowestRatedMoviesIMDB.classes.Filme;

public class WebcrawlerConfiguration {// obs.: editar caminho do arquivo em caso de mudan�a de pasta
	private String path = "C:\\Users\\jvict\\eclipse-workspace\\webcrawler-lowest-rated-movies-imdb\\Webcrawler-10-lowest-rated-movies-IMDB.txt";
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
		List<String> diretoresLista, elencoPrincipalLista;
		String tituloPortugues, tituloIngles, link, imdbRating1, imdbRating2, ano, diretores, elencoPrincipal, comentarioLink,
				comentarioTexto;
		for (i = 0; i < filmes.size(); i++) {
			id = filmes.get(i).getId();
			tituloPortugues = filmes.get(i).getTituloPortugues();
			tituloIngles = filmes.get(i).getTituloIngles();
			link = filmes.get(i).getLink();
			imdbRating1 = filmes.get(i).getImdbRating1();
			imdbRating2 = filmes.get(i).getImdbRating2();
			ano = filmes.get(i).getAno();
			
			diretoresLista = filmes.get(i).getDiretores();
			diretores = pularLinhaEmListaParaTXT(diretoresLista);	
			
			elencoPrincipalLista = filmes.get(i).getElencoPrincipal();
			elencoPrincipal = pularLinhaEmListaParaTXT(elencoPrincipalLista);
			
			
			comentarioLink = filmes.get(i).getComentarioLink();
			comentarioTexto = "\""+filmes.get(i).getComentarioTexto()+"\"";
			comentarioTexto = pularLinhaEmComentarioTXT(comentarioTexto);

			txt += "-> ID: "+id+";\n-> Titulo portugues: " + tituloPortugues + ";\n-> Titulo ingles: " + tituloIngles + ";\n-> Link: " + link
					+ ";\n-> IMDB 1: " + imdbRating1 + ";\n-> IMDB 2: " + imdbRating2 + ";\nAno: " + ano
					+ ";\n-> Diretores: " + diretores + ";\n-> Elenco principal: " + elencoPrincipal + ";\n-> Link do comentario: "
					+ comentarioLink + ";\n-> Texto do comentario: " + comentarioTexto + ".\n\n";
		}
		return txt;
	}
	
	private String converterMovieListEmHTML(List<Filme> filmes) {// convertendo lista em texto numa única variável
		String html;
		int i, id;
		List<String> diretoresLista, elencoPrincipalLista;
		String tituloPortugues, tituloIngles, link, imdbRating1, imdbRating2, ano, diretores, elencoPrincipal, comentarioLink,
				comentarioTexto;
		html = "<!DOCTYPE html><html> <head></head> <body style=\"background-color:#FAEBD7;color:black;\"> <title>Lista dos 10 filmes com pior nota IMDB</title>"; 
		html += "<font size=7><center><b style=\"color:#AFAFAF;\">Lista dos 10 filmes com pior nota <a href=https://www.imdb.com/chart/bottom target=\\\"_blank\\\">IMDB</a></b></center></font>";
		html += "<br><font color=Red>Created by</font> <font color=Blue>João Victor Reis<br><br></font><pre>";
		for (i = 0; i < filmes.size(); i++) {
			id = filmes.get(i).getId();
			tituloPortugues = filmes.get(i).getTituloPortugues();
			tituloIngles = filmes.get(i).getTituloIngles();
			link = "<a href="+filmes.get(i).getLink()+" target=\"_blank\">Movie Link</a>";;
			imdbRating1 = filmes.get(i).getImdbRating1();
			imdbRating2 = filmes.get(i).getImdbRating2();
			ano = filmes.get(i).getAno();
			
			diretoresLista = filmes.get(i).getDiretores();
			diretores = pularLinhaEmListaParaHTML(diretoresLista);	
			
			elencoPrincipalLista = filmes.get(i).getElencoPrincipal();
			elencoPrincipal = pularLinhaEmListaParaHTML(elencoPrincipalLista);
			
			comentarioLink = "<a href="+filmes.get(i).getComentarioLink()+" target=\"_blank\">Comment Link</a>";
			comentarioTexto = "\""+filmes.get(i).getComentarioTexto()+"\"";
			comentarioTexto = pularLinhaEmComentarioHTML(comentarioTexto);
			html += "<font color=Red>-></font> ID: "+id+";<br><font color=Blue>-></font> Titulo portugues: " + tituloPortugues + ";<br><font color=Blue>-></font> Titulo ingles: " + tituloIngles + ";<br><font color=Blue>-></font> Link: " + link+ ";<br><font color=Blue>-></font> IMDB 1: " + imdbRating1 + ";<br><font color=Blue>-></font> IMDB 2: " + imdbRating2 + ";<br><font color=Blue>-></font> Ano: " + ano
					+ ";<br><font color=Blue>-></font> Diretores: " + diretores + ";<br><font color=Blue>-></font> Elenco principal: " + elencoPrincipal + ";<br><font color=Blue>-></font> Link do comentario: "
					+comentarioLink+";<br><font color=Blue>-></font> Texto do comentario: " + comentarioTexto +"<br><br>";
		}
		html += "</pre> </body> </html>";
		return html;
	}
	
	private String pularLinhaEmComentarioTXT(String comentario) {
		int contador=0;
		String resultadoComentario = "";
		
		for(int i=0;i<comentario.length();i++) {
			resultadoComentario += comentario.charAt(i);
			if((comentario.charAt(i)==32)){//char espaço = 32
				contador++;
				if((contador%22)==0) {
					resultadoComentario += "\n";
				}
			}
		}	
		return resultadoComentario;
	}
	
	private String pularLinhaEmComentarioHTML(String comentario) {
		int contador=0;
		String resultadoComentario = "";
		
		for(int i=0;i<comentario.length();i++) {
			resultadoComentario += comentario.charAt(i);
			if((comentario.charAt(i)==32)){//char espaço = 32
				contador++;
				if((contador%22)==0) {
					resultadoComentario += "<br>";
				}
			}
		}	
		return resultadoComentario;
	}
	
	private String pularLinhaEmListaParaTXT(List<String> lista) {
		String resultado = "";
		
		for(int i=0;i < lista.size();i++) {
			if(i==(lista.size()-1)) {
				resultado += lista.get(i);	
			}else {
				resultado += lista.get(i)+", ";
			}
			if(((i%8)==0) && (i>0)){
					resultado += "\n";
				}
		}	
		return resultado;
	}
	
	private String pularLinhaEmListaParaHTML(List<String> lista) {
		String resultado = "";
		
		for(int i=0;i < lista.size();i++) {
			if(i==(lista.size()-1)) {
				resultado += lista.get(i);	
			}else {
				resultado += lista.get(i)+", ";
			}
			if(((i%8)==0) && (i>0)){
					resultado += "<br>";
				}
		}	
		return resultado;
	}
}