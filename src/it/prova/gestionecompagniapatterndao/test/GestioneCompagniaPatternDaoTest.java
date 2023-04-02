package it.prova.gestionecompagniapatterndao.test;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.prova.gestionecompagniapatterndao.connection.MyConnection;
import it.prova.gestionecompagniapatterndao.dao.Constants;
import it.prova.gestionecompagniapatterndao.dao.compagnia.CompagniaDAO;
import it.prova.gestionecompagniapatterndao.dao.compagnia.CompagniaDAOImpl;
import it.prova.gestionecompagniapatterndao.dao.impiegato.ImpiegatoDAO;
import it.prova.gestionecompagniapatterndao.dao.impiegato.ImpiegatoDAOImpl;
import it.prova.gestionecompagniapatterndao.model.Compagnia;
import it.prova.gestionecompagniapatterndao.model.Impiegato;

public class GestioneCompagniaPatternDaoTest {

	public static void main(String[] args) {

		ImpiegatoDAO impiegatoDAOInstance = null;
		CompagniaDAO compagniaDAOInstance = null;

		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			// ecco chi 'inietta' la connection: il chiamante

			impiegatoDAOInstance = new ImpiegatoDAOImpl(connection);
			compagniaDAOInstance = new CompagniaDAOImpl(connection);

//******************************************************************************
//						        test compagnia

			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			testInserimentoCompagnia(compagniaDAOInstance);

			System.out.println("In tabella user ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");

			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");

			testGetCompagnia(compagniaDAOInstance);

			testUpdateCompagnia(compagniaDAOInstance);

			testDeleteCompagnia(compagniaDAOInstance);

			testFindAllByDataAssunzioneMaggioreDi(compagniaDAOInstance);

			testFindAllByRagioneSocialeContiene(compagniaDAOInstance);

			testFindAllByCodFisImpiegatoContiene(compagniaDAOInstance);

//******************************************************************************
//							test impiegato

			testInserimentoImpiegato(impiegatoDAOInstance);

			testGetImpiegato(impiegatoDAOInstance);

			testUpdateImpiegato(impiegatoDAOInstance);

			testDeleteImpiegato(impiegatoDAOInstance);

			testCountByDataFondazioneCompagniaGreaterThan(impiegatoDAOInstance);

			testFindAllByCompagniaConFatturatoMaggioreDi(impiegatoDAOInstance);

			testFindAllErroriAssunzione(impiegatoDAOInstance);

			testFindAllByCompagnia(impiegatoDAOInstance);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	
//******************************************************************************
//							inizio test compagnia
//******************************************************************************

	private static void testInserimentoCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testInserimentoCompagnia inizio.............");
		int quantiCompagnieInserite = compagniaDAOInstance
				.insert(new Compagnia("Acme Technologies Srl", 1000000, LocalDate.parse("2005-01-1")));
		if (quantiCompagnieInserite < 1)
			throw new RuntimeException("testInserimentoCompagnia : FAILED");

		System.out.println(".......testInserimentoCompagnia fine: PASSED.............");
	}

	private static void testGetCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testGetCompagnia inizio.............");
		List<Compagnia> presentiSulDb = compagniaDAOInstance.list();
		List<Compagnia> ottenutiDalMetodo = new ArrayList<>(
				Arrays.asList(compagniaDAOInstance.get(presentiSulDb.get(0).getId())));
		if (ottenutiDalMetodo.size() < 1)
			throw new RuntimeException("testGetCompagnia : FAILED");

		System.out.println(".......testGetCompagnia fine: PASSED.............");
	}

	private static void testUpdateCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testUpdateCompagnia inizio.............");
		int quantiCompagnieAggiornate = compagniaDAOInstance
				.update(new Compagnia(1L, "Green Life Spa", 5000000, LocalDate.parse("2012-07-15")));
		if (quantiCompagnieAggiornate < 1)
			throw new RuntimeException("testUpdateCompagnia : FAILED");

		System.out.println(".......testUpdateCompagnia fine: PASSED.............");
	}

	private static void testDeleteCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testDeleteCompagnia inizio.............");
		int quantiCompagnieInserite = compagniaDAOInstance
				.insert(new Compagnia("Blue Ocean Srl", 20000000, LocalDate.parse("1998-05-3")));
		if (quantiCompagnieInserite < 1)
			throw new RuntimeException("testDeleteCompagnia : FAILED, inserimento fallito");
		List<Compagnia> presentiSulDbDopoInsert = compagniaDAOInstance.list();
		compagniaDAOInstance.delete(presentiSulDbDopoInsert.get(presentiSulDbDopoInsert.size() - 1));
		List<Compagnia> presentiSulDbDopoDelete = compagniaDAOInstance.list();
		if (presentiSulDbDopoDelete.size() == presentiSulDbDopoInsert.size()) {
			throw new RuntimeException("testDeleteCompagnia : FAILED, delete fallita");
		}
		System.out.println(".......testUpdateCompagnia fine: PASSED.............");
	}

	private static void testFindAllByDataAssunzioneMaggioreDi(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testUpdateCompagnia inizio.............");
		List<Compagnia> compagnieConDataMaggDi = compagniaDAOInstance
				.findAllByDataAssunzioneMaggioreDi(LocalDate.parse("2018-03-01")); // un ritorno aspettato da lettura
		if (compagnieConDataMaggDi.size() < 1) {
			throw new RuntimeException("testFindAllByDataAssunzioneMaggioreDi : FAILED, nessun ritorno 1 aspettato");

		}

		System.out.println(".......testUpdateCompagnia fine: PASSED.............");
	}

	private static void testFindAllByRagioneSocialeContiene(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testUpdateCompagnia inizio.............");
		int quantiCompagnieInserite = compagniaDAOInstance
				.insert(new Compagnia("Red Star Ltd", 15000000, LocalDate.parse("1985-09-9")));
		if (quantiCompagnieInserite < 1)
			throw new RuntimeException("testFindAllByDataAssunzioneMaggioreDi : FAILED, inserimeto fallito");
		List<Compagnia> compagnieConDataMaggDi = compagniaDAOInstance.findAllByRagioneSocialeContiene("Re");
		if (compagnieConDataMaggDi.size() < 1) {
			throw new RuntimeException("testFindAllByDataAssunzioneMaggioreDi : FAILED, nessun ritorno 1 aspettato");

		}

		System.out.println(".......testUpdateCompagnia fine: PASSED.............");
	}

	private static void testFindAllByCodFisImpiegatoContiene(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testUpdateCompagnia inizio.............");
		List<Compagnia> compagnieConDataMaggDi = compagniaDAOInstance.findAllBYCodFisImpiegatoContiene("501");
		if (compagnieConDataMaggDi.size() < 1) {
			throw new RuntimeException("testFindAllByDataAssunzioneMaggioreDi : FAILED, nessun ritorno 1 aspettato");

		}

		System.out.println(".......testUpdateCompagnia fine: PASSED.............");
	}

	
//******************************************************************************
//						   inizio test impiegato

	private static void testInserimentoImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testInserimentoNegozio inizio.............");
		int quantiImpiegatiInseriti = impiegatoDAOInstance.insert(new Impiegato("Mario", "Rossi", "RSSMRA95D31F205L",
				LocalDate.parse("12/03/1995"), LocalDate.parse("01/02/2020"),
				new Compagnia(3L, "Golden Harvest Inc", 30000000, LocalDate.parse("1990-12-22"))));
		if (quantiImpiegatiInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		System.out.println(".......testInserimentoNegozio fine: PASSED.............");
	}

	private static void testGetImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testGetImpiegato inizio.............");
		List<Impiegato> presentiSulDb = impiegatoDAOInstance.list();
		List<Impiegato> ottenutiDalMetodo = new ArrayList<>(
				Arrays.asList(impiegatoDAOInstance.get(presentiSulDb.get(0).getId())));
		if (ottenutiDalMetodo.size() < 1)
			throw new RuntimeException("testGetImpiegato : FAILED");

		System.out.println(".......testGetImpiegato fine: PASSED.............");
	}

	private static void testUpdateImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testUpdateCompagnia inizio.............");
		int quantiCompagnieAggiornate = impiegatoDAOInstance.update(new Impiegato(1L, "Sofia", "Bianchi",
				"BNCSFA00A41L219M", LocalDate.parse("2000-05-01"), LocalDate.parse("2021-06-15")));
		if (quantiCompagnieAggiornate < 1)
			throw new RuntimeException("testUpdateCompagnia : FAILED");

		System.out.println(".......testUpdateCompagnia fine: PASSED.............");
	}

	private static void testDeleteImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testDeleteCompagnia inizio.............");
		int quantiCompagnieInserite = impiegatoDAOInstance.insert(new Impiegato("Carlo", "Neri", "NERCRF91E28I712H",
				LocalDate.parse("1991-05-28"), LocalDate.parse("2017-04-03"),
				new Compagnia(3L, "Golden Harvest Inc", 30000000, LocalDate.parse("1990-12-22"))));
		if (quantiCompagnieInserite < 1)
			throw new RuntimeException("testDeleteCompagnia : FAILED, inserimento fallito");
		List<Impiegato> presentiSulDbDopoInsert = impiegatoDAOInstance.list();
		impiegatoDAOInstance.delete(presentiSulDbDopoInsert.get(presentiSulDbDopoInsert.size() - 1));
		List<Impiegato> presentiSulDbDopoDelete = impiegatoDAOInstance.list();
		if (presentiSulDbDopoDelete.size() == presentiSulDbDopoInsert.size()) {
			throw new RuntimeException("testDeleteCompagnia : FAILED, delete fallita");
		}
		System.out.println(".......testUpdateCompagnia fine: PASSED.............");
	}

	private static void testFindAllByCompagnia(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testFindAllByCompagniaConFatturatoMaggioreDi inizio.............");
		Impiegato impProva = impiegatoDAOInstance.getEager(1L);
		List<Impiegato> compagnieConDataMaggDi = impiegatoDAOInstance.findAllByCompagnia(impProva.getCompagnia());
		if (compagnieConDataMaggDi.size() < 1) {
			throw new RuntimeException("testFindAllByDataAssunzioneMaggioreDi : FAILED, nessun ritorno 1 aspettato");

		}

		System.out.println(".......testFindAllByCompagniaConFatturatoMaggioreDi fine: PASSED.............");
	}

	private static void testCountByDataFondazioneCompagniaGreaterThan(ImpiegatoDAO impiegatoDAOInstance)
			throws Exception {
		System.out.println(".......testCountByDataFondazioneCompagniaGreaterThan inizio.............");

		if (impiegatoDAOInstance.countByDataFondazioneCompagniaGreaterThan(LocalDate.parse("1999-03-01")) < 2) {

			throw new RuntimeException("testCountByDataFondazioneCompagniaGreaterThan : FAILED");
		}

		System.out.println(".......testCountByDataFondazioneCompagniaGreaterThan fine: PASSED.............");

	}

	private static void testFindAllByCompagniaConFatturatoMaggioreDi(ImpiegatoDAO impiegatoDAOInstance)
			throws Exception {
		System.out.println(".......testFindAllByCompagniaConFatturatoMaggioreDi inizio.............");
		List<Impiegato> compagnieConDataMaggDi = impiegatoDAOInstance.findAllByCompagniaConFatturatoMaggioreDi(1000);
		if (compagnieConDataMaggDi.size() < 1) {
			throw new RuntimeException("testFindAllByDataAssunzioneMaggioreDi : FAILED, nessun ritorno 1 aspettato");

		}

		System.out.println(".......testFindAllByCompagniaConFatturatoMaggioreDi fine: PASSED.............");
	}

	private static void testFindAllErroriAssunzione(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testFindAllErroriAssunzione inizio.............");
		List<Impiegato> compagnieConDataMaggDi = impiegatoDAOInstance.findAllErroriAssunzione();
		if (compagnieConDataMaggDi.size() < 2) {
			throw new RuntimeException("testFindAllErroriAssunzione : FAILED, nessun ritorno 2 aspettato");

		}

		System.out.println(".......testFindAllErroriAssunzione fine: PASSED.............");
	}

}