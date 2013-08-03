package br.ufrj.ad.simulador.testes;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.ufrj.ad.simulator.eventos.EventoRoteadorRecebePacoteTxTCP;
import br.ufrj.ad.simulator.eventos.EventoRoteadorTerminaEnvio;
import br.ufrj.ad.simulator.eventos.EventoTimeOut;
import br.ufrj.ad.simulator.eventos.EventoTxRecebeSACK;
import br.ufrj.ad.simulator.models.Pacote;
import br.ufrj.ad.simulator.models.Parametros;
import br.ufrj.ad.simulator.models.Simulador;
import br.ufrj.ad.simulator.models.TxTCP;

/**
 * Casos de teste para classe Simulador.
 * 
 * @author André Ramos, Wellignton Mascena, Felipe Teixeira
 * 
 */
public class TesteSimulador {

	private Simulador simulador;
	private Parametros parametros;

	@Before
	public void setUp() throws Exception {
		parametros = new Parametros();

		parametros.setProperty("EstacoesGrupo1", "1");
		parametros.setProperty("EstacoesGrupo2", "0");
		parametros.setProperty("Cs", "1E9");
		parametros.setProperty("Cg", "10E6");
		parametros.setProperty("TP1", "100");
		parametros.setProperty("TP2", "50");
		parametros.setProperty("TPACK1", "100");
		parametros.setProperty("TPACK2", "50");
		parametros.setProperty("DisciplinaRoteador", "FIFO");
		parametros.setProperty("TamanhoBufferRoteador", "40");
		parametros.setProperty("TempoMedioEntreRajadas", "24");
		parametros.setProperty("MediaPacotePorRajada", "10");

		simulador = new Simulador(parametros);
	}

	@Test
	public void testRoteadorRecebePacoteTxTCP1() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		assertEquals(EventoRoteadorTerminaEnvio.class, simulador
				.getFilaEventos().peek().getClass());
	}

	@Test
	public void testRoteadorRecebePacoteTxTCP2() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		assertEquals(1.2 + 100, simulador.getFilaEventos().peek()
				.getTempoDeOcorrencia(), 0);
	}

	@Test
	public void testRoteadorRecebePacoteTxTCP3() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();

		assertEquals(EventoRoteadorRecebePacoteTxTCP.class, simulador
				.getFilaEventos().peek().getClass());
	}

	@Test
	public void testRoteadorRecebePacoteTxTCP4() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();

		assertEquals(100 + 100 + 0.012, simulador.getFilaEventos().peek()
				.getTempoDeOcorrencia(), 0);
	}

	@Test
	public void testRoteadorRecebePacoteTxTCP5() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();
		simulador.getFilaEventos().poll();

		assertEquals(1, simulador.getFilaEventos().size());
	}

	@Test
	public void testRoteadorRecebePacoteTxTCP6() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();
		simulador.getFilaEventos().poll();

		assertEquals(EventoTimeOut.class, simulador.getFilaEventos().poll()
				.getClass());
	}

	@Test
	public void testRoteadorRecebePacoteTxTCP7() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();
		simulador.getFilaEventos().poll();

		assertEquals(simulador.getTransmissores()[0].getRTO() + 100, simulador
				.getFilaEventos().poll().getTempoDeOcorrencia(), 0);
	}

	/**
	 * Verifica se o ao iniciar uma nova transmissão de pacote TxTCP o evento de
	 * time-out corresponte está sendo criado corretamente.
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP8() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // RoteadorTerminaEnvio
		Pacote p1 = ((EventoRoteadorRecebePacoteTxTCP) simulador
				.getFilaEventos().poll()).getPacote(); // RoteadorRecebePacoteTxTCP

		assertEquals(p1.getEventoTimeOut(), simulador.getFilaEventos().poll());
	}

	/**
	 * Verifica se irá criar um evento de RoteadorRecebePacoteTxTCP ao resgatar
	 * o primeiro evento da fila de eventos
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP9() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getRoteador().receberPacote(new Pacote());

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		assertEquals(EventoRoteadorRecebePacoteTxTCP.class, simulador
				.getFilaEventos().poll().getClass()); // RoteadorRecebePacoteTxTCP

	}

	/**
	 * Verifica se irá resgatar um evento TimeOut ao resgatar o segundo evento
	 * da fila de eventos
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP10() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getRoteador().receberPacote(new Pacote());

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // RoteadorRecebePacoteTxTCP

		assertEquals(EventoTimeOut.class, simulador.getFilaEventos().poll()
				.getClass()); // Time-Out
	}

	/**
	 * Verifica se o evento RoteadorRecebePacoteTxTCP recebe o evento no tempo
	 * esperado (200,012)
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP11() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getRoteador().receberPacote(new Pacote());

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		assertEquals(200.012, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0); // RoteadorRecebePacoteTxTCP

	}

	/**
	 * Verifica se irá resgatar um evento TimeOut ao resgatar o segundo evento
	 * da fila de eventos no tempo correto
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP12() {

		Pacote p0 = new Pacote();
		p0.setByteInicialEFinal(0, 1499);
		p0.setDestino(0);

		simulador.getRoteador().receberPacote(new Pacote());

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // RoteadorRecebePacoteTxTCP

		assertEquals(100 + simulador.getTransmissores()[0].getRTO(), simulador
				.getFilaEventos().poll().getTempoDeOcorrencia(), 0); // Time-Out
	}

	/**
	 * Verifica se não haverá eventos na fila de eventos, que é o esperado nesse
	 * caso, onde não o TxTCP já fica com a cwnd congestionada, e portanto não
	 * criará evento de Time-out e como o roteador está cheio, não haverá
	 * eventos na lista de eventos
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP13() {

		simulador = new Simulador(parametros);

		Pacote p0 = simulador.getTransmissores()[0].enviarPacote();

		simulador.getRoteador().receberPacote(new Pacote());

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100.012, p0));

		simulador.tratarProximoEvento();

		assertEquals(null, simulador.getFilaEventos().poll()); // null

	}

	/**
	 * Verifica se irá criar o evento RoteadorTerminaEnvio no caso onde o buffer
	 * do roteador estará vazio
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP14() {

		Pacote p0 = simulador.getTransmissores()[0].enviarPacote();

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100.012, p0));

		simulador.tratarProximoEvento();

		assertEquals(EventoRoteadorTerminaEnvio.class, simulador
				.getFilaEventos().poll().getClass()); // RoteadorTerminaEnvio

	}

	/**
	 * Verifica se irá criar o evento RoteadorTerminaEnvio no caso onde o buffer
	 * do roteador estará vazio, e verificar se o tempo é condizente com a
	 * expectativa
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP15() {

		Pacote p0 = simulador.getTransmissores()[0].enviarPacote();

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100.012, p0));

		simulador.tratarProximoEvento();

		assertEquals(101.212, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0); // RoteadorTerminaEnvio

	}

	/**
	 * Verifica se o evento após o RoteadorTerminaEnvio é nulo
	 */
	@Test
	public void testRoteadorRecebePacoteTxTCP16() {

		Pacote p0 = simulador.getTransmissores()[0].enviarPacote();

		simulador.getFilaEventos().add(
				new EventoRoteadorRecebePacoteTxTCP(100.012, p0));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll();

		assertEquals(null, simulador.getFilaEventos().poll()); // null

	}

	/* --------------Testes do EventoRoteadorTerminaEnvio-------------- */

	/**
	 * Tratar fim de envio no roteador quando não há pacotes. Indica que criamos
	 * eventos de envio desnecessários.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testRoteadorTerminaEnvio1() {

		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(1.2));

		simulador.tratarProximoEvento();

	}

	/**
	 * Garante que não irá criar um evento de terminar envio quando não houver
	 * mais pacotes no buffer para serem enviados.
	 * 
	 * OBS: Nesse caso, pacotes de tráfego de fundo não criam o evento de SACK.
	 */
	@Test
	public void testRoteadorTerminaEnvio2() {

		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(1.2));

		simulador.tratarProximoEvento();

		assertEquals(null, simulador.getFilaEventos().poll());

	}

	/**
	 * Testa tratamento de evento de envio no roteador quando há dois pacotes de
	 * tráfego de fundo no buffer.
	 * 
	 * OBS: Nesse caso, pacotes de tráfego de fundo não criam o evento de SACK.
	 */
	@Test
	public void testRoteadorTerminaEnvio3() {

		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(1.2));

		simulador.tratarProximoEvento();

		assertEquals(EventoRoteadorTerminaEnvio.class, simulador
				.getFilaEventos().poll().getClass());

	}

	/**
	 * Confere o tempo de ocorrência do envio do segundo pacote de tráfego de
	 * fundo no buffer do roteador.
	 * 
	 * OBS: Nesse caso, pacotes de tráfego de fundo não criam o evento de SACK.
	 */
	@Test
	public void testRoteadorTerminaEnvio4() {

		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(1.2));

		simulador.tratarProximoEvento();

		assertEquals(1.2 + 1.2, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);

	}

	/**
	 * Agora testando com pacotes TCP no buffer do roteador.
	 */
	@Test
	public void testRoteadorTerminaEnvio5() {

		TxTCP tx = simulador.getTransmissores()[0];

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		assertEquals(EventoTxRecebeSACK.class, simulador.getFilaEventos()
				.poll().getClass());

	}

	@Test
	public void testRoteadorTerminaEnvio6() {

		TxTCP tx = simulador.getTransmissores()[0];

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		assertEquals(100 + 100, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);

	}

	@Test
	public void testRoteadorTerminaEnvio7() {

		TxTCP tx = simulador.getTransmissores()[0];

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();
		simulador.getFilaEventos().poll(); // EventoTxTCPRecebeSACK

		assertEquals(0, simulador.getFilaEventos().size());

	}

	@Test
	public void testRoteadorTerminaEnvio8() {

		TxTCP tx = simulador.getTransmissores()[0];

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		assertEquals(EventoRoteadorTerminaEnvio.class, simulador
				.getFilaEventos().poll().getClass());

	}

	@Test
	public void testRoteadorTerminaEnvio9() {

		TxTCP tx = simulador.getTransmissores()[0];

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		assertEquals(100 + 1.2, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);

	}

	@Test
	public void testRoteadorTerminaEnvio10() {

		TxTCP tx = simulador.getTransmissores()[0];

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // RoteadorTerminaEnvio

		assertEquals(EventoTxRecebeSACK.class, simulador.getFilaEventos()
				.poll().getClass());

	}

	@Test
	public void testRoteadorTerminaEnvio11() {

		TxTCP tx = simulador.getTransmissores()[0];

		simulador.getRoteador().receberPacote(tx.enviarPacote());
		simulador.getRoteador().receberPacote(new Pacote());
		simulador.getFilaEventos().add(new EventoRoteadorTerminaEnvio(100));

		simulador.tratarProximoEvento();

		simulador.getFilaEventos().poll(); // RoteadorTerminaEnvio

		assertEquals(100 + 100, simulador.getFilaEventos().poll()
				.getTempoDeOcorrencia(), 0);

	}
}
