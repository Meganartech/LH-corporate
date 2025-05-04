package com.knowledgeVista.Email;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MailkeysRepo extends JpaRepository<Mailkeys,Long> {


	@Query("SELECT u FROM Mailkeys u WHERE u.institution = ?1")
	Optional<Mailkeys>FindMailkeyByInstituiton(String institution);
}
