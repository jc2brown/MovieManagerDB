package ca.jc2brown.mmdb.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import ca.jc2brown.mmdb.model.Actor;

@Repository
public class ActorDao extends GenericDao<Actor> implements ActorDai {

	private static Logger log = Logger.getLogger( ActorDao.class.getName() );

	public ActorDao() {
		super();
		log.debug("ActorDao constructed");
	}
	
}