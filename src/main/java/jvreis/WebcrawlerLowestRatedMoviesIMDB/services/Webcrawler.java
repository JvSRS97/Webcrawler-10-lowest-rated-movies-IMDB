package jvreis.WebcrawlerLowestRatedMoviesIMDB.services;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;//importando biblioteca Jsoup
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jvreis.WebcrawlerLowestRatedMoviesIMDB.classes.Filme;

//Para melhor entendimento do código, favor verificar arquivo "Ideia para webcrawler IMDB - lowest rated movies.txt"

public class Webcrawler {
//-------------------------------------------------
	// retorna uma lista de descrição dos Filmes (olhar classe Filme)
	public static List<Filme> search() {
		List<Filme> filmes;
		filmes = new ArrayList<>();

		// URL do site a ser acessado
		String link = ("https://www.imdb.com/chart/bottom");
		String link2 = ("https://www.imdb.com");
		Document documento = null;

		// Primeiro Passo
		// --------------------------------------------------
		filmes = extrair10PioresFilmes(link, link2);
		// --------------------------------------------------

		// Segundo Passo
		// --------------------------------------------------
		System.out.print("\nRealizando requisição para URL de cada filme e salvando informações...\nProcessando...\nPor favor, aguarde!\n\t  Loading\n\t[");
		int contador = 1;
		for (Filme filme : filmes) {
			documento = getHTML(filme.link);
			filme = extrairInformacoesDoFilme(documento, filme, link2, contador);
			contador++;
		}
		return filmes;
	}
	// --------------------------------------------------

	
	//Métodos Principais
	// --------------------------------------------------
	//Conectar a um link e retorna conteúdo HTML
	private static Document getHTML(String link) {
		Document documento;
		documento = null;
		try {// Conectando e obtendo uma cópia do html inteiro da página do filme
			documento = Jsoup.connect(link).get();
		} catch (Exception e) {
			System.out.println("Site Indisponível ou falha na requisição!");
		}
		return documento;
	}
	
	//Extrai uma lista de 10 piores filmes
	private static List<Filme> extrair10PioresFilmes(String link, String link2){
		System.out.println("\nRealizando requisição...");
		Document documento = getHTML(link);
		List<Filme> filmes = new ArrayList<>();

		Elements pg = documento.getElementsByClass("chart full-width");

		// Obtendo elemento através da tag
		Element tbody = pg.get(0).getElementsByTag("tbody").first();

		// Criando uma lista de todos os tr's do tbody obtido
		int i;
		List<Element> TRs, Bckp, TDs;
		TRs = tbody.getElementsByTag("tr");

		// Pondo em ordem decrescente
		Bckp = TRs;
		TRs = new ArrayList<Element>();
		for (i = 9; i >= 0; i--) {
			TRs.add(Bckp.get(i));
		}

		// Criando uma lista de todos os td's de cada tr obtido para obter link dos filmes
		Element tagA;
		List<String> filmeLinkRest = new ArrayList<>();
		List<String> ano = new ArrayList<>();
		List<String> imdb1 = new ArrayList<>();

		for (Element elemento : TRs) {
			TDs = elemento.getElementsByTag("td");
			tagA = TDs.get(1).getElementsByTag("a").first();// 2º TD
			filmeLinkRest.add(tagA.attr("href"));// pegando o restante do link de cada filme

			ano.add(TDs.get(1).getElementsByClass("secondaryInfo").first().text());// pegando o ano
			imdb1.add(TDs.get(2).text());
		}

		// salvando informações obtidas na lista de filmes;
		System.out.println("Salvando primeiras informações...");
		for (i = 0; i < 10; i++) {
			Filme x;
			x = new Filme();
			x.id = i+1;
			x.ano = ano.get(i);
			x.imdbRating1 = imdb1.get(i);
			x.link = link2 + filmeLinkRest.get(i);
			
			//retirando "(" e ")" do ano do filme
			x.ano = x.ano.replaceAll("\\(", "");
			x.ano = x.ano.replaceAll("\\)", "");
			
			filmes.add(x);
		}
		System.out.println("Êxito ao salvar primeiras informações!");
		return filmes;
	}
	
	//Extrair Informações sobre cada filme
	private static Filme extrairInformacoesDoFilme(Document documento, Filme filme, String link2, int contador) {
		//Extraindo Titulos
		// ------------
		String tituloPortugues = extrairTituloPortugues(documento);
		String tituloIngles = extrairTituloIngles(documento, tituloPortugues, filme);
		// ------------
		
		//Extraindo nota IMDB do site do filme
		// ------------
		String imdb2 = extrairIMDB2(documento);
		// ------------
		
		//Extraindo elenco principal
		// ------------
		List<String> elencoPrincipal = extrairElencoPrincipal(documento);
		// ------------

		//Extraindo diretores
		// ------------
		List<String> diretores = extrairDiretores(documento, link2);
		// ------------

		//Extraindo link do comentário
		// ------------
		String comentarioLink = extrairComentarioLink(documento, link2);
		// ------------

		//Extraindo texto do comentário
		String comentarioTexto = extrairComentarioTexto(comentarioLink); 

		//Atualizando informações obtidas
		filme = atualizarInformacoes(filme, tituloPortugues, tituloIngles, imdb2, elencoPrincipal, diretores, comentarioLink, comentarioTexto);
		
		//Imprimindo carregamento
		imprimirCarregamento(contador);
		
		return filme;
	}

	// --------------------------------------------------
	
	//Métodos Secundários
	// ----------------------------------------------------------
	// Titulo em portugues
	private static String extrairTituloPortugues(Document documento) {
		String tituloPortugues;
		Element divX;
		List<String> titlePortugues = new ArrayList<String>();
		divX = documento.getElementsByClass("TitleBlock__TitleContainer-sc-1nlhx7j-1 jxsVNt").first();
		titlePortugues.add(divX.getElementsByTag("h1").first().text());
		tituloPortugues = titlePortugues.get(0);
		
		return tituloPortugues;
	}
	
	//Titulo em ingles
	private static String extrairTituloIngles(Document documento, String tituloPortugues, Filme filme) {
		String tituloIngles, corretor1, corretor2;
		Element div, ulS;
		Elements liS;
		div = documento.getElementsByClass("TitleBlock__TitleMetaDataContainer-sc-1nlhx7j-2 hWHMKr").first();
		tituloIngles = div.text();
		
		//corrigindo o titulo
		ulS = documento.getElementsByClass("ipc-inline-list ipc-inline-list--show-dividers TitleBlockMetaData__MetaDataList-sc-12ein40-0 dxizHm baseAlt").first();
		liS = ulS.getElementsByTag("li");
		corretor1 = liS.get(1).text();
		corretor2 = liS.get(2).text();		
		tituloIngles = corrigirTituloIngles(tituloIngles, tituloPortugues,filme.ano, corretor1, corretor2);
		
		return tituloIngles;
	}
	
	// IMDB 2
	private static String extrairIMDB2(Document documento) {
		String imdb2;
		Element div2;
		div2 = documento.getElementsByClass("AggregateRatingButton__RatingScore-sc-1ll29m0-1 iTLWoV").first();
		imdb2 = div2.text();
		return imdb2;
	}
	
	// Elenco principal
	private static List<String> extrairElencoPrincipal(Document documento) {
		List<Element> elencoPrincip;
		List<String> elencoPrincipal = new ArrayList<String>();
		elencoPrincip = documento.getElementsByClass("StyledComponents__ActorName-sc-y9ygcu-1 ezTgkS");
		for (Element elemento : elencoPrincip) {
			elencoPrincipal.add(elemento.text());
		}
		return elencoPrincipal;
	}
	
	// Diretores
	private static List<String> extrairDiretores(Document documento, String link2){
		// Obtendo complemento do Link dos diretores
		Element ul;
		List<Element> li;
		List<String> diretores = new ArrayList<String>();
		List<String> diretoresLinkRest = new ArrayList<String>();
		ul = documento.getElementsByClass("ipc-inline-list ipc-inline-list--show-dividers ipc-inline-list--inline ipc-metadata-list-item__list-content baseAlt").first();
		li = ul.getElementsByTag("li");
		for (Element elemento : li) {
			diretoresLinkRest.add((elemento.getElementsByTag("a").first()).attr("href"));
		}

		// Acessando a pagina de cada diretor (Terceiro Passo)
		for (int i = 0; i < diretoresLinkRest.size(); i++) {
			String diretor;
			diretor = acessarDiretorPG(link2 + diretoresLinkRest.get(i));
			diretores.add(diretor);
		}
		return diretores;
	}
	
	// Link do comentário
	private static String extrairComentarioLink(Document documento, String link2) {
		String comentarioLink;
		// Link de avaliacao
		Element avaliacaoLink;
		List<String> avaliacaoLinkRest = new ArrayList<String>();
		avaliacaoLink = documento.getElementsByClass("ipc-link ipc-link--baseAlt ipc-link--touch-target ReviewContent__StyledTextLink-sc-vlmc3o-2 dTjvFT isReview").first();
		avaliacaoLinkRest.add(avaliacaoLink.attr("href"));

		// Acessando a pagina da avaliacao em ordem decrescente
		comentarioLink = link2 + acessarAvaliacaoPG(link2 + formatarLinkAvaliacao(avaliacaoLinkRest.get(0)));
		
		return comentarioLink;
	}
	
	private static String extrairComentarioTexto(String link) {
		String comentario = "";
		Element div;
		Document doc;
		doc = null;
		try {
			doc = Jsoup.connect(link).get();
		} catch (Exception e) {
			System.out.println("Site Indisponível ou falha na requisição!");
		}
		div = doc.getElementsByClass("text show-more__control").first();
		comentario = div.text();
		return comentario;
	}
	
	// Atualizar informações
	private static Filme atualizarInformacoes(Filme filme, String tituloPortugues, String tituloIngles, String imdb2, List<String> elencoPrincipal, List<String> diretores, String comentarioLink, String comentarioTexto) {
		filme.tituloPortugues = tituloPortugues;
		filme.tituloIngles = tituloIngles;
		filme.imdbRating2 = imdb2;
		filme.elencoPrincipal = elencoPrincipal;
		filme.diretores = diretores;
		filme.comentarioLink = comentarioLink;
		filme.comentarioTexto = comentarioTexto;
		return filme;
	}
	// ----------------------------------------------------------

	// Impressão de carregamento
	private static void imprimirCarregamento(int contador) {
		if (contador < 10) {
			System.out.print(":");
		} else {
			System.out.println(":]");
			System.out.println("\t[:::100%:::]\n");
		}
	}
	
	//Métodos de formatação ou correção
	// ----------------------------------------------------------
	public static String corrigirTituloIngles(String tituloIngles, String tituloPortugues, String filmeAno,String corretor1, String corretor2) {
		String corretor;
		String tituloInglesCorrigido = tituloIngles;
		corretor = "Original title: ";
		tituloInglesCorrigido = tituloInglesCorrigido.replaceAll(" " + filmeAno + filmeAno + " ", "");
		tituloInglesCorrigido = tituloInglesCorrigido.replaceAll(filmeAno + filmeAno + " ", "");
		tituloInglesCorrigido = tituloInglesCorrigido.replaceAll(corretor, "");
		tituloInglesCorrigido = tituloInglesCorrigido.replaceAll(corretor1 + " ", "");
		tituloInglesCorrigido = tituloInglesCorrigido.replaceAll(corretor1, "");
		tituloInglesCorrigido = tituloInglesCorrigido.replaceAll(corretor2, "");

		if (tituloInglesCorrigido == "") {
			tituloInglesCorrigido = tituloPortugues;
		}
		return tituloInglesCorrigido;
	}
	
	private static String formatarLinkAvaliacao(String link) {
		String remover = ("ref_=tt_ov_rt");
		String adicionar = ("sort=userRating&dir=desc&ratingFilter=0");

		link = link.replace(remover, adicionar);

		return link;
	}
	// ----------------------------------------------------------
	
	// Métodos de conexão que retornam informação
	// ----------------------------------------------------------
	private static String acessarAvaliacaoPG(String link) {
		String avaliacao = "";
		Document doc;
		Element div, a;
		doc = null;
		try {
			doc = Jsoup.connect(link).get();
		} catch (Exception e) {
			System.out.println("Site Indisponível ou falha na requisição!");
		}
		div = doc.getElementsByClass("lister-item-content").first();
		a = div.getElementsByTag("a").first();
		avaliacao = a.attr("href");
		return avaliacao;
	}

	private static String acessarDiretorPG(String link) {
		String diretor = "";
		Document doc;
		doc = null;
		try {
			doc = Jsoup.connect(link).get();
		} catch (Exception e) {
			System.out.println("Site Indisponível ou falha na requisição!");
		}
		diretor = doc.getElementsByClass("itemprop").first().text();
		return diretor;
	}

	// ----------------------------------------------------------
}
