package com.avaliacao.desafioHyperativa.repository;

import com.avaliacao.desafioHyperativa.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    Cartao findByNumero(String numero);

    List<Cartao> findByUsuarioId(Long usuarioId);
}