package model;

import java.math.BigDecimal;

public class Produto {
	private String descricao;
	private String dadosValores;
	private BigDecimal valorAVista;
	private BigDecimal valorAPrazo;
	private int parcelas;
	
	public Produto(String descricao, String dadosValores) {
		super();
		this.descricao = descricao;
		this.dadosValores = dadosValores;
		this.parcelas = getParcelas(dadosValores);
		this.valorAVista = getValorAVista(dadosValores);
		this.valorAPrazo = getValorAPrazo(dadosValores);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDadosValores() {
		return dadosValores;
	}

	public void setDadosValores(String dadosValores) {
		this.dadosValores = dadosValores;
	}

	public BigDecimal getValorAVista() {
		return valorAVista;		
	}
	
	public BigDecimal getValorAVista(String dadosValores) {
		
		return new BigDecimal(dadosValores.substring(dadosValores.indexOf('$'), dadosValores.indexOf('\n')).replace("$", "")
			.replaceAll("\\.", "")+"."+
				dadosValores.substring(dadosValores.indexOf('\n')+1, dadosValores.indexOf('\n')+3).trim());
	}

	public void setValorAVista(BigDecimal valorAVista) {
		this.valorAVista = valorAVista;
	}

	public BigDecimal getValorAPrazo() {
		return valorAPrazo;
	}
	
	public BigDecimal getValorAPrazo(String dadosValores) {
		
		if(this.parcelas==0)
			return BigDecimal.ZERO;
		
		return new BigDecimal(dadosValores.substring(dadosValores.lastIndexOf('$')+2, dadosValores.lastIndexOf(',')+3)
				.replaceAll("\\.", "").replaceAll(",", "\\.")).multiply(new BigDecimal(this.parcelas));
	}

	public void setValorAPrazo(BigDecimal valorAPrazo) {
		this.valorAPrazo = valorAPrazo;
	}

	public int getParcelas() {
		return parcelas;
	}
	
	public int getParcelas(String dadosValores) {
		if(dadosValores.indexOf("até ")>0)
			return Integer.parseInt(dadosValores.substring(dadosValores.indexOf("até ") + 4, dadosValores.indexOf("x de")));
		
		return 0;
	}

	public void setParcelas(int parcelas) {
		this.parcelas = parcelas;
	}

	@Override
	public String toString() {
		return "Produto [descricao=" + descricao + ", dadosValores=" + dadosValores + ", valorAVista=" + valorAVista
				+ ", valorAPrazo=" + valorAPrazo + ", parcelas=" + parcelas + "]";
	}
	
	

}
