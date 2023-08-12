package co.com.boxercrossgym.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class PageRender<T> {
	
	
	private String url;
	private Page<T> page;
	private int totalPaginas;
	private int numeroElementosPorPagina;

	private int paginaActual;
	
	private List<PageItem> paginas;
	
	
	
	public PageRender(String url, Page<T> page){
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();
		
		numeroElementosPorPagina = page.getSize();
		totalPaginas = page.getTotalPages();
		paginaActual = page.getNumber() +1;
		
		System.out.println("URL: " + url + "\n , page = " + page + " , paginas: " + paginas.toString() + " , numeroElePorPag " + numeroElementosPorPagina
				+ " , totalPaginas: " + totalPaginas + " , paginaActual: " + paginaActual +  " , numeroElePorPag/2 " + numeroElementosPorPagina/2);
		
		int desde, hasta;
		
		if(totalPaginas <= numeroElementosPorPagina) {
			desde = 1;
			hasta = totalPaginas;
		}else if( paginaActual >= totalPaginas - numeroElementosPorPagina/2) {
			desde = totalPaginas - numeroElementosPorPagina/2 -1;
			hasta = numeroElementosPorPagina;
		}else {
			desde = paginaActual - numeroElementosPorPagina/2;
			hasta = getNumeroElementosPorPagina();
		}
		
		for(int i = 0; i < hasta ; i++) {
			if(desde + i <= totalPaginas) {
				paginas.add(new PageItem(desde +i, paginaActual == desde +i));
			}
			
		}
		
	}
	
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
}
