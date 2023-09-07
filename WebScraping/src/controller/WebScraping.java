package controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import model.Produto;

public class WebScraping {
	public static void main(String[] args) {
		criarPlanilha(rasparDados());
	}

	private static ArrayList<Produto> rasparDados() {
		//definir o caminho do driver
		System.setProperty("webdriver.edge.driver", "resources/msedgedriver.exe");
		
		EdgeOptions options = new EdgeOptions();
		
		//nao exibe o navegador
		options.addArguments("--headless=new");
		
		
		//para corrigir possiveis erros na execução
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		
		//evitar detecao dos sites
		
		options.addArguments("--disable-blink-features=AutomationControlled");
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.setExperimentalOption("useAutomationExtension", null);
		
		options.addArguments("window-size=1600,800");
		
		//para ajudar a não ser identificado como bot
		options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
		
		WebDriver driver = new EdgeDriver(options = options);
		
		driver.get("https://amazon.com.br");
		
		WebElement inputPesquisa = driver.findElement(By.xpath("//input[@id=\"twotabsearchtextbox\"]"));
		
		inputPesquisa.sendKeys("smartphone");
		
		inputPesquisa.submit();
		
		waitForIt(5000);
		
		List<WebElement> descricoesProdutos = driver.findElements(By.xpath("//span[@class=\"a-size-base-plus a-color-base a-text-normal\"]"));
		
		List<WebElement> valoresProdutos = driver.findElements(By.xpath("//div[@class=\"a-row a-size-base a-color-base\"]"));
		
		ArrayList<Produto> produtos = new ArrayList<>();
		for(int i = 0; i < descricoesProdutos.size(); i++) {
			
			produtos.add(new Produto(descricoesProdutos.get(i).getText(), valoresProdutos.get(i).getText()));
		}
		
		waitForIt(5000);
		
		driver.quit();
		
		return produtos;
	}

	//metodo para pausar a execução
	private static void waitForIt(long tempo) {
		
		try {
			new Thread().sleep(tempo);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void criarPlanilha(ArrayList<Produto> produtos){
		//cria um arquivo excel
		Workbook pastaTrabalho = new XSSFWorkbook();
		
		//cria a planilha
		Sheet planilha = pastaTrabalho.createSheet("PRODUTOS");
		
		//cria fonte no estilo negrito
		Font fonteNegrito = pastaTrabalho.createFont();
		fonteNegrito.setBold(true);
		
		//definir celulas negrito
		CellStyle estiloNegrito = pastaTrabalho.createCellStyle();
		estiloNegrito.setFont(fonteNegrito);
		
		//cria as colunas
		Row linha = planilha.createRow(0);
		Cell celula1 = linha.createCell(0);
		celula1.setCellValue("Descrição");
		celula1.setCellStyle(estiloNegrito);
		
		Cell celula2 = linha.createCell(1);
		celula2.setCellValue("Valor a vista");
		celula2.setCellStyle(estiloNegrito);
		
		Cell celula3 = linha.createCell(2);
		celula3.setCellValue("Qtd. parcelas");
		celula3.setCellStyle(estiloNegrito);
		
		Cell celula4 = linha.createCell(3);
		celula4.setCellValue("Valor a prazo");
		celula4.setCellStyle(estiloNegrito);
		
		planilha.autoSizeColumn(0);
		planilha.autoSizeColumn(1);
		planilha.autoSizeColumn(2);
		planilha.autoSizeColumn(3);
		
		
		if(produtos.size() > 0) {
			
			int i = 1;
			for(Produto produto: produtos) {
				Row linhaProduto = planilha.createRow(i);
				Cell celulaDescricao = linhaProduto.createCell(0);
				celulaDescricao.setCellValue(produto.getDescricao());
				
				Cell celulaValorAvista = linhaProduto.createCell(1);
				celulaValorAvista.setCellValue(produto.getValorAVista().toString());
				
				Cell celulaQuantidadeParcelas = linhaProduto.createCell(2);
				celulaQuantidadeParcelas.setCellValue(produto.getParcelas());
				
				Cell celulaValorAPrazo = linhaProduto.createCell(3);
				celulaValorAPrazo.setCellValue(produto.getValorAPrazo().toString());
				i++;
			}
		}
		
		try (FileOutputStream arquivo = new FileOutputStream("produtos.xlsx")){
			pastaTrabalho.write(arquivo);
			
			JOptionPane.showMessageDialog(null, "Planilha criada com sucesso");
		}catch (Exception e) {
			System.out.println("Erro ao criar a planilha: " + e.getMessage());
		}finally {
			try {
				pastaTrabalho.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
