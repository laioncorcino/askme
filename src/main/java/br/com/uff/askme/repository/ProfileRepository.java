package br.com.uff.askme.repository;

import br.com.uff.askme.model.Profile;
import br.com.uff.askme.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile findByRole(Role role);
}
