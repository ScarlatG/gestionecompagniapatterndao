package it.prova.gestionecompagniapatterndao.dao.compagnia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.prova.gestionecompagniapatterndao.dao.AbstractMySQLDAO;
import it.prova.gestionecompagniapatterndao.model.Compagnia;
import it.prova.gestionecompagniapatterndao.model.Impiegato;

public class CompagniaDAOImpl extends AbstractMySQLDAO implements CompagniaDAO {

	public CompagniaDAOImpl(Connection connection) {
		super(connection);
	}

	public List<Compagnia> list() throws Exception {// LAZY

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		List<Compagnia> result = new ArrayList<Compagnia>();

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from compagnia")) {

			while (rs.next()) {
				Compagnia compagniaTemp = new Compagnia();
				compagniaTemp.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
				compagniaTemp.setDataFondazione(
						rs.getDate("DATAFONDAZIONE") != null ? rs.getDate("DATAFONDAZIONE").toLocalDate() : null);
				compagniaTemp.setId(rs.getLong("ID"));
				result.add(compagniaTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public List<Compagnia> listEager() throws Exception {// EAGER

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		List<Compagnia> result = new ArrayList<Compagnia>();
		List<Impiegato> impiegatiTemp = new ArrayList<>();

		try (Statement ps = connection.createStatement();
				ResultSet rs = ps
						.executeQuery("select * from compagnia c left outer join impiegato i on c.id=i.id_compagnia")) {

			while (rs.next()) {
				Compagnia compagniaTemp = new Compagnia();
				compagniaTemp.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
				compagniaTemp.setDataFondazione(
						rs.getDate("DATAFONDAZIONE") != null ? rs.getDate("DATAFONDAZIONE").toLocalDate() : null);
				compagniaTemp.setId(rs.getLong("ID"));

				Impiegato impiegatoTemp = new Impiegato();
				impiegatoTemp.setNome(rs.getString("NOME"));
				impiegatoTemp.setCognome(rs.getString("COGNOME"));
				impiegatoTemp.setCodiceFiscale(rs.getString("CODICEFISCALE"));
				impiegatoTemp.setDataNascita(
						rs.getDate("DATANASCITA") != null ? rs.getDate("DATANASCITA").toLocalDate() : null);
				impiegatoTemp.setDataAssunzione(
						rs.getDate("DATAASSUNZIONE") != null ? rs.getDate("DATAASSUNZIONE").toLocalDate() : null);
				impiegatoTemp.setId(rs.getLong("ID"));
				impiegatiTemp.add(impiegatoTemp);
				if (impiegatoTemp != null) {
					compagniaTemp.setImpiegati(impiegatiTemp);
				}

				result.add(compagniaTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public Compagnia get(Long idInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Compagnia result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from compagnia where id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Compagnia();
					result.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
					result.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
					result.setDataFondazione(
							rs.getDate("DATAFONDAZIONE") != null ? rs.getDate("DATAFONDAZIONE").toLocalDate() : null);
					result.setId(rs.getLong("ID"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public int update(Compagnia input) throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE compagnia SET ragionesociale=?, fatturatoannuo=?, datafondazione=? where id=?;")) {
			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			ps.setDate(3, java.sql.Date.valueOf(input.getDataFondazione()));
			ps.setLong(4, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}

	public int insert(Compagnia input) throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO compagnia (ragionesociale, fatturatoannuo, datafondazione) VALUES (?, ?, ?);")) {
			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			ps.setDate(3, java.sql.Date.valueOf(input.getDataFondazione()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public int delete(Compagnia input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM compagnia WHERE ID=?")) {
			ps.setLong(1, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public List<Compagnia> findAllByDataAssunzioneMaggioreDi(LocalDate dateCreatedInput) throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		List<Compagnia> result = new ArrayList<Compagnia>();
		List<Impiegato> impiegatiTemp = new ArrayList<>();

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from compagnia c inner join impiegato i on c.id=i.id_compagnia where dataassunzione > ? ");) {

			ps.setDate(1, java.sql.Date.valueOf(dateCreatedInput));

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Compagnia compagniaTemp = new Compagnia();
					compagniaTemp.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
					compagniaTemp.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
					compagniaTemp.setDataFondazione(
							rs.getDate("DATAFONDAZIONE") != null ? rs.getDate("DATAFONDAZIONE").toLocalDate() : null);
					compagniaTemp.setId(rs.getLong("ID"));

					Impiegato impiegatoTemp = new Impiegato();
					impiegatoTemp.setNome(rs.getString("NOME"));
					impiegatoTemp.setCognome(rs.getString("COGNOME"));
					impiegatoTemp.setCodiceFiscale(rs.getString("CODICEFISCALE"));
					impiegatoTemp.setDataNascita(
							rs.getDate("DATANASCITA") != null ? rs.getDate("DATANASCITA").toLocalDate() : null);
					impiegatoTemp.setDataAssunzione(
							rs.getDate("DATAASSUNZIONE") != null ? rs.getDate("DATAASSUNZIONE").toLocalDate() : null);
					impiegatoTemp.setId(rs.getLong("ID"));
					impiegatiTemp.add(impiegatoTemp);
					compagniaTemp.setImpiegati(impiegatiTemp);

					result.add(compagniaTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public List<Compagnia> findAllByRagioneSocialeContiene(String ragioneSocialeInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		List<Compagnia> result = new ArrayList<Compagnia>();
		List<Impiegato> impiegatiTemp = new ArrayList<>();

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from compagnia c left outer join impiegato i on c.id=i.id_compagnia where ragionesociale like ? ");) {

			ps.setString(1, "%" + ragioneSocialeInput + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Compagnia compagniaTemp = new Compagnia();
					compagniaTemp.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
					compagniaTemp.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
					compagniaTemp.setDataFondazione(
							rs.getDate("DATAFONDAZIONE") != null ? rs.getDate("DATAFONDAZIONE").toLocalDate() : null);
					compagniaTemp.setId(rs.getLong("ID"));

					Impiegato impiegatoTemp = new Impiegato();
					impiegatoTemp.setNome(rs.getString("NOME"));
					impiegatoTemp.setCognome(rs.getString("COGNOME"));
					impiegatoTemp.setCodiceFiscale(rs.getString("CODICEFISCALE"));
					impiegatoTemp.setDataNascita(
							rs.getDate("DATANASCITA") != null ? rs.getDate("DATANASCITA").toLocalDate() : null);
					impiegatoTemp.setDataAssunzione(
							rs.getDate("DATAASSUNZIONE") != null ? rs.getDate("DATAASSUNZIONE").toLocalDate() : null);
					impiegatoTemp.setId(rs.getLong("ID"));
					impiegatiTemp.add(impiegatoTemp);
					compagniaTemp.setImpiegati(impiegatiTemp);

					result.add(compagniaTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public List<Compagnia> findAllBYCodFisImpiegatoContiene(String parteDiCodiceFiscaleInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		List<Compagnia> result = new ArrayList<Compagnia>();
		List<Impiegato> impiegatiTemp = new ArrayList<>();

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from compagnia c inner join impiegato i on c.id=i.id_compagnia where codicefiscale like ? ");) {

			ps.setString(1, "%" + parteDiCodiceFiscaleInput + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Compagnia compagniaTemp = new Compagnia();
					compagniaTemp.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
					compagniaTemp.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
					compagniaTemp.setDataFondazione(
							rs.getDate("DATAFONDAZIONE") != null ? rs.getDate("DATAFONDAZIONE").toLocalDate() : null);
					compagniaTemp.setId(rs.getLong("ID"));

					Impiegato impiegatoTemp = new Impiegato();
					impiegatoTemp.setNome(rs.getString("NOME"));
					impiegatoTemp.setCognome(rs.getString("COGNOME"));
					impiegatoTemp.setCodiceFiscale(rs.getString("CODICEFISCALE"));
					impiegatoTemp.setDataNascita(
							rs.getDate("DATANASCITA") != null ? rs.getDate("DATANASCITA").toLocalDate() : null);
					impiegatoTemp.setDataAssunzione(
							rs.getDate("DATAASSUNZIONE") != null ? rs.getDate("DATAASSUNZIONE").toLocalDate() : null);
					impiegatoTemp.setId(rs.getLong("ID"));
					impiegatiTemp.add(impiegatoTemp);
					compagniaTemp.setImpiegati(impiegatiTemp);

					result.add(compagniaTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

}
