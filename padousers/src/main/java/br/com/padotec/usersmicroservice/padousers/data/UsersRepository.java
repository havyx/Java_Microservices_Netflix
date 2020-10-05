package br.com.padotec.usersmicroservice.padousers.data;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<UserEntity, Long> { //<TIPO DO OBJETO A SER SALVO E O TIPO DO ID>
UserEntity findByEmail(String email);
}
