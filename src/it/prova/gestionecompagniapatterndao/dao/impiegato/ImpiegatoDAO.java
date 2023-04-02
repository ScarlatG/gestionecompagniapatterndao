package it.prova.gestionecompagniapatterndao.dao.impiegato;

import java.time.LocalDate;
import java.util.List;

import it.prova.gestionecompagniapatterndao.dao.IBaseDAO;
import it.prova.gestionecompagniapatterndao.model.Compagnia;
import it.prova.gestionecompagniapatterndao.model.Impiegato;

public interface ImpiegatoDAO extends IBaseDAO<Impiegato> {

	public List<Impiegato> findAllByCompagnia(Compagnia compagniaInput) throws Exception;

	public int countByDataFondazioneCompagniaGreaterThan(LocalDate dateCreatedInput) throws Exception;

	public List<Impiegato> findAllByCompagniaConFatturatoMaggioreDi(int fatturatoInput) throws Exception;

	public List<Impiegato> findAllErroriAssunzione() throws Exception;

	public Impiegato getEager(Long idInput) throws Exception;

}