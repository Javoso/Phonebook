package com.phonebook.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;

public class DataUtil implements Serializable {

	private static final long serialVersionUID = -5392655117510854021L;

	public String formatar(Date data, String formato) {
		return new SimpleDateFormat(formato).format(data);
	}

	public Integer getAnoAtual() {
		return LocalDate.now().getYear();
	}

	public String getDataEnvioFormatada(Date dataEnvio) {
		if (dataEnvio == null)
			return "Não enviado";

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		return format.format(dataEnvio);
	}

	public Date createDateAtual() {
		TimeZone tz = TimeZone.getTimeZone("America/Fortaleza");
		TimeZone.setDefault(tz);
		Calendar ca = GregorianCalendar.getInstance(tz);
		return ca.getTime();
	}

	public Date createDate(String data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = formato.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	public Date createTempo(String data) {
		SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
		Date date = null;
		try {
			date = formato.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	public String formatarData(Date date) {
		String data = null;
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		data = formatador.format(date);
		return data;
	}

	public String formatarDataSQL(Date date) {
		String data = null;
		SimpleDateFormat formatador = new SimpleDateFormat("yyyy/MM/dd");
		data = formatador.format(date);
		return data;
	}

	public LocalDate asLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public Date asDate(LocalDate date) {
		return Date.from(
				date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public Date somarDiasParaDataAtual(int quantidade) {
		LocalDate now = LocalDate.now().plusDays(quantidade);
		return asDate(now);
	}

	public boolean primeiraDataVemAntes(Date inicio, Date termino) {
		LocalDate ini = asLocalDate(inicio);
		LocalDate fim = asLocalDate(termino);

		return ini.isBefore(fim);
	}

	public long numeroDeDiasEntreDatas(Date inicio, Date fim) {
		return ChronoUnit.DAYS.between(asLocalDate(inicio),
				asLocalDate(fim).plusDays(1));
	}

	public long numeroDeMesesEntreDatas(Date inicio, Date fim) {
		return ChronoUnit.MONTHS.between(asLocalDate(inicio),
				asLocalDate(fim).plusMonths(1));
	}

	public long numeroDeAnosEntreDatas(Date hoje, Date futuro) {
		return ChronoUnit.YEARS.between(asLocalDate(hoje),
				asLocalDate(futuro).plusYears(1));
	}

	public Date getPrimeiroDiaMesAtual() {
		Calendar data = Calendar.getInstance();
		data = DateUtils.truncate(data, Calendar.DAY_OF_MONTH);
		data.set(Calendar.DAY_OF_MONTH, 1);
		return data.getTime();
	}

	public Date getPrimeiroDiaSemestreAtual() {
		Calendar data = Calendar.getInstance();

		if (data.get(Calendar.MONTH) < 6) {
			data.set(Calendar.DAY_OF_MONTH, 1);
			data.set(Calendar.MONTH, 0);
		} else {
			data.set(Calendar.DAY_OF_MONTH, 1);
			data.set(Calendar.MONTH, 6);
		}

		return data.getTime();
	}

	public Date getPrimeiroDiaAnoAtual() {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_MONTH, 1);
		data.set(Calendar.MONTH, 0);

		return data.getTime();
	}

	public List<Date> gerarMeses(Date dataInicial, Date dataFinal) {
		List<Date> datas = new ArrayList<>();

		if (dataInicial == null) 
			dataInicial = new Date();

		dataFinal = dataInicial;

		int iterator = qtdMesesEntreDatas(dataInicial, dataFinal);

		for (int i = 0; i < iterator; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(dataInicial);
			c.add(Calendar.MONTH, i);
			datas.add(c.getTime());
		}

		return datas;
	}

	public List<Date> gerarAnos(Date dataInicial, Date dataFinal) {
		List<Date> datas = new ArrayList<>();

		if (dataInicial == null)
			dataInicial = new Date();

		dataFinal = dataInicial;

		int iterator = qtdAnosEntreDatas(dataInicial, dataFinal);

		for (int i = 0; i < iterator; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(dataInicial);
			c.add(Calendar.YEAR, i);
			datas.add(c.getTime());
		}

		return datas;
	}

	public int qtdMesesEntreDatas(Date dataInicial, Date dataFinal) {
		int qtd = 0;

		if (dataInicial == null)
			dataInicial = new Date();

		dataFinal = dataInicial;

		Calendar c = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c.setTime(dataInicial);
		c2.setTime(dataFinal);

		c.set(Calendar.DATE, 1);
		c2.set(Calendar.DATE, 2);

		while (c2.after(c)) {
			c.add(Calendar.MONTH, 1);
			qtd++;
		}

		return qtd;
	}

	public int qtdAnosEntreDatas(Date dataInicial, Date dataFinal) {

		if (dataInicial == null) 
			dataInicial = new Date();

		dataFinal = dataInicial;

		int qtd = 1;

		Calendar c = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c.setTime(dataInicial);
		c2.setTime(dataFinal);

		c.set(Calendar.DATE, 1);
		c2.set(Calendar.DATE, 2);

		int anoInicial = c.get(Calendar.YEAR);
		int anoFinal = c2.get(Calendar.YEAR);

		while (anoFinal > anoInicial) {
			c.add(Calendar.YEAR, 1);
			qtd++;
			anoInicial = c.get(Calendar.YEAR);
		}

		return qtd;
	}

	public List<Integer> qtdMesesPorAno(Date dataInicial, Date dataFinal) {

		if (dataInicial == null) 
			dataInicial = new Date();

		dataFinal = dataInicial;

		List<Integer> aux = new ArrayList<>();

		Calendar c = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c.setTime(dataInicial);
		c2.setTime(dataFinal);

		c.set(Calendar.DATE, 1);
		c2.set(Calendar.DATE, 2);

		int anoInicial = c.get(Calendar.YEAR);
		int anoFinal = c2.get(Calendar.YEAR);

		while (anoInicial <= anoFinal && c2.after(c)) {

			int anoAux = c.get(Calendar.YEAR);
			int mesAux = 0;

			while (anoAux == c.get(Calendar.YEAR) && c2.after(c)) {

				c.add(Calendar.MONTH, 1);

				mesAux += 1;

			}

			aux.add(mesAux);
			anoInicial = c.get(Calendar.YEAR);
		}

		return aux;
	}

	public static String getPeriodoLetivoAtual() {
		StringBuilder periodoLetivo = new StringBuilder();

		LocalDate date = LocalDate.now();
		periodoLetivo.append(date.getYear()).append("/");
		if (date.getMonthValue() < 6)
			periodoLetivo.append("1");
		else
			periodoLetivo.append("2");
		return periodoLetivo.toString();
	}

	public static String getProximoPeriodoLetivo() {
		String proximo = null;
		LocalDate daquiASeisMeses = LocalDate.now().plusMonths(6);

		if (daquiASeisMeses.getMonthValue() < 7) {
			proximo = daquiASeisMeses.getYear() + "/1";
		} else {
			proximo = daquiASeisMeses.getYear() + "/2";
		}

		return proximo;
	}

	/**
	 * 
	 * @param termino
	 * @return
	 */
	public Date getDataFinalComTempo(Date termino) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		LocalDate localDate = asLocalDate(new Date(termino.getTime()));
		int ano = localDate.getYear();
		int mes = localDate.getMonthValue();
		int dia = localDate.getDayOfMonth();
		try {
			return dateFormat.parse(dia + "/" + mes + "/" + ano + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
			return termino;
		}
	}

}