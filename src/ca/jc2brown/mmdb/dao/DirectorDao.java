package ca.jc2brown.mmdb.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import ca.jc2brown.mmdb.model.Director;

@Repository
public class DirectorDao extends GenericDao<Director> implements DirectorDai {

	private static Logger log = Logger.getLogger( ActorDao.class.getName() );

	public DirectorDao() {
		super();
		log.debug("DirectorDao constructed");
	}
}